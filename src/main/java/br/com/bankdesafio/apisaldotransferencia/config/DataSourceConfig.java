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

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Ajustes para suportar alta carga
        dataSource.setMaxActive(6000);
        dataSource.setMinIdle(100);
        dataSource.setMaxIdle(1000);
        dataSource.setInitialSize(100);
        dataSource.setMaxWait(5000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setMinEvictableIdleTimeMillis(30000);
        dataSource.setTimeBetweenEvictionRunsMillis(15000);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(false);
        dataSource.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
        ); // Interceptors para otimizar o pool

        return dataSource;
    }
}