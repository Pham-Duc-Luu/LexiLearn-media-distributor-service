package com.example.application.service;

import com.example.application.model.image.ImageModal;
import com.example.application.repository.image.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void saveImage(ImageModal imageModal) {
        imageRepository.save(imageModal);
    }

    public Optional<ImageModal> getImageById(String id) {
        return imageRepository.findById(Integer.parseInt(id));
    }

    public Optional<ImageModal> getImageByFileName(String fileName) {
        return imageRepository.findByFileName(fileName);
    }

    public ImageModal updateImage(String id, ImageModal updatedImageModal) {
        return imageRepository.findById(Integer.parseInt(id)).map(existingImage -> {
            // Update fields as needed
            if (updatedImageModal.getFileName() != null) {
                existingImage.setFileName(updatedImageModal.getFileName());
            }
            if (updatedImageModal.getFileSize() != null) {
                existingImage.setFileSize(updatedImageModal.getFileSize());
            }
            if (updatedImageModal.getFormat() != null) {
                existingImage.setFormat(updatedImageModal.getFormat());
            }
            if (updatedImageModal.getPublicUrl() != null) {
                existingImage.setPublicUrl(updatedImageModal.getPublicUrl());
            }
            if (updatedImageModal.getExpireAt() != null) {
                existingImage.setExpireAt(updatedImageModal.getExpireAt());
            }
            if (updatedImageModal.getWidth() != null) {
                existingImage.setWidth(updatedImageModal.getWidth());
            }
            if (updatedImageModal.getHeight() != null) {
                existingImage.setHeight(updatedImageModal.getHeight());
            }
            // Save updated entity
            return imageRepository.save(existingImage);
        }).orElseThrow(() -> new IllegalArgumentException("Image with ID " + id + " not found"));
    }

    public byte[] compressImage(MultipartFile file, double maxFileSizeMB) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        double quality = 1.0; // Start with maximum quality
        // Loop to compress until file size is below maxFileSizeMB
        while (true) {
            outputStream.reset();
            Thumbnails.of(file.getInputStream())
                    .scale(0.1) // Maintain original dimensions
                    .outputQuality(quality) // Adjust quality
                    .toOutputStream(outputStream);
            if (outputStream.toByteArray().length <= maxFileSizeMB * (1000 * 1024)) break;

            if (quality < 0.1) break;

            quality -= 0.005;

        }

        return outputStream.toByteArray();
    }
}
