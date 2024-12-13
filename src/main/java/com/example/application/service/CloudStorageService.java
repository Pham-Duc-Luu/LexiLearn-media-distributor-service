package com.example.application.service;

import com.example.application.Strategy.CloudStorageStrategy.CloudStorageStrategy;
import com.example.application.Strategy.CloudStorageStrategy.GoogleCloudStorageStrategy;
import com.example.application.Strategy.CloudStorageStrategy.S3StorageStrategy;
import com.example.application.util.CloudProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class CloudStorageService {
    private CloudStorageStrategy cloudStorageStrategy;

    public CloudStorageService() {
    }

    // Allow switching strategy dynamically
    public CloudStorageService(CloudProvider context) {

        // * if this the Service init strategy equals "Amazon S3" => init S3 store
        if (context.equals(CloudProvider.AMAZON_S3)) {
            this.cloudStorageStrategy = new S3StorageStrategy();
        }

        // * if this the Service init strategy equals "Google cloud" => init S3 store
        if (context.equals(CloudProvider.GOOGLE_CLOUD)) {
            this.cloudStorageStrategy = new GoogleCloudStorageStrategy();
        }

    }

    @Async
    public void uploadFile(String fileName, byte[] fileData) throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        cloudStorageStrategy.uploadFile(fileName, fileData);
    }

    public String getPresignedGetUrl(String filename, Duration duration) throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.getPresignedGetUrl(filename, duration);
    }

    public String getPresignedGetUrl(Duration duration) throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.getPresignedGetUrl(duration);
    }

    public String getPresignedGetUrl() throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.getPresignedGetUrl();
    }


}