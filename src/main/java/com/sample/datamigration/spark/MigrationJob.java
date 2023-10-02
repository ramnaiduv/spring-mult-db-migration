package com.sample.datamigration.spark;

import com.sample.datamigration.dto.BatchProcess;
import com.sample.datamigration.dto.ConnectionChannelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MigrationJob  extends AbstractMigrationJob{

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.data-ram-migration.channel.save.format}")
    String format;

    public MigrationJob(final ConnectionChannelDTO channelDTO, final BatchProcess batchProcess) {
        super(channelDTO, batchProcess);
    }

    public void execute(String customQuery) {
        try {
            log.info("Migration {} format: {}, custom Query: {}",batchProcess, format, customQuery);
            int batchSize = getBatchProcess().getBatchSize();
            int recordsPerBatch = getBatchProcess().getRecordsPerBatch();
            for(int batchIdx=0;batchIdx<batchSize;batchIdx++){
                int offset = batchIdx * recordsPerBatch;
                SparkThread sparkThread = applicationContext.getBean(SparkThread.class,getCurrentSQLContext(),batchIdx,recordsPerBatch,offset,channelDTO,format,objectMapper);
                sparkThread.run();
                Thread.sleep(1000);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
