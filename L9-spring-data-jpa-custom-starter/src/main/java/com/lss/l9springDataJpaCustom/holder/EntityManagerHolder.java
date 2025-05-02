package com.lss.l9springDataJpaCustom.holder;

import jakarta.persistence.EntityManager;

public class EntityManagerHolder {

    private static final ThreadLocal<EntityManager> holder = new ThreadLocal<>();

    public static void set(EntityManager em) {
        holder.set(em);
    }

    public static EntityManager get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}