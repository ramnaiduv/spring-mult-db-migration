package com.sample.datamigration.spark;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class SparkJob {

    private static JavaSparkContext sparkContext;
    private List<SQLContext> sqlContexts = new ArrayList<>();


    public SparkJob(final String master) {
        initSpark(master);

    }

    private void initSpark(String master) {
        if(sparkContext == null) {
            SparkConf sparkConf = new SparkConf().setAppName("DataMigration - Spark");
            sparkConf.set("spark.master",master);
            //sparkConf.set("spark.ui.port","8081");
            //sparkConf.set("spark.ssl.enabled","true");
            sparkConf.set("spark.sql.sources.partitionOverwriteMode","dynamic");
            sparkConf.set("spark.sql.shuffle.partitions", "500");
            sparkContext = new JavaSparkContext(sparkConf);
        }
    }
    public SQLContext getCurrentSQLContext() {
        return new SQLContext(sparkContext);
    }

    protected void close() {
        sparkContext.close();
        sparkContext = null;
    }
}
