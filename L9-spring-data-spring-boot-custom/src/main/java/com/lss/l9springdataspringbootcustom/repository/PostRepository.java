package com.lss.l9springdataspringbootcustom.repository;

import com.lss.l9springDataJpaCustom.rootRepository.MyJpaRepository;
import com.lss.l9springdataspringbootcustom.entity.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MyJpaRepository<Post, Long> {

}
