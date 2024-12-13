package com.example.application.Strategy.CloudStorageStrategy;

import com.example.application.service.S3Service;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class S3StorageStrategy implements CloudStorageStrategy {
    // Inject S3 client (e.g., AmazonS3 or S3AsyncClient)
    private final S3Service s3Service = new S3Service();
    private final Duration duration = Duration.ofHours(1);

    @Override
    public void uploadFile(String fileName, byte[] fileData) throws Exception {
        // Logic to upload file to S3
        s3Service.uploadFile(fileName, fileData);
    }

    @Override
    public String getPresignedGetUrl(Duration duration) throws Exception {
        return s3Service.getPresignedGetUrl(duration);
    }

    @Override
    public String getPresignedGetUrl() throws Exception {
        return s3Service.getPresignedGetUrl(this.duration);
    }

    @Override
    public String getPresignedGetUrl(String filename, Duration duration) throws Exception {
        return s3Service.getPresignedGetUrl(filename, duration);
    }

}
