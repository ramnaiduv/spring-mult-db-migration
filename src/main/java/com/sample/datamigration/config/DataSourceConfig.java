package com.sample.datamigration.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.one")
    @Primary
    public DataSourceProperties queryOneDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.two")
    public DataSourceProperties queryTwoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary public DataSource queryOneDataSource() {
        return queryOneDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public DataSource queryTwoDataSource() {
        return queryTwoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
