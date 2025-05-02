package com.lss.l9springDataJpaCustom;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class MyJpaFactory {

    public EntityManagerFactory createEntityManagerFactory(DataSource dataSource) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        props.put("javax.persistence.nonJtaDataSource", dataSource);

        PersistenceUnitInfo info = new PersistenceUnitInfoImpl(
                "myjpa",
                MyJpaMetadataHolder.getEntityClassNames().stream().toList()
        );

        return new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(info, props);
    }
}
