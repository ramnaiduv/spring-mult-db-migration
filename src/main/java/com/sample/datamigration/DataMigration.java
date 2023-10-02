package com.sample.datamigration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMigration implements CommandLineRunner {



    public static void main(String[] args) {

        SpringApplication.run(DataMigration.class);
    }

    @Override
    public void run(String... args) throws Exception {

        //migrationJob.execute();
    }
}

