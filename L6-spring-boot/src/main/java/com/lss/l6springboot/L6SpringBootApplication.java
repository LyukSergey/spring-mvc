package com.lss.l6springboot;

import com.lss.l6springboot.properties.ProphetProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProphetProperties.class)
public class L6SpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(L6SpringBootApplication.class, args);
    }

}
