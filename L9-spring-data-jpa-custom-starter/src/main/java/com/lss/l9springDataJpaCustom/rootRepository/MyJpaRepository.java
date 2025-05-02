package com.lss.l9springDataJpaCustom.rootRepository;

import java.util.List;

public interface MyJpaRepository<T, ID> {

    T save(T entity);

    void deleteById(ID id);

    T findById(ID id);

    List<T> findAll();
}
