package com.autoorion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class AutoorionSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoorionSysApplication.class, args);
    }
}
