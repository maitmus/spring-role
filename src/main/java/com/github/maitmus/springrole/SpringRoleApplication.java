package com.github.maitmus.springrole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringRoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRoleApplication.class, args);
    }
}
