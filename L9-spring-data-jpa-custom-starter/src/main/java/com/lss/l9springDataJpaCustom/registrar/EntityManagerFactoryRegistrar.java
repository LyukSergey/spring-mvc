package com.lss.l9springDataJpaCustom.registrar;

import com.lss.l9springDataJpaCustom.factory.MyJpaFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class EntityManagerFactoryRegistrar {

    public void register(BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition("myJpaFactory",
                BeanDefinitionBuilder.genericBeanDefinition(MyJpaFactory.class).getBeanDefinition());

        BeanDefinitionBuilder emfBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(EntityManagerFactory.class);
        emfBuilder.setFactoryMethodOnBean("createEntityManagerFactory", "myJpaFactory");
        emfBuilder.addConstructorArgReference("dataSource");
        registry.registerBeanDefinition("entityManagerFactory", emfBuilder.getBeanDefinition());
    }
}
