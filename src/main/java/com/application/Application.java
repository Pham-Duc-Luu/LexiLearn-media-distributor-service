package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class Application {

    public static void main(String[] args) {

        // Set the port dynamically

        SpringApplication.run(Application.class, args);
    }
}
