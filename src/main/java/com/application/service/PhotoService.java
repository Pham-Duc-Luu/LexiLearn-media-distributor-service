package com.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.application.model.image.elasticesearch.Photo;
import com.application.repository.image.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private PhotoRepository photoRepository;

    public PhotoService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Photo> searchByText(String query) throws IOException {
        System.out.println(query);
        // Build the Elasticsearch query
        // Create fuzzy query for each field
        Query searchQuery = Query.of(q -> q
                .bool(b -> b
                        .should(sh -> sh
                                .fuzzy(f -> f
                                        .field("photo_description")
                                        .value(query)
                                        .fuzziness("AUTO") // automatic fuzziness calculation
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

        NativeQuery nativeQuery = NativeQuery.builder().withQuery(searchQuery)
                .build();

        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class);

        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    public List<Photo> searchByText(String query, Integer skip, Integer limit) throws IOException {
        System.out.println(query);
        // Build the Elasticsearch query
        // Create fuzzy query for each field
        Query searchQuery = Query.of(q -> q
                .bool(b -> b
                        .should(sh -> sh
                                .match(m -> m
                                        .field("photo_description")
                                        .query(query)
                                        .fuzziness("AUTO") // automatic fuzziness calculation
                                )
                        )
                        .should(sh -> sh
                                .match(m -> m
                                        .field("ai_description")
                                        .query(query)
                                        .fuzziness("AUTO")
                                )
                        )
                        .minimumShouldMatch("1")

                )

        );
        // Create Pageable object for pagination (0-based index)
        Pageable pageable = PageRequest.of(skip / limit, limit);  // Calculate page index based on skip and limit

        // Native query with pageable
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .build();

        SearchHits<Photo> searchHits = elasticsearchOperations.search(nativeQuery, Photo.class);

        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}
