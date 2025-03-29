package com.application.service.ImageService;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.application.model.image.elasticesearch.Photo;
import com.application.model.image.elasticesearch.PhotoDocument;
import com.application.model.mongo.UserImageMongoModal;
import com.application.repository.mongodb.UserImageMongoRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImageService {
    private final ElasticsearchOperations elasticsearchOperations;
    Logger logger = LogManager.getLogger(UserImageService.class);
    @Autowired
    private UserImageMongoRepository userImageMongoRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public UserImageService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Retrieves a photo by its unique photo ID.
     *
     * @param photoId The unique identifier of the photo.
     * @return The found {@link Photo} object, or {@code null} if not found.
     */
    @Deprecated
    public Photo getPhotoByPhotoId(String photoId) {
        Query query = Query.of(q -> q
                .match(m -> m.query(photoId).field("photo_id"))
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class, Photo.getIndexCoordinates(PhotoDocument.USER_IMAGE));
        return searchHits.hasSearchHits() ? searchHits.getSearchHit(0).getContent() : null;
    }

    public UserImageMongoModal insertUserImage(@Valid UserImageMongoModal userImageMongoModal) {
        return userImageMongoRepository.save(userImageMongoModal);
    }

    public Optional<UserImageMongoModal> getUserImageById(String imageId, String userUUID) {
        return userImageMongoRepository.findByIdAndUserUUID(imageId, userUUID);
    }

    public Optional<UserImageMongoModal> getUserImageByFileName(String filename, String userUUID) {
        return userImageMongoRepository.findByFileNameAndUserUUID(filename, userUUID);
    }

}
