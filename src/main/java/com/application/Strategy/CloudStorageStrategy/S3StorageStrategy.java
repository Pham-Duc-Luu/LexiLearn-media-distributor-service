package com.application.Strategy.CloudStorageStrategy;

import com.application.config.DotenvConfig;
import com.application.service.CloudStoreService.S3Service;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("s3CloudStorage")  // This name will be used for dependency injection
public class S3StorageStrategy implements CloudStorageStrategy {
    // Inject S3 client (e.g., AmazonS3 or S3AsyncClient)
    private final S3Service s3Service = new S3Service();

    private final Duration duration = Duration.ofHours(Long.parseLong(DotenvConfig.getS3PresignedUrlDuration()));

    @Override
    public void uploadFile(String fileName, byte[] fileData) throws Exception {
        // Logic to upload file to S3
        s3Service.uploadFile(fileName, fileData);
    }

    @Override
    public String getPresignedGetUrl(String fileName) throws Exception {
        return s3Service.getPresignedGetUrl(fileName, duration);
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
