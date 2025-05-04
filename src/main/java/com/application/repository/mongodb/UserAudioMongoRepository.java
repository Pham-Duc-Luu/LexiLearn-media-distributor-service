package com.application.repository.mongodb;

import com.application.model.mongo.UserAudioMongoModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAudioMongoRepository extends MongoRepository<UserAudioMongoModal, String> {
    Optional<UserAudioMongoModal> findByIdAndUserUUID(String id, String userUUID);

    Optional<UserAudioMongoModal> findByFileNameAndUserUUID(String filename, String userUUID);

}