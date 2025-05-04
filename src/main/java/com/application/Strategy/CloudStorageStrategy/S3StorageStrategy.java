package com.application.Strategy.CloudStorageStrategy;

import com.application.service.CloudStoreService.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class S3StorageStrategy implements CloudStorageStrategy {
    // Inject S3 client (e.g., AmazonS3 or S3AsyncClient)
    
    @Autowired
    private S3Service s3Service;


    @Value("${spring.application.s3.presignurl.duration}")
    private Integer duration;

    @Override
    public void uploadFile(String fileName, byte[] fileData, String contentType) throws Exception {
        // Logic to upload file to S3
        s3Service.uploadFile(fileName, fileData, contentType);
    }

    @Override
    public String getPresignedGetUrl(String fileName) throws Exception {

        return s3Service.getPresignedGetUrl(fileName, Duration.ofHours(duration));
    }


    @Override
    public String getPresignedGetUrl(Duration duration) throws Exception {
        return s3Service.getPresignedGetUrl(duration);
    }


    @Override
    public String getPresignedGetUrl() throws Exception {
        return s3Service.getPresignedGetUrl(Duration.ofHours(duration));
    }

    @Override
    public String getPresignedGetUrl(String filename, Duration duration) throws Exception {
        return s3Service.getPresignedGetUrl(filename, duration);
    }

}
