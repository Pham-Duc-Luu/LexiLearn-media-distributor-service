package com.application;

import com.application.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class Application {

    public static void main(String[] args) {

        // Set the port dynamically
        String appPort = DotenvConfig.getEnv("APP_PORT");

        if (appPort != null) {
            System.setProperty("server.port", appPort);
        }
        SpringApplication.run(Application.class, args);
    }
}
