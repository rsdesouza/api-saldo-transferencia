package br.com.bankdesafio.apisaldotransferencia.config;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Para MySQL 8
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setInitialSize(5);
        dataSource.setMaxWait(10000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(5000);

        return dataSource;
    }
}
