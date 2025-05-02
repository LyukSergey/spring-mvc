package com.lss.l9springDataJpaCustom.factoryBean;

import com.lss.l9springDataJpaCustom.invocationHandler.RepositoryInvocationHandler;
import com.lss.l9springDataJpaCustom.resolver.EntityClassResolver;
import jakarta.persistence.EntityManagerFactory;
import java.lang.reflect.Proxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyRepositoryFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private final Class<?> repositoryInterface;
    private final String entityManagerFactoryBeanName;
    private ApplicationContext applicationContext;
    private final Class<?> entityClass;

    public MyRepositoryFactoryBean(Class<?> repositoryInterface, String entityManagerFactoryBeanName) {
        this.repositoryInterface = repositoryInterface;
        this.entityManagerFactoryBeanName = entityManagerFactoryBeanName;
        this.entityClass = EntityClassResolver.resolve(repositoryInterface);
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot resolve entity class for " + repositoryInterface.getName());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        EntityManagerFactory emf = applicationContext.getBean(entityManagerFactoryBeanName, EntityManagerFactory.class);
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class[]{repositoryInterface},
                new RepositoryInvocationHandler(emf, entityClass)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return repositoryInterface;
    }


}
