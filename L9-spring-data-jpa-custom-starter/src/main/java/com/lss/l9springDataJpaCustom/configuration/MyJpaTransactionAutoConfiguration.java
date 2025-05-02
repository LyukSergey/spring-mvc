package com.lss.l9springDataJpaCustom.configuration;

import com.lss.l9springDataJpaCustom.aspect.MyTransactionAspect;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MyJpaTransactionAutoConfiguration {

    @Bean
    public MyTransactionAspect myTransactionAspect(EntityManagerFactory myJpaFactory) {
        return new MyTransactionAspect(myJpaFactory);
    }
}
