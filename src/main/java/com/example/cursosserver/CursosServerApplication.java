package com.example.cursosserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class CursosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursosServerApplication.class, args);
    }

}
