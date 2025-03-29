package com.application.controller;

import com.application.dto.QueryPaginationResult;
import com.application.dto.authentication.UserJWTObject;
import com.application.enums.ImageSize;
import com.application.exception.HttpNotFoundException;
import com.application.exception.HttpUnauthorizedException;
import com.application.model.image.elasticesearch.Photo;
import com.application.model.mongo.UserImageMongoModal;
import com.application.service.CloudStoreService.CloudStorageService;
import com.application.service.ImageService.ImagePropertyService;
import com.application.service.ImageService.ImageService;
import com.application.service.ImageService.PhotoService;
import com.application.service.ImageService.UserImageService;
import com.application.util.CloudProvider;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${apiPrefix}/images")
public class PhotoController {
    private static final String UPLOAD_DIR = "uploaded_images/";
    private static final int MAX_WIDTH = 1920; // desired width
    private static final int MAX_HEIGHT = 600; // desired height
    private final PhotoService photoService;
    private final ImagePropertyService imagePropertyService;
    private final UserImageService userImageService;
    Logger logger = LogManager.getLogger(PhotoController.class);

    @Autowired
    public PhotoController(PhotoService photoService, ImageService imageService, ImagePropertyService imagePropertyService, UserImageService userImageService) {
        this.photoService = photoService;
        this.imagePropertyService = imagePropertyService;
        this.userImageService = userImageService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPhoto(@RequestParam(name = "q") String query, @RequestParam(name = "skip", defaultValue = "0") Integer skip, @RequestParam(name = "limit", defaultValue = "30") Integer limit) {
        try {

            // * prepare data
            QueryPaginationResult<List<Photo>> queryPaginationResult = new QueryPaginationResult();

            if (skip == null) {
                skip = 0;
            }
            if (limit == null || limit > 30) limit = 30;


            List<Photo> photos = photoService.searchByText(query, skip, limit);
            return ResponseEntity.ok(new QueryPaginationResult<List<Photo>>(photos.size(), limit, skip, photos));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error fetching photos");
        }
    }

    @PostMapping("/private/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                         @Valid UserJWTObject userJWTObject,
                                         @RequestParam(name = "image_size", required = false) ImageSize imageSize
    ) throws Exception {

        // Check if file is empty
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }
        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException();
        }

        if (!ImagePropertyService.isImageFile(file)) throw new BadRequestException("Wrong file type");

        if (imageSize == null) imageSize = ImageSize.SD;

        // * resize the image
        byte[] resizedImage = ImagePropertyService.resizeImage(file, imageSize);
        // * generate uuid image url name
        String imageFileName = UUID.randomUUID().toString();

        // * upload to cloud
        CloudStorageService s3StorageStrategy = new CloudStorageService(CloudProvider.AMAZON_S3);
        // * Upload the file asynchronously and then fetch the presigned URL
        CompletableFuture<String> uploadFuture = s3StorageStrategy.uploadFileAsync(imageFileName, resizedImage)
                .thenApplyAsync(ignored -> {
                    try {
                        return s3StorageStrategy.getPresignedGetUrl();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate pre-signed URL", e);
                    }
                });

        // * get the pre-sign url
        UserImageMongoModal userImage = new UserImageMongoModal();
        userImage.setFileName(imageFileName);
        userImage.setUserUUID(userJWTObject.getUser_uuid());
        userImage.setHeight(ImagePropertyService.getImageHeight(resizedImage));
        userImage.setWidth(ImagePropertyService.getImageWidth(resizedImage));

        // * save image information to elasticsearch
        UserImageMongoModal savedPhoto = userImageService.insertUserImage(userImage);

        // * Wait for the upload to complete and set the image URL
        try {
            savedPhoto.setPublicUrl(uploadFuture.get()); // Blocking call to ensure URL is set
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing upload");
        }

        return ResponseEntity.status(HttpStatus.OK).body(savedPhoto);
    }

    // TODO : create the presigned url for image in s3
    @GetMapping("")
    public ResponseEntity<?> getImage(
            @RequestParam(name = "image_id", required = false) String imageId,
            @RequestParam(name = "file_name", required = false) String filename,
            @RequestParam(name = "image_size", required = false) ImageSize imageSize,
            @Valid UserJWTObject userJWTObject) throws Exception {

        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException("You are not allow to query this resources");
        }
        if (imageId != null) {
            CloudStorageService cloudStorageService = new CloudStorageService(CloudProvider.AMAZON_S3);
            Optional<UserImageMongoModal> photo = userImageService.getUserImageById(imageId, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();
            photo.get().setPublicUrl(cloudStorageService.getPresignedGetUrl(photo.get().getFileName()));
            return ResponseEntity.ok(photo);
        }
        if (filename != null) {
            CloudStorageService cloudStorageService = new CloudStorageService(CloudProvider.AMAZON_S3);
            Optional<UserImageMongoModal> photo = userImageService.getUserImageByFileName(filename, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();
            photo.get().setPublicUrl(cloudStorageService.getPresignedGetUrl(photo.get().getFileName()));
            return ResponseEntity.ok(photo);
        }
        return null;
    }

}
