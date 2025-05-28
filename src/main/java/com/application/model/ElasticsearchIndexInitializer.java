package com.application.model;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import com.application.model.image.elasticesearch.Photo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchIndexInitializer {
    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchOperations elasticsearchOperations;
    Logger logger = LogManager.getLogger(ElasticsearchIndexInitializer.class);

    @PostConstruct
    public void createIndexIfNotExists() {
        try {
            IndexOperations indexOps = elasticsearchOperations.indexOps(Photo.getDefaultIndexCoordinates());
            if (!indexOps.exists()) {
                indexOps.create();             // create index
                indexOps.putMapping(indexOps.createMapping()); // apply mapping
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    @PostConstruct
    public void checkElasticsearchHealth() {
        try {
            HealthResponse health = elasticsearchClient.cluster().health();
            logger.info(health);
        } catch (Exception e) {
            logger.error(e);

        }
    }
}
