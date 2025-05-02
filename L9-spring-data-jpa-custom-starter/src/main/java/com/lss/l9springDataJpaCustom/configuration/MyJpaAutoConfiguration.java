package com.lss.l9springDataJpaCustom.configuration;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class MyJpaAutoConfiguration {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl() != null ? properties.getUrl() : "jdbc:postgresql://localhost:5432/defaultdb";
        String username = properties.getUsername() != null ? properties.getUsername() : "default_user";
        String password = properties.getPassword() != null ? properties.getPassword() : "default_pass";
        String driver = properties.getDriverClassName() != null ? properties.getDriverClassName() : "org.postgresql.Driver";

        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driver)
                .build();
    }
}
