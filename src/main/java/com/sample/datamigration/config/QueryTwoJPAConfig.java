package com.sample.datamigration.config;

import com.sample.datamigration.two.query.QueryTwo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = QueryTwo.class,
        entityManagerFactoryRef = "queryTwoEntityManagerFactory",
        transactionManagerRef = "queryTwoTransactionManager")
public class QueryTwoJPAConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean queryTwoEntityManagerFactory(
            @Qualifier("queryTwoDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages(QueryTwo.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager queryTwoTransactionManager(
            @Qualifier("queryTwoEntityManagerFactory") LocalContainerEntityManagerFactoryBean queryTwoEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(queryTwoEntityManagerFactory.getObject()));
    }
}
