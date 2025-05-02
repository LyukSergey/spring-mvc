package com.lss.l9springdataspringbootcustom;

import com.lss.l9springDataJpaCustom.rootRepository.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MyJpaRepository<Post, Long> {

}
