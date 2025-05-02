package com.lss.l9springDataJpaCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyTransactionAspect {

    private EntityManagerFactory emf;

    @Autowired
    public MyTransactionAspect(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Around("@annotation(MyTransactional)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        EntityManager em = emf.createEntityManager();
        EntityManagerHolder.set(em);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Object result = pjp.proceed();
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            EntityManagerHolder.clear();
            em.close();
        }
    }
}
