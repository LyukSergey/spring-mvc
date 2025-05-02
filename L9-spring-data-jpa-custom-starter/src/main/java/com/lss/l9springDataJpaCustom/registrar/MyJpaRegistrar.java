package com.lss.l9springDataJpaCustom.registrar;

import com.lss.l9springDataJpaCustom.factoryBean.MyRepositoryFactoryBean;
import com.lss.l9springDataJpaCustom.annotation.EnableMyJpa;
import com.lss.l9springDataJpaCustom.holder.MyJpaMetadataHolder;
import com.lss.l9springDataJpaCustom.scaner.EntityScanner;
import com.lss.l9springDataJpaCustom.scaner.RepositoryScanner;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyJpaRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableMyJpa.class.getName());
        String[] basePackages = (String[]) attributes.get("basePackages");
        if (basePackages == null || basePackages.length == 0) {
            basePackages = new String[]{"com.example.repository"};
        }

        // Скануємо Entity
        Set<String> entityClasses = new EntityScanner().scan(basePackages);
        MyJpaMetadataHolder.setEntityClassNames(entityClasses);

        // Реєструємо фабрику
        new EntityManagerFactoryRegistrar().register(registry);

        // Скануємо Repository
        Set<Class<?>> repos = new RepositoryScanner().scan(basePackages);

        // Реєструємо BeanDefinitions для Repository
        for (Class<?> repository : repos) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .genericBeanDefinition(MyRepositoryFactoryBean.class);
            builder.addConstructorArgValue(repository);
            builder.addConstructorArgValue("entityManagerFactory");
            registry.registerBeanDefinition(repository.getName(), builder.getBeanDefinition());
        }
    }

}