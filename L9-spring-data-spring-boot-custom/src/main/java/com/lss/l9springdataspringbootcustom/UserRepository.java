package com.lss.l9springdataspringbootcustom;

import com.lss.l9springDataJpaCustom.MyJpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MyJpaRepository<Users, Long> {

    List<Users> findByName(String name);

    List<Users> findAllUsers();
}
