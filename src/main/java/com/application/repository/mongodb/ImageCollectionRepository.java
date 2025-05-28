package com.application.repository.mongodb;

import com.application.model.mongo.CollectingImageModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCollectionRepository extends MongoRepository<CollectingImageModal, String> {

}