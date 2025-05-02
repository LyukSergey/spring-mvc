package com.lss.l9springDataJpaCustom.factoryBean;

import com.lss.l9springDataJpaCustom.holder.EntityManagerHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
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

    static class RepositoryInvocationHandler implements InvocationHandler {

        private final EntityManagerFactory myJpaFactory;
        private final Class<?> entityClass;

        RepositoryInvocationHandler(EntityManagerFactory emf, Class<?> entityClass) {
            this.myJpaFactory = emf;
            this.entityClass = entityClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            EntityManager em = EntityManagerHolder.get();
            boolean isExternalEm = true;
            if (em == null) {
                em = myJpaFactory.createEntityManager();
                isExternalEm = false;
            }
            try {
                String methodName = method.getName();
                String entityName = entityClass.getSimpleName();
                Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
                if (entityAnnotation != null && !entityAnnotation.name().isEmpty()) {
                    entityName = entityAnnotation.name();
                }

                if (methodName.equals("save")) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    Object merged = em.merge(args[0]);
                    tx.commit();
                    return merged;
                }

                if (methodName.equals("deleteById")) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    Object entity = em.find(entityClass, args[0]);
                    if (entity != null) {
                        em.remove(entity);
                    }
                    tx.commit();
                    return null;
                }

                if (methodName.equals("findAll")) {
                    return em.createQuery("SELECT e FROM " + entityName + " e", entityClass).getResultList();
                }

                if (methodName.equals("findById")) {
                    return em.find(entityClass, args[0]);
                }

                if (methodName.startsWith("findBy")) {
                    String fieldsPart = methodName.substring(6);
                    String[] parts = fieldsPart.split("And");
                    StringBuilder jpql = new StringBuilder("SELECT e FROM " + entityName + " e WHERE ");
                    for (int i = 0; i < parts.length; i++) {
                        String field = Character.toLowerCase(parts[i].charAt(0)) + parts[i].substring(1);
                        if (i > 0) {
                            jpql.append(" AND ");
                        }
                        jpql.append("e.").append(field).append(" = :param").append(i);
                    }
                    var query = em.createQuery(jpql.toString(), entityClass);
                    for (int i = 0; i < parts.length; i++) {
                        query.setParameter("param" + i, args[i]);
                    }
                    if (method.getReturnType().isAssignableFrom(List.class)) {
                        return query.getResultList();
                    } else {
                        return query.getSingleResult();
                    }
                }

                throw new UnsupportedOperationException("Method not supported: " + methodName);

            } finally {
                if (!isExternalEm) {
                    em.close();
                }
            }
        }

    }

}
