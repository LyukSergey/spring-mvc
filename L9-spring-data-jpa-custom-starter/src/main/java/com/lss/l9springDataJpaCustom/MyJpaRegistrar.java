package com.lss.l9springDataJpaCustom;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class MyJpaRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableMyJpa.class.getName());
        String[] basePackages = (String[]) attributes.get("basePackages");
        if (basePackages == null || basePackages.length == 0) {
            basePackages = new String[]{"com.example.repository"};
        }

        Set<String> entityClasses = new HashSet<>();
        for (String pkg : basePackages) {
            entityClasses.addAll(scanForEntities(pkg));
        }
        System.out.println("Entities found: " + entityClasses);
        MyJpaMetadataHolder.setEntityClassNames(entityClasses);

        // Регіструємо фабрику (як звичайний Spring bean)
        registry.registerBeanDefinition("myJpaFactory",
                BeanDefinitionBuilder.genericBeanDefinition(MyJpaFactory.class).getBeanDefinition());
        // Регіструємо EntityManagerFactory через factory method
        BeanDefinitionBuilder emfBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(EntityManagerFactory.class);
        emfBuilder.setFactoryMethodOnBean("createEntityManagerFactory", "myJpaFactory");
        emfBuilder.addConstructorArgReference("dataSource");  // inject DataSource
        registry.registerBeanDefinition("entityManagerFactory", emfBuilder.getBeanDefinition());

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

    private Set<String> scanForEntities(String basePackage) {
        Set<String> entities = new HashSet<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            entities.add(bd.getBeanClassName());
        }
        return entities;
    }

}