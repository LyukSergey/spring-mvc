package com.lss.l9springDataJpaCustom;

import java.util.List;

public interface MyJpaRepository<T, ID> {

    T findById(ID id);

    List<T> findAll();
}
