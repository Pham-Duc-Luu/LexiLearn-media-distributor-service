package com.application.controller;

import com.application.ExternalService.GoogleSearch.SearchService.Upsplash.UpsplashSeachService;
import com.application.ExternalService.GoogleSearch.SearchService.google.GoogleImageSearchService;
import com.application.ExternalService.GoogleSearch.dto.Upsplash.UpsplashSeachPhotoResult;
import com.application.ExternalService.GoogleSearch.dto.google.GoogleSearchImageResult;
import com.application.dto.ImageDto;
import com.application.dto.QueryPaginationResult;
import com.application.dto.authentication.UserJWTObject;
import com.application.enums.ImageSize;
import com.application.exception.HttpBadRequestException;
import com.application.exception.HttpInternalServerErrorException;
import com.application.exception.HttpNotFoundException;
import com.application.exception.HttpUnauthorizedException;
import com.application.model.image.elasticesearch.Photo;
import com.application.model.mongo.UserImageMongoModal;
import com.application.repository.mongodb.ImageCollectionRepository;
import com.application.service.CloudStoreService.CloudStorageService;
import com.application.service.ImageService.ImagePropertyService;
import com.application.service.ImageService.PhotoService;
import com.application.service.ImageService.UserImageService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${apiPrefix}/images")
public class PhotoController {
    private static final String UPLOAD_DIR = "uploaded_images/";
    private static final int MAX_WIDTH = 1920; // desired width
    private static final int MAX_HEIGHT = 600; // desired height
    Logger logger = LogManager.getLogger(PhotoController.class);
    @Autowired
    private PhotoService photoService;
    @Autowired
    private ImagePropertyService imagePropertyService;
    @Autowired
    private UserImageService userImageService;
    @Autowired
    private Environment environment;


    @Autowired
    private CloudStorageService s3StorageStrategy;

    @Autowired
    private GoogleImageSearchService googleImageSearchService;

    @Autowired
    private UpsplashSeachService upsplashSeachService;

    @Autowired
    private ImageCollectionRepository imageCollectionRepository;

//    @Autowired
//    public PhotoController(PhotoService photoService, ImagePropertyService imagePropertyService, UserImageService userImageService) {
//        this.photoService = photoService;
//        this.imagePropertyService = imagePropertyService;
//        this.userImageService = userImageService;
//    }

    @GetMapping("/search")
    public ResponseEntity<QueryPaginationResult<List<ImageDto>>> searchPhoto(@RequestParam(name = "q") String query,
                                                                             @RequestParam(value = "lr", required = false) String languageRestrict,
                                                                             @RequestParam(name = "skip", defaultValue = "0") Integer skip,
                                                                             @RequestParam(name = "limit", defaultValue = "30") Integer limit
    ) throws IOException {

        // * prepare data
        QueryPaginationResult<List<Photo>> queryPaginationResult = new QueryPaginationResult();

        if (skip == null) {
            skip = 0;
        }
        if (limit == null || limit > 30) limit = 30;

        // * store result for later use
        List<ImageDto> imageDtoList = new ArrayList<>();

        // * using google sear api
        try {
            List<GoogleSearchImageResult> googleSearchImageResults = googleImageSearchService.search(query, 0, languageRestrict);
            googleSearchImageResults.forEach(item -> imageDtoList.add(item.mapToImageDto()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

        // * using upsplash search api
        try {
            List<UpsplashSeachPhotoResult> upsplashSeachPhotoResultList = upsplashSeachService.upsplashSearch(query).getResults();
            upsplashSeachPhotoResultList.forEach(item -> imageDtoList.add(item.mapToImage()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

        photoService.asyncSaveImageCollectionToMongo(imageDtoList);

        return ResponseEntity.ok(new QueryPaginationResult<List<ImageDto>>(imageDtoList.size(), limit, skip, imageDtoList));


    }

    @PostMapping("/private/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("image") MultipartFile file,
                                                @Valid UserJWTObject userJWTObject,
                                                @RequestParam(name = "image_size", required = false) ImageSize imageSize
    ) throws Exception {

        // Check if file is empty
        if (file.isEmpty()) {
            throw new HttpBadRequestException("No file uploaded.");
        }
        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException();
        }

        if (!ImagePropertyService.isImageFile(file)) throw new BadRequestException("Wrong file type");

        if (imageSize == null) imageSize = ImageSize.SD;


        // * resize the image
        byte[] resizedImage = ImagePropertyService.resizeImage(file, imageSize);
        // * generate uuid image url name : {user's uurd}.{random_uuid}

        // * get the pre-sign url
        UserImageMongoModal userImage = new UserImageMongoModal();
        userImage.setUserUUID(userJWTObject.getUser_uuid());
        userImage.setHeight(ImagePropertyService.getImageHeight(resizedImage));
        userImage.setFileSize(file.getSize());
        userImage.setWidth(ImagePropertyService.getImageWidth(resizedImage));

        // * save image information to elasticsearch
        UserImageMongoModal savedPhoto = userImageService.insertUserImage(userImage);

        // * upload to cloud
        // * Upload the file asynchronously and then fetch the presigned URL
        CompletableFuture<String> uploadFuture = s3StorageStrategy.uploadImageAsync(userImage.getFileName(), resizedImage)
                .thenApplyAsync(ignored -> {
                    try {
                        return s3StorageStrategy.getPresignedGetUrl();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate pre-signed URL", e);
                    }
                });


        // * Wait for the upload to complete and set the image URL
        try {
            savedPhoto.setPublicUrl(uploadFuture.get()); // Blocking call to ensure URL is set
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpInternalServerErrorException("Error processing upload");
        }

        return ResponseEntity.status(HttpStatus.OK).body(savedPhoto.mapToImageDto());
    }

    // TODO : create the presigned url for image in s3
    @GetMapping("")
    public ResponseEntity<ImageDto> getImage(
            @RequestParam(name = "image_id", required = false) String imageId,
            @RequestParam(name = "file_name", required = false) String filename,
            @RequestParam(name = "image_size", required = false) ImageSize imageSize,

            @Valid UserJWTObject userJWTObject) throws Exception {

        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException("You are not allow to query this resources");
        }
        if (imageId != null) {
            Optional<UserImageMongoModal> photo = userImageService.getUserImageById(imageId, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();

            return ResponseEntity.ok(photo.get().mapToImageDto());
        }
        if (filename != null) {
            Optional<UserImageMongoModal> photo = userImageService.getUserImageByFileName(filename, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) {
                UserImageMongoModal newPhoto = new UserImageMongoModal();
                newPhoto.setPublicUrl(s3StorageStrategy.getPresignedGetUrl(filename));
                return ResponseEntity.ok(newPhoto.mapToImageDto());
            } else {
                photo.get().setPublicUrl(s3StorageStrategy.getPresignedGetUrl(photo.get().getFileName()));

            }
            return ResponseEntity.ok(photo.get().mapToImageDto());
        }
        return null;
    }

}
