package com.lss.l9springdataspringbootcustom.repository;

import com.lss.l9springDataJpaCustom.rootRepository.MyJpaRepository;
import com.lss.l9springdataspringbootcustom.entity.Users;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MyJpaRepository<Users, Long> {

    List<Users> findByName(String name);
}
