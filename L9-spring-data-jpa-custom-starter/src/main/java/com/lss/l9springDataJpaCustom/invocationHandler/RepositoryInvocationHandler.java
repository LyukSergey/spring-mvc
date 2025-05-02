package com.lss.l9springDataJpaCustom.invocationHandler;

import com.lss.l9springDataJpaCustom.methodExtractor.RepositoryMethodExecutor;
import com.lss.l9springDataJpaCustom.holder.EntityManagerHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocationHandler implements InvocationHandler {

    private final EntityManagerFactory emf;
    private final Class<?> entityClass;

    public RepositoryInvocationHandler(EntityManagerFactory emf, Class<?> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        EntityManager em = EntityManagerHolder.get();
        boolean externalEm = (em != null);
        if (!externalEm) {
            em = emf.createEntityManager();
        }

        try {
            RepositoryMethodExecutor executor = new RepositoryMethodExecutor(em, entityClass);
            return executor.execute(method, args);
        } finally {
            if (!externalEm) {
                em.close();
            }
        }
    }

}
