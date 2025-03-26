package com.application.repository.mongodb;

import com.application.model.mongo.UserImageMongoModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageMongoRepository extends MongoRepository<UserImageMongoModal, String> {

}
