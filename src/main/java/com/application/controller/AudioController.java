package com.application.controller;

import com.application.dto.authentication.UserJWTObject;
import com.application.exception.HttpNotFoundException;
import com.application.exception.HttpUnauthorizedException;
import com.application.model.mongo.UserAudioMongoModal;
import com.application.service.AudioService.AudioService;
import com.application.service.CloudStoreService.CloudStorageService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${apiPrefix}/audio")
public class AudioController {

    long maxSize = 5 * 1024 * 1024; // 5 MB limit
    @Autowired
    private AudioService audioService;

    @Autowired
    private CloudStorageService s3StorageStrategy;

    @PostMapping("/private/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("audio") MultipartFile file,
                                         @Valid UserJWTObject userJWTObject
    ) throws Exception {
        // Check if file is empty
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }
        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException();
        }

        if (!audioService.isAudioFile(file)) {
            throw new BadRequestException("Wrong file type");
        }

        if (file.getSize() > maxSize) {
            throw new BadRequestException("File size exceeds the limit of 5MB");
        }

        // * generate audio name
        String audioFileName = "audio_" + UUID.randomUUID();
        // * upload to cloud
        // * Upload the file asynchronously and then fetch the presigned URL
        CompletableFuture<String> uploadFuture = s3StorageStrategy.uploadAudioAsync(audioFileName, file.getBytes())
                .thenApplyAsync(ignored -> {
                    try {
                        return s3StorageStrategy.getPresignedGetUrl();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate pre-signed URL", e);
                    }
                });

        // * get the pre-sign url

        UserAudioMongoModal userAudioMongoModal = new UserAudioMongoModal();
        userAudioMongoModal.setFileName(audioFileName);
        userAudioMongoModal.setUserUUID(userJWTObject.getUser_uuid());

        // * save audio infor to database
        UserAudioMongoModal savedAudio = audioService.insertUserAudio(userAudioMongoModal);

        // * Wait for the upload to complete and set the image URL
        try {
            savedAudio.setPublicUrl(uploadFuture.get()); // Blocking call to ensure URL is set
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing upload");
        }

        return ResponseEntity.status(HttpStatus.OK).body(savedAudio);
    }

    // TODO : create the presigned url for image in s3
    @GetMapping("")
    public ResponseEntity<?> getImage(
            @RequestParam(name = "audio_id", required = false) String audioId,
            @RequestParam(name = "file_name", required = false) String filename,
            @Valid UserJWTObject userJWTObject) throws Exception {

        if (userJWTObject.getUser_uuid() == null) {
            throw new HttpUnauthorizedException("You are not allow to query this resources");
        }
        if (audioId != null) {
            Optional<UserAudioMongoModal> photo = audioService.getUserAudioById(audioId, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();
            photo.get().setPublicUrl(s3StorageStrategy.getPresignedGetUrl(photo.get().getFileName()));
            return ResponseEntity.ok(photo);
        }
        if (filename != null) {
            Optional<UserAudioMongoModal> photo = audioService.getUserAudioByFileName(filename, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();
            photo.get().setPublicUrl(s3StorageStrategy.getPresignedGetUrl(photo.get().getFileName()));
            return ResponseEntity.ok(photo);
        }
        return null;
    }
}
