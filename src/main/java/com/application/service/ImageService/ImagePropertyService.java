package com.application.service.ImageService;

import com.application.enums.ImageSize;
import com.application.exception.HttpInternalServerErrorException;
import com.application.exception.HttpResponseException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImagePropertyService {

    public static Integer getImageHeight(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Invalid image file.");
            }
            return image.getHeight();
        }
    }

    public static Integer getImageHeight(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        return image.getHeight();
    }

    public static Integer getImageWidth(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        return image.getWidth();
    }

    public static Integer getImageWidth(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Invalid image file.");
            }
            return image.getWidth();
        }
    }

    public static boolean isImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static byte[] resizeImage(MultipartFile file, ImageSize imageSize) throws HttpResponseException {
        try (InputStream inputStream = file.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            if (getImageWidth(file) > getImageHeight(file)) {
                // Horizontal
                // Resize the image using Thumbnailator
                Thumbnails.of(inputStream)
                        .size(imageSize.getWidth(), imageSize.getHeight()) // Set target dimensions
                        .outputFormat("jpg") // Ensure output format
                        .toOutputStream(outputStream);
            } else {
                // Vertical
                // Resize the image using Thumbnailator
                Thumbnails.of(inputStream)
                        .size(imageSize.getHeight(), imageSize.getWidth()) // Set target dimensions
                        .outputFormat("jpg") // Ensure output format
                        .toOutputStream(outputStream);
            }
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpInternalServerErrorException();
        }
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
