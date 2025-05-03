package com.lss.l10springsecurity.repository;

import com.lss.l10springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
