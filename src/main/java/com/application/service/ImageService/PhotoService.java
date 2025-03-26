package com.application.service.ImageService;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.application.model.image.elasticesearch.Photo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing Photo data in Elasticsearch.
 * Provides methods to search, retrieve, and insert photos.
 */
@Service
public class PhotoService {
    private final ElasticsearchOperations elasticsearchOperations;
    Logger logger = LogManager.getLogger(PhotoService.class);

    /**
     * Constructor for PhotoService.
     *
     * @param elasticsearchOperations The ElasticsearchOperations instance for interacting with Elasticsearch.
     */
    public PhotoService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Retrieves a photo by its unique photo ID.
     *
     * @param photoId The unique identifier of the photo.
     * @return The found {@link Photo} object, or {@code null} if not found.
     */
    public Photo getPhotoByPhotoId(String photoId) {
        Query query = Query.of(q -> q
                .term(t -> t.field("photo_id").value(photoId))
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class);
        return searchHits.hasSearchHits() ? searchHits.getSearchHit(0).getContent() : null;
    }

    /**
     * Searches for photos using fuzzy text matching across multiple fields.
     *
     * @param query The search term.
     * @return A list of matching {@link Photo} objects.
     * @throws IOException If an error occurs while querying Elasticsearch.
     */
    public List<Photo> searchByText(String query) throws IOException {
        Query searchQuery = Query.of(q -> q
                .bool(b -> b
                        .should(sh -> sh
                                .fuzzy(f -> f
                                        .field("photo_description")
                                        .value(query)
                                        .fuzziness("AUTO")
                                )
                        )
                        .should(sh -> sh
                                .fuzzy(f -> f
                                        .field("ai_description")
                                        .value(query)
                                        .fuzziness("AUTO")
                                )
                        )
                        .should(sh -> sh
                                .fuzzy(f -> f
                                        .field("photo_location_name")
                                        .value(query)
                                        .fuzziness("AUTO")
                                )
                        )
                )
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(searchQuery)
                .build();

        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class);
        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new photo document into Elasticsearch.
     *
     * @param photo The {@link Photo} object to insert.
     * @return The saved {@link Photo} object.
     */
    public Photo insertNewPhoto(Photo photo) {
        return elasticsearchOperations.save(photo, photo.getIndexCoordinates());
    }

    /**
     * Searches for photos using text matching with pagination support.
     *
     * @param query The search term.
     * @param skip  The number of results to skip.
     * @param limit The maximum number of results to return.
     * @return A paginated list of matching {@link Photo} objects.
     * @throws IOException If an error occurs while querying Elasticsearch.
     */
    public List<Photo> searchByText(String query, Integer skip, Integer limit) throws IOException {
        logger.debug(query);
        Query searchQuery = Query.of(q -> q
                .bool(b -> b
                        .should(sh -> sh
                                .match(f -> f
                                        .query(query)
                                        .field("photo_description")
                                        .fuzziness("AUTO")
                                )
                        )
                        .should(sh -> sh
                                .match(f -> f
                                        .field("ai_description")
                                        .query(query)
                                        .fuzziness("AUTO")
                                )
                        )
                        .minimumShouldMatch("1")
                )
        );

        Pageable pageable = PageRequest.of(skip / limit, limit);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .build();
        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class, Photo.getDefaultIndexCoordinates());
        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}
