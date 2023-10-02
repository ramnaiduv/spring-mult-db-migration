package com.sample.datamigration.one.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QUERY_ONE")
public class QueryOne {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    @Column(name = "JOB_ID") private Integer jobId;

    @Column(name = "JOB_NAME")private String jobName;

    @Column(name = "CUSTOM_QUERY")private String customQuery;
    @Column(name = "CREATED_DATE")private Date createdDate;
}
