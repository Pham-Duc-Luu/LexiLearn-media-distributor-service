package com.application.service.CloudStoreService;

import com.application.Strategy.CloudStorageStrategy.CloudStorageStrategy;
import com.application.Strategy.CloudStorageStrategy.GoogleCloudStorageStrategy;
import com.application.Strategy.CloudStorageStrategy.S3StorageStrategy;
import com.application.util.CloudProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Service
public class CloudStorageService {
    @Autowired
    GoogleCloudStorageStrategy googleCloudStorageStrategy;

    private CloudStorageStrategy cloudStorageStrategy;
    @Autowired
    private S3StorageStrategy s3StorageStrategy;

    @PostConstruct
    private void init() {
        cloudStorageStrategy = s3StorageStrategy;
    }

    public void setCloudStorageStrategy(CloudProvider context) {

        // * if this the Service init strategy equals "Amazon S3" => init S3 store
        if (context.equals(CloudProvider.AMAZON_S3)) {
            this.cloudStorageStrategy = s3StorageStrategy;
        }

        // * if this the Service init strategy equals "Google cloud" => init S3 store
        if (context.equals(CloudProvider.GOOGLE_CLOUD)) {
            this.cloudStorageStrategy = googleCloudStorageStrategy;
        }
    }

    public void uploadFile(String fileName, byte[] fileData, String contentType) throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        cloudStorageStrategy.uploadFile(fileName, fileData, contentType);
    }

    @Async
    public CompletableFuture<Void> uploadFileAsync(String fileName, byte[] fileData, String contentType) {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.uploadFileAsync(fileName, fileData, contentType);
    }

    @Async
    public CompletableFuture<Void> uploadImageAsync(String fileName, byte[] fileData) {

        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        String contentType = "image/jpeg";
        return cloudStorageStrategy.uploadFileAsync(fileName, fileData, contentType);
    }


    @Async
    public CompletableFuture<Void> uploadAudioAsync(String fileName, byte[] fileData) {

        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        String contentType = "audio/mpeg";
        return cloudStorageStrategy.uploadFileAsync(fileName, fileData, contentType);
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

    public String getPresignedGetUrl(String fileName) throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.getPresignedGetUrl(fileName);
    }

    public String getPresignedGetUrl() throws Exception {
        if (cloudStorageStrategy == null) {
            throw new IllegalStateException("CloudStorageStrategy is not set");
        }
        return cloudStorageStrategy.getPresignedGetUrl();
    }


}