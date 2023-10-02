package com.sample.datamigration.dto;

import lombok.Data;

@Data
public class SparkConfig {
    String fetchSize;
    String numPartitions;
    String master;

}
