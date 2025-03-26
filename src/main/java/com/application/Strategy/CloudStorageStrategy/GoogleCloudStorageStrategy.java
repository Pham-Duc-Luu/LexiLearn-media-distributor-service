package com.application.Strategy.CloudStorageStrategy;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service("googleCloudStorage")  // This name will be used for dependency injection
public class GoogleCloudStorageStrategy implements CloudStorageStrategy {
    @Override
    public void uploadFile(String fileName, byte[] fileData) {
        // Logic to upload file to Google Cloud Storage
    }

    @Override
    public CompletableFuture<Void> uploadFileAsync(String fileName, byte[] fileData) {
        return CloudStorageStrategy.super.uploadFileAsync(fileName, fileData);
    }

    @Override
    public String getPresignedGetUrl(String fileName) throws Exception {
        return "";
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