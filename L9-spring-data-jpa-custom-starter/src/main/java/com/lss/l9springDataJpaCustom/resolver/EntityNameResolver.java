package com.lss.l9springDataJpaCustom.resolver;

import jakarta.persistence.Entity;

public class EntityNameResolver {

    public static String resolve(Class<?> entityClass) {
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.name().isEmpty()) {
            return entityAnnotation.name();
        }
        return entityClass.getSimpleName();
    }
}
