package com.sample.datamigration.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "spring.data-ram-migration.channel.database")
public class ConnectionChannelDTO {

    private Database source;
    private Database target;

    private SparkConfig sparkConfig;


}
