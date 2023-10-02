package com.sample.datamigration.config;

import com.sample.datamigration.one.query.QueryOne;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackageClasses = QueryOne.class,
        entityManagerFactoryRef = "queryOneEntityManagerFactory",
        transactionManagerRef = "queryOneTransactionManager")
public class QueryOneJPAConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean queryOneEntityManagerFactory(
            @Qualifier("queryOneDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages(QueryOne.class)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager queryOneTransactionManager(
            @Qualifier("queryOneEntityManagerFactory") LocalContainerEntityManagerFactoryBean queryOneEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(queryOneEntityManagerFactory.getObject()));
    }
}

