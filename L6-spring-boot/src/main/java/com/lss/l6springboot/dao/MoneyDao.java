package com.lss.l6springboot.dao;

import com.lss.l6springboot.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyDao extends JpaRepository<Bank, String> {

}
