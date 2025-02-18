package com.application.config;

public class UpsplashConfig {
    private final String upsplashUrl = DotenvConfig.getEnv("UPSPLASH_URL");
    private final String upsplashAccesstoken = DotenvConfig.getEnv("UPSPLASH_ACCESS_TOKEN");
}
