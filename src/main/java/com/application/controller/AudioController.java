package com.application.controller;

import com.application.dto.AudioDto;
import com.application.dto.authentication.UserJWTObject;
import com.application.exception.HttpInternalServerErrorException;
import com.application.exception.HttpNotFoundException;
import com.application.exception.HttpUnauthorizedException;
import com.application.model.mongo.UserAudioMongoModal;
import com.application.service.AudioService.AudioService;
import com.application.service.CloudStoreService.CloudStorageService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
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
    public ResponseEntity<AudioDto> uploadImage(@RequestParam("audio") MultipartFile file,
                                                @Valid UserJWTObject userJWTObject
    ) throws Exception {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new BadRequestException("No file found");
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

        // Read duration
        // Extract audio duration using Apache Tika
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();
        try (InputStream input = file.getInputStream()) {
            parser.parse(input, handler, metadata, context);
        }

        String duration = metadata.get("xmpDM:duration"); // in milliseconds
        long durationInMillis = duration != null ? (long) Double.parseDouble(duration) : 0;
        long durationInSeconds = durationInMillis / 1000;

        // * get the pre-sign url
        UserAudioMongoModal userAudioMongoModal = new UserAudioMongoModal();
        userAudioMongoModal.setUserUUID(userJWTObject.getUser_uuid());
        userAudioMongoModal.setLengthInSecond(durationInSeconds);
        userAudioMongoModal.setFileSize(file.getSize());

        // * save audio infor to database
        UserAudioMongoModal savedAudio = audioService.insertUserAudio(userAudioMongoModal);

        // * upload to cloud
        // * Upload the file asynchronously and then fetch the presigned URL
        CompletableFuture<String> uploadFuture = s3StorageStrategy.uploadAudioAsync(userAudioMongoModal.getFileName(), file.getBytes())
                .thenApplyAsync(ignored -> {
                    try {
                        return s3StorageStrategy.getPresignedGetUrl();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate pre-signed URL", e);
                    }
                });


        // * Wait for the upload to complete and set the image URL
        try {
            savedAudio.setPublicUrl(uploadFuture.get()); // Blocking call to ensure URL is set
        } catch (Exception e) {
            throw new HttpInternalServerErrorException("Error processing upload");
        }
        return ResponseEntity.status(HttpStatus.OK).body(savedAudio.mapToAudioDto());
    }

    // TODO : create the presigned url for image in s3
    @GetMapping("")
    public ResponseEntity<AudioDto> getAudio(
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
            return ResponseEntity.ok(photo.get().mapToAudioDto());
        }
        if (filename != null) {
            Optional<UserAudioMongoModal> photo = audioService.getUserAudioByFileName(filename, userJWTObject.getUser_uuid());
            if (photo.isEmpty()) throw new HttpNotFoundException();
            photo.get().setPublicUrl(s3StorageStrategy.getPresignedGetUrl(photo.get().getFileName()));
            return ResponseEntity.ok(photo.get().mapToAudioDto());
        }
        throw new HttpInternalServerErrorException();
    }
}
