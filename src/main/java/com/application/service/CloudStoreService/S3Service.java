package com.application.service.CloudStoreService;

import com.application.exception.HttpInternalServerErrorException;
import com.application.exception.HttpResponseException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;

@Service
public class S3Service {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private S3Client s3Client;
    @Autowired
    private Environment environment;
    @Value("${spring.application.s3.bucket-name}")
    private String bucketname;
    @Value("${spring.application.s3.bucket-region}")
    private Region region;
    @Value("${spring.application.s3.bucket-key}")
    private String key;
    @Value("${spring.application.s3.bucket-secret}")
    private String secret;
    private String PresignedGetUrl;
    private String keyName;

    @PostConstruct
    private void init() {
        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(key, secret))
                ).build();
    }

    // Helper method to perform the actual S3 upload
    private void uploadToS3(String bucketName, String keyName, byte[] fileData, String contentType) throws HttpResponseException {
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .contentType(contentType)  // Specify the content type as JPEG
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, fileData.length));

            System.out.println("File uploaded to S3 successfully.");
        } catch (S3Exception e) {
            logger.error(e.awsErrorDetails().toString());
            throw new HttpInternalServerErrorException(e.awsErrorDetails().toString());
        } catch (Exception e) {
            logger.error(
                    e.toString()
            );
            throw new HttpInternalServerErrorException(e.toString());

        }
    }

    // Asynchronously uploads a file to S3 with the specified key
    @Async
    public void uploadFile(String keyName, byte[] fileData, String contentType) throws HttpResponseException {
        this.keyName = keyName;
        uploadToS3(bucketname, keyName, fileData, contentType);

    }

    // Overloaded method to allow uploading to a specified bucket
    @Async
    public void uploadFile(String bucketName, String keyName, byte[] fileData, String contentType) throws HttpResponseException {
        this.keyName = keyName;
        uploadToS3(bucketName, keyName, fileData, contentType);
    }


    /* Create a pre-signed URL to download an object in a subsequent GET request. */
    public String getPresignedGetUrl(String bucketName, String keyName, Duration duration) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(key, secret)))
                .build()
        ) {


            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration)  // The URL will expire in 10 minutes.
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

            return presignedGetObjectRequest.url().toString();

        }
    }

    public String getPresignedGetUrl(String keyName, Duration duration) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(key, secret)))
                .build()
        ) {


            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(this.bucketname)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration)  // The URL will expire in 10 minutes.
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

            return presignedGetObjectRequest.url().toString();

        }
    }


    /* Create a pre-signed URL to download an object in a subsequent GET request. */
    public String getPresignedGetUrl(Duration duration) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(key, secret)))
                .build()
        ) {


            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(this.bucketname)
                    .key(this.keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration)  // The URL will expire in 10 minutes.
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

            return presignedGetObjectRequest.url().toString();

        }
    }


}
