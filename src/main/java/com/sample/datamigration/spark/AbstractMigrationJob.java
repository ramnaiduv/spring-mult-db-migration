package com.sample.datamigration.spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.datamigration.dto.BatchProcess;
import com.sample.datamigration.dto.ConnectionChannelDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AbstractMigrationJob extends SparkJob {

    final ConnectionChannelDTO channelDTO;
    final BatchProcess batchProcess;
    final ObjectMapper objectMapper = new ObjectMapper();

    public AbstractMigrationJob(final ConnectionChannelDTO channelDTO, BatchProcess batchProcess) {
        super(channelDTO.getSparkConfig().getMaster());
        this.channelDTO = channelDTO;

        this.batchProcess = batchProcess;
    }







}
