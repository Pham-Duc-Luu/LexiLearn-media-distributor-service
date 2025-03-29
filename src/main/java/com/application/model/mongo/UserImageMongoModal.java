package com.application.model.mongo;

import com.application.util.CloudProvider;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_images")
public class UserImageMongoModal {
    @Id
    private String id; // Auto-generated by MongoDB
    @NotNull
    private String userUUID;
    private String publicUrl;
    @NotNull
    private String fileName;
    private Long fileSize;
    private String format;
    private Integer width;
    private Integer height;
    private LocalDateTime createdAt = LocalDateTime.now(); // Maps to image_created_at
    private LocalDateTime expireAt;
    private String cloudProvider = CloudProvider.AMAZON_S3.toString();

    public UserImageMongoModal() {
    }

    public UserImageMongoModal(String userUUID, String fileName, Integer width, Integer height) {
        this.userUUID = userUUID;
        this.fileName = fileName;
        this.width = width;
        this.height = height;
    }

    public UserImageMongoModal(String userUUID, String publicUrl, String fileName, Long fileSize, String format, Integer width, Integer height, LocalDateTime createdAt, LocalDateTime expireAt) {
        this.userUUID = userUUID;
        this.publicUrl = publicUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.format = format;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public UserImageMongoModal(String userUUID, String publicUrl, String fileName, Long fileSize, String format, Integer width, Integer height, LocalDateTime createdAt, LocalDateTime expireAt, String cloudProvider) {
        this.userUUID = userUUID;
        this.publicUrl = publicUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.format = format;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
        this.cloudProvider = cloudProvider;
    }

    public String getCloudProvider() {
        return cloudProvider;
    }

    public void setCloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    public UserImageMongoModal setCloudProvider(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider.toString();
        return this;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public UserImageMongoModal setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
        return this;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public UserImageMongoModal setId(String id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public UserImageMongoModal setFileName(String fileName) {
        this.fileName = fileName;
        return this;

    }

    public Long getFileSize() {
        return fileSize;
    }

    public UserImageMongoModal setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;

    }

    public String getFormat() {
        return format;
    }

    public UserImageMongoModal setFormat(String format) {
        this.format = format;
        return this;

    }

    public Integer getWidth() {
        return width;
    }

    public UserImageMongoModal setWidth(Integer width) {
        this.width = width;
        return this;

    }


    public Integer getHeight() {
        return height;
    }

    public UserImageMongoModal setHeight(Integer height) {
        this.height = height;
        return this;

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserImageMongoModal setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public UserImageMongoModal setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
        return this;
    }
}
