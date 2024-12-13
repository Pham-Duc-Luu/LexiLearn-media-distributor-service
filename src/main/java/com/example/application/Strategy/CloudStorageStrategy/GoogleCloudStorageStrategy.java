package com.example.application.Strategy.CloudStorageStrategy;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class GoogleCloudStorageStrategy implements CloudStorageStrategy {
    @Override
    public void uploadFile(String fileName, byte[] fileData) {
        // Logic to upload file to Google Cloud Storage
    }

    @Override
    public String getPresignedGetUrl(Duration duration) {
        return "";
    }

    @Override
    public String getPresignedGetUrl() throws Exception {
        return "";
    }

    @Override
    public String getPresignedGetUrl(String filename, Duration duration) throws Exception {
        return "";
    }
}