package com.lss.l102sringsecuritykeycloak.repository;

import com.lss.l102sringsecuritykeycloak.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByDeletedFalse();

}
