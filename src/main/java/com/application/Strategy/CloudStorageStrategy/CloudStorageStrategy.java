package com.application.Strategy.CloudStorageStrategy;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface CloudStorageStrategy {
    void uploadFile(String fileName, byte[] fileData, String contentType) throws Exception;

    default CompletableFuture<Void> uploadFileAsync(String fileName, byte[] fileData, String contentType) {
        return CompletableFuture.runAsync(() -> {
            try {
                uploadFile(fileName, fileData, contentType);
            } catch (Exception e) {
                throw new RuntimeException("File upload failed", e);
            }
        });
    }

    String getPresignedGetUrl(String fileName) throws Exception;

    String getPresignedGetUrl(Duration duration) throws Exception;

    String getPresignedGetUrl() throws Exception;

    String getPresignedGetUrl(String filename, Duration duration) throws Exception;
}
