package com.lss.l9springdataspringbootcustom;

import com.lss.l9springDataJpaCustom.annotation.EnableMyJpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMyJpa(basePackages = "com.lss.l9springdataspringbootcustom")
public class L9SpringDataSpringBootCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(L9SpringDataSpringBootCustomApplication.class, args);
    }

}
