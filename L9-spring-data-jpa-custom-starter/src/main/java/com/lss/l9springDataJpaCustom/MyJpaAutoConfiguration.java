package com.lss.l9springDataJpaCustom;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
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

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, ConfigurableApplicationContext context) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("javax.persistence.nonJtaDataSource", dataSource);
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        return new org.hibernate.jpa.HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(
                        buildPersistenceUnitInfo(),
                        props
                );
    }

    private PersistenceUnitInfo buildPersistenceUnitInfo() {
        return new PersistenceUnitInfoImpl("myjpa", List.of("com.lss.l9springdataspringbootcustom.Users"));
    }
}
