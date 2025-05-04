package com.application.service.AudioService;

import com.application.model.mongo.UserAudioMongoModal;
import com.application.repository.mongodb.UserAudioMongoRepository;
import jakarta.validation.Valid;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AudioService {
    private final Tika tika = new Tika();

    @Autowired
    private UserAudioMongoRepository userAudioMongoRepository;

    public boolean isAudioFile(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getInputStream());
        return mimeType.startsWith("audio/");
    }

    public UserAudioMongoModal insertUserAudio(@Valid UserAudioMongoModal userAudioMongoModal) {
        return userAudioMongoRepository.save(userAudioMongoModal);
    }

    public Optional<UserAudioMongoModal> getUserAudioById(String imageId, String userUUID) {
        return userAudioMongoRepository.findByIdAndUserUUID(imageId, userUUID);
    }

    public Optional<UserAudioMongoModal> getUserAudioByFileName(String filename, String userUUID) {
        return userAudioMongoRepository.findByFileNameAndUserUUID(filename, userUUID);
    }

}
