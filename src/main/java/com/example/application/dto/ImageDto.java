package com.example.application.dto;

import com.example.application.model.image.ImageModal;

public class ImageDto extends ImageModal {

    public ImageDto(ImageModal imageModal) {
        super(); // Call the superclass constructor
        this.setId(imageModal.getId());
        this.setFileName(imageModal.getFileName());
        this.setFileSize(imageModal.getFileSize());
        this.setFormat(imageModal.getFormat());
        this.setWidth(imageModal.getWidth());
        this.setHeight(imageModal.getHeight());
        this.setCreatedAt(imageModal.getCreatedAt());
        this.setPublicUrl(imageModal.getPublicUrl());
        this.setExpireAt(imageModal.getExpireAt());
    }


}