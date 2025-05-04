package com.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpsplashConfig {
    @Value("${spring.application.upsplash.url}")
    private String upsplashUrl;
    @Value("${spring.application.upsplash.access-token}")
    private String upsplashAccesstoken;
}
