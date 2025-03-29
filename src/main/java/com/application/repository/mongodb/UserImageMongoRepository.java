package com.application.repository.mongodb;

import com.application.model.mongo.UserImageMongoModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageMongoRepository extends MongoRepository<UserImageMongoModal, String> {
    Optional<UserImageMongoModal> findByIdAndUserUUID(String id, String userUUID);

    Optional<UserImageMongoModal> findByFileNameAndUserUUID(String filename, String userUUID);

}
