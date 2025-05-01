package com.lss.l9springDataJpaCustom;

import java.util.Map;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class MyJpaRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes("com.lss.l9springDataJpaCustom.EnableMyJpa");
        String[] basePackages = (String[]) attributes.get("basePackages");
        if (basePackages == null || basePackages.length == 0) {
            basePackages = new String[]{"com.example.repository"};
        }

        var scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(org.springframework.beans.factory.annotation.AnnotatedBeanDefinition beanDefinition) {
                return true;
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(org.springframework.stereotype.Repository.class));

        for (String basePackage : basePackages) {
            var candidates = scanner.findCandidateComponents(basePackage);
            for (var candidate : candidates) {
                try {
                    Class<?> repoInterface = Class.forName(candidate.getBeanClassName());
                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(MyRepositoryFactoryBean.class);
                    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, repoInterface);
                    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, "entityManagerFactory");
                    registry.registerBeanDefinition(candidate.getBeanClassName(), beanDefinition);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}