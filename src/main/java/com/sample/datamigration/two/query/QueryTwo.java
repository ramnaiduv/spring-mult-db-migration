package com.sample.datamigration.two.query;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "QUERY_TWO")
public class QueryTwo {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    @Column(name = "JOB_ID") private Integer jobId;

    @Column(name = "JOB_NAME")private String jobName;

    @Column(name = "CUSTOM_QUERY")private String customQuery;
    @Column(name = "CREATED_DATE")private Date createdDate;
}
