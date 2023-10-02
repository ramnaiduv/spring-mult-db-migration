package com.sample.datamigration.spark;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.datamigration.dto.ConnectionChannelDTO;
import lombok.extern.log4j.Log4j;
import org.apache.spark.sql.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.collection.immutable.Seq;
import scala.jdk.CollectionConverters;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Component
@Scope("prototype")
@Log4j
public class SparkThread extends Thread{

    final int jobId;
    final int limit;
    final int offset;

    final SQLContext sqlContext;
    final ConnectionChannelDTO channelDTO;
    final ObjectMapper objectMapper;
    final String format;


    public SparkThread(SQLContext sqlContext, int jobId, int limit, int offset, ConnectionChannelDTO channelDTO, String format, ObjectMapper objectMapper) {
        this.jobId = jobId;
        this.limit = limit;
        this.offset = offset;
        this.sqlContext = sqlContext;
        this.channelDTO = channelDTO;
        this.format = format;
        this.objectMapper = objectMapper;
    }


    public void run() {
        log.info("loading.... "+ offset);
        load(extract(limit,offset));
    }
    public void load(Dataset<Row> mainDataset) {

        Map<String,String>  targetSystemOptions = objectMapper.convertValue(channelDTO.getTarget(), Map.class);
        targetSystemOptions.putAll(objectMapper.convertValue(channelDTO.getSparkConfig(),Map.class));
        targetSystemOptions.remove("schema");
        targetSystemOptions.remove("columns");


        if(format.equalsIgnoreCase("JDBC")) {
            mainDataset
                .write()
                .options(targetSystemOptions)
                .format("jdbc")
                .mode(SaveMode.Append).save();
        } else if(format.equalsIgnoreCase("CSV")) {

            generateCSV(mainDataset, "/tmp/data_rm/","/tmp/finalFile.csv");

        } else if(format.equalsIgnoreCase("EXCEL")) {
            generateExcel(mainDataset,"/tmp/finalFile.xlsx");

        }

    }

    private void generateExcel(Dataset<Row> mainDataset, String finalPath) {
        mainDataset.write()
                .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                //.option("dataAddress", "'My Sheet'!B3:C35")
                .option("header", offset==0? "true" : "false")
                .option("dateFormat", "yy-mmm-d") // Optional, default: yy-m-d h:mm
                .option("timestampFormat", "mm-dd-yyyy hh:mm:ss") // Optional, default: yyyy-mm-dd hh:mm:ss.000
                .mode("append") // Optional, default: overwrite.
                .save(finalPath);
    }



    private void generateCSV(Dataset<Row> mainDataset,String generatedPartCSVPath,String finalPath) {

        mainDataset.write().option("header", offset==0? true : false)
                .mode(SaveMode.Append)
                .csv(generatedPartCSVPath);

        File targetFile = new File(generatedPartCSVPath);
        String[] listedFiles = targetFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("part-0000")?true:false;
            }
        });
        try {

            FileWriter fstream = new FileWriter(new File(finalPath), true);
            BufferedWriter out = new BufferedWriter(fstream);
            Arrays.stream(listedFiles).forEach(fileName -> {
                try {
                    File sourceFile  = new File(generatedPartCSVPath+fileName);
                    FileInputStream sfis = new FileInputStream(sourceFile);
                    BufferedReader in = new BufferedReader(new InputStreamReader(sfis));
                    String aLine;
                    while((aLine = in.readLine()) != null) {
                        out.write(aLine);
                        out.write("\n");
                    }
                    in.close();
                    sourceFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Dataset<Row> extract(int limit, int offset)  {

        Seq<String> seqTargetNames = CollectionConverters.IteratorHasAsScala(channelDTO.getTarget().getColumns().iterator()).asScala().toSeq();
        List<Column> targetColumns = channelDTO.getTarget().getColumns().stream().map(targetField -> col(targetField)).collect(Collectors.toList());
        Seq<Column> seqSourceCols = CollectionConverters.IteratorHasAsScala(channelDTO.getSource().getColumns().stream()
                .map(mp->col(mp)).iterator()).asScala().toSeq();
        Column[] projectCols = targetColumns.toArray(new Column[targetColumns.size()]);
        Map<String,String> sourceSystemOptions = objectMapper.convertValue(channelDTO.getSource(), Map.class);
        sourceSystemOptions.putAll(objectMapper.convertValue(channelDTO.getSparkConfig(),Map.class));
        sourceSystemOptions.remove("columns");
        sourceSystemOptions.remove("schema");
        String sourceDBTable = sourceSystemOptions.get("dbtable");
        sourceSystemOptions.put("query","select * from "+sourceDBTable+" limit "+limit+" offset "+offset+"");
        sourceSystemOptions.remove("dbtable");
        return sqlContext.read().format("jdbc").
                options(sourceSystemOptions).load().withColumns(seqTargetNames,seqSourceCols).select(projectCols);

    }
}
