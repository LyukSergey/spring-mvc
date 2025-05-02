package com.lss.l9springDataJpaCustom.methodExtractor;

import com.lss.l9springDataJpaCustom.resolver.EntityNameResolver;
import com.lss.l9springDataJpaCustom.query.JpqlQueryBuilder;
import jakarta.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.List;

public class RepositoryMethodExecutor {

    private final EntityManager em;
    private final Class<?> entityClass;
    private final String entityName;

    public RepositoryMethodExecutor(EntityManager em, Class<?> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
        this.entityName = EntityNameResolver.resolve(entityClass);
    }

    public Object execute(Method method, Object[] args) {
        String methodName = method.getName();

        return switch (methodName) {
            case "save" -> em.merge(args[0]);
            case "deleteById" -> {
                Object entity = em.find(entityClass, args[0]);
                if (entity != null) {
                    em.remove(entity);
                }
                yield null;
            }
            case "findAll" -> em.createQuery("SELECT e FROM " + entityName + " e", entityClass).getResultList();
            case "findById" -> em.find(entityClass, args[0]);
            default -> {
                if (methodName.startsWith("findBy")) {
                    String jpql = JpqlQueryBuilder.buildFindByQuery(entityName, methodName);
                    var query = em.createQuery(jpql, entityClass);
                    String[] parts = methodName.substring(6).split("And");
                    for (int i = 0; i < parts.length; i++) {
                        query.setParameter("param" + i, args[i]);
                    }
                    if (method.getReturnType().isAssignableFrom(List.class)) {
                        yield query.getResultList();
                    } else {
                        yield query.getSingleResult();
                    }
                }
                throw new UnsupportedOperationException("Method not supported: " + methodName);
            }
        };
    }
}
