package com.example.application.controller;

import com.cloudinary.Api;
import com.example.application.config.DotenvConfig;
import com.example.application.dto.ApiResponse;
import com.example.application.dto.ImageDto;
import com.example.application.model.image.ImageModal;
import com.example.application.service.*;
import com.example.application.util.CloudProvider;
import com.example.application.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ResourceLoader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/images")
public class ImageController {


    // IMPORTANT note : this is the static file path for upload images to the src
    // IMPORTANT note : this is for testing only
    private static final String PUBLIC_FOLDER = DotenvConfig.getEnv("PUBLIC_FOLDER_URL");
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    private static final ImageUtil imageUtil = new ImageUtil();
    // list of allow MIME type
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");
    private static final Double maxFileSize = 0.5;
    private final SaveFile saveFile = new SaveFile();
    private final ImageService imageService;
    private final String public_url_expire_in = DotenvConfig.getEnv("PUBLIC_CLOUD_URL_EXPIRE_IN");

    private final CloudStorageService cloudStorageService = new CloudStorageService(CloudProvider.AMAZON_S3);


    // * define a living time for the url that fetching image data from the s3 bucket
    // TODO : this parameter should be confined to match the system requirements
    // ? for now this will be 10 minutes
    private final Duration timeLivingOfFilUrl = Duration.ofMinutes(10);


    public ImageController(ResourceLoader resourceLoader, ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>("File empty", HttpStatus.BAD_REQUEST.value())
            );
        }

        // ! Check the MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(new ApiResponse<String>("File is not supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
        }
        try {

            // ! compress image file below than max file size
            byte[] fileAfterCompressed = imageService.compressImage(file, maxFileSize);
            System.out.println(fileAfterCompressed.length);
            // * init a imageModal instance to hold image's information
            ImageModal imageModal = new ImageModal();
            // * generate a unique file name using uuid
            imageModal.setFileName(UUID.randomUUID().toString())
                    .setFileSize((long) fileAfterCompressed.length)
                    .setExpireAt(LocalDateTime.now().plusSeconds(Integer.parseInt(this.public_url_expire_in)))
                    .setFormat("image/jpeg")
                    .setCloudProvider(CloudProvider.AMAZON_S3)
                    .setPublicUrl("");

            // IMPORTANT : upload it to cloud
            cloudStorageService.uploadFile(imageModal.getFileName(), fileAfterCompressed);

            imageModal.setPublicUrl(cloudStorageService.getPresignedGetUrl(imageModal.getFileName(), Duration.ofSeconds(Integer.parseInt(
                    this.public_url_expire_in
            ))));

            // IMPORTANT : save image information to database
            imageService.saveImage(imageModal);


            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<ImageDto>("Image update successfully", HttpStatus.OK.value(), new ImageDto(imageModal)));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ApiResponse<?>> getFileById(@PathVariable("fileName") String fileName) {

        try {
            Optional<ImageModal> optionalImage = imageService.getImageByFileName(fileName);

            if (optionalImage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Image not found", HttpStatus.NOT_FOUND.value()));
            }

            ImageModal image = optionalImage.get();

            // IMPORTANT : update the public url and expired time of url to the newest version
            image.setPublicUrl(cloudStorageService.getPresignedGetUrl(image.getFileName(), Duration.ofSeconds(Integer.parseInt(
                            this.public_url_expire_in
                    ))))
                    .setExpireAt(LocalDateTime.now().plusSeconds(Integer.parseInt(this.public_url_expire_in)));

            //  * save the modal to database
            imageService.updateImage(image.getId().toString(), image);

            // * response the image modal
            return ResponseEntity.ok(new ApiResponse<ImageDto>("Query successful", HttpStatus.OK.value(), new ImageDto(image)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

    }
}
