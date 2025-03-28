package com.application.model.image;

import com.application.util.CloudProvider;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "images")
public class ImageModal {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // Maps to SERIAL
//    @Column(name = "image_id")
    private Long id;
    //    @Column(name = "image_public_url", columnDefinition = "TEXT")
    private String publicUrl;
    //    @Column(name = "image_file_name", nullable = false, unique = true) // Maps to image_file_name
    private String fileName;
    //    @Column(name = "image_file_size") // Maps to image_file_size
    private Long fileSize;
    //    @Column(name = "image_format") // Maps to image_format
    private String format;
    //    @Column(name = "image_width") // Maps to image_width
    private Integer width;
    //    @Column(name = "image_height") // Maps to image_height
    private Integer height;
    //    @Column(name = "image_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Maps to image_created_at
    //    @Column(name = "image_expire_at")
    private LocalDateTime expireAt;

    //    @Column(name = "image_cloud_provider", nullable = false)
    private String cloudProvider = CloudProvider.AMAZON_S3.toString();

    public CloudProvider getCloudProvider() {
        return CloudProvider.valueOf(cloudProvider);
    }

    public ImageModal setCloudProvider(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider.toString();
        return this;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public ImageModal setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
        return this;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public ImageModal setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public ImageModal setFileName(String fileName) {
        this.fileName = fileName;
        return this;

    }

    public Long getFileSize() {
        return fileSize;
    }

    public ImageModal setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;

    }

    public String getFormat() {
        return format;
    }

    public ImageModal setFormat(String format) {
        this.format = format;
        return this;

    }

    public Integer getWidth() {
        return width;
    }

    public ImageModal setWidth(Integer width) {
        this.width = width;
        return this;

    }


    public Integer getHeight() {
        return height;
    }

    public ImageModal setHeight(Integer height) {
        this.height = height;
        return this;

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ImageModal setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public ImageModal setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
        return this;
    }
}