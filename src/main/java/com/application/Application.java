package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableFeignClients
public class Application {

    public static void main(String[] args) {

        // Set the port dynamically

        SpringApplication.run(Application.class, args);
    }
}
