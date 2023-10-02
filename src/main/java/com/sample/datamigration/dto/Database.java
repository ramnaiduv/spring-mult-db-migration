package com.sample.datamigration.dto;

import lombok.Data;

import java.util.List;

@Data public class Database {

    String url;
    String user;
    String password;
    String schema;
    String dbtable;
    List<String> columns;

}
