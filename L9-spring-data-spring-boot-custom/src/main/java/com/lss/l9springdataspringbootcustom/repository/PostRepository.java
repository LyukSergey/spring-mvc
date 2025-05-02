package com.lss.l9springdataspringbootcustom.repository;

import com.lss.l9springDataJpaCustom.annotation.MyRepository;
import com.lss.l9springDataJpaCustom.rootRepository.MyJpaRepository;
import com.lss.l9springdataspringbootcustom.entity.Post;

@MyRepository
public interface PostRepository extends MyJpaRepository<Post, Long> {

}
