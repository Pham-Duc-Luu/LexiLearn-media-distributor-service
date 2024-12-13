package com.example.application.Strategy.CloudStorageStrategy;

import java.time.Duration;

public interface CloudStorageStrategy {
    void uploadFile(String fileName, byte[] fileData) throws Exception;

    String getPresignedGetUrl(Duration duration) throws Exception;

    String getPresignedGetUrl() throws Exception;

    String getPresignedGetUrl(String filename, Duration duration) throws Exception;
}
