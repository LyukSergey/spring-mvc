package com.lss.l9springdataspringbootcustom.repository;

import com.lss.l9springDataJpaCustom.annotation.MyRepository;
import com.lss.l9springDataJpaCustom.rootRepository.MyJpaRepository;
import com.lss.l9springdataspringbootcustom.entity.Users;
import java.util.List;

@MyRepository
public interface UserRepository extends MyJpaRepository<Users, Long> {

    List<Users> findByName(String name);
}
