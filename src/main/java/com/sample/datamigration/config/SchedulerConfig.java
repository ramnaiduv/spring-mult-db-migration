package com.sample.datamigration.config;

import com.sample.datamigration.one.query.QueryOneRepository;
import com.sample.datamigration.spark.MigrationJob;
import com.sample.datamigration.two.query.QueryTwoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
@Configuration
@EnableScheduling
@Slf4j
public class SchedulerConfig {

    @Autowired
    MigrationJob migrationJob;

    @Autowired
    QueryOneRepository queryOneRepository;

    @Autowired
    QueryTwoRepository queryTwoRepository;

    @Async
    @Scheduled(fixedRateString = "${spring.job.schedule}")
    public void scheduleJob() {

        log.info(
                "Job Execute every - {} " ,System.currentTimeMillis());
        log.info("Query One: {}, ", queryOneRepository.findAll());
        log.info("Query Two: {}, ", queryTwoRepository.findAll());


    }
}
