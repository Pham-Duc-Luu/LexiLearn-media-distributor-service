package com.application.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${spring.application.cloudinary.url}")
    private String url;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(url);
    }
}
