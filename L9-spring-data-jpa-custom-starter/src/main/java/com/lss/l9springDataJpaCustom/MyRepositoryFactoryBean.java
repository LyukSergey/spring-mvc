package com.lss.l9springDataJpaCustom;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
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
        this.entityClass = resolveEntityClass(repositoryInterface);

        if (this.entityClass == null) {
            throw new IllegalArgumentException("Cannot resolve entity class for " + repositoryInterface.getName());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Class<?> resolveEntityClass(Class<?> repoInterface) {
        for (Type iface : repoInterface.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt) {
                Class<?> raw = (Class<?>) pt.getRawType();
                if (raw.getSimpleName().equals("MyJpaRepository")) {
                    return (Class<?>) pt.getActualTypeArguments()[0];
                } else {
                    Class<?> candidate = resolveEntityClass(raw);
                    if (candidate != null) {
                        return candidate;
                    }
                }
            } else if (iface instanceof Class<?> clz) {
                Class<?> candidate = resolveEntityClass(clz);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        EntityManagerFactory emf = applicationContext.getBean(entityManagerFactoryBeanName, EntityManagerFactory.class);
        EntityManager em = emf.createEntityManager();
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class[]{repositoryInterface},
                new RepositoryInvocationHandler(em, entityClass)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return repositoryInterface;
    }

    static class RepositoryInvocationHandler implements InvocationHandler {

        private final EntityManager em;
        private final Class<?> entityClass;

        RepositoryInvocationHandler(EntityManager em, Class<?> entityClass) {
            this.em = em;
            this.entityClass = entityClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            String entityName = entityClass.getSimpleName();
            Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
            if (entityAnnotation != null && !entityAnnotation.name().isEmpty()) {
                entityName = entityAnnotation.name();
            }

            if (methodName.equals("findAll")) {
                return em.createQuery("SELECT e FROM " + entityName + " e", entityClass).getResultList();
            }
            if (methodName.equals("findById")) {
                return em.find(entityClass, args[0]);
            }
            if (methodName.startsWith("findBy")) {
                String field = methodName.substring(6);
                String jpql = "SELECT e FROM " + entityName + " e WHERE e." +
                        Character.toLowerCase(field.charAt(0)) + field.substring(1) + " = :param";
                return em.createQuery(jpql, entityClass)
                        .setParameter("param", args[0])
                        .getResultList();
            }
            throw new UnsupportedOperationException("Method not supported: " + methodName);
        }

    }

}
