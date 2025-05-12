package com.application.ExternalService.GoogleSearch.SearchService;

import com.application.ExternalService.GoogleSearch.SearchClient.GoogleSearchClient;
import com.application.ExternalService.GoogleSearch.dto.GoogleSearchImageResult;
import com.application.ExternalService.GoogleSearch.dto.GoogleSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoogleImageSearchService {

    @Autowired
    private final GoogleSearchClient googleSearchClient;

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.search.id}")
    private String cx;

    public GoogleImageSearchService(GoogleSearchClient googleSearchClient) {
        this.googleSearchClient = googleSearchClient;
    }

    public List<GoogleSearchImageResult> search(String query, Number start, String lr) {
        GoogleSearchResponse response = googleSearchClient.searchImages(apiKey, cx, query, "image", start, lr);
        if (response.getItems() == null) return List.of();
        return response.getItems();
    }

    public List<GoogleSearchImageResult> search(Map<String, String> allParams) {

        allParams.put("key", apiKey)
        ;
        allParams.put("cx", cx)
        ;
        allParams.put("searchType", "image");
        
        GoogleSearchResponse response = googleSearchClient.searchImages(allParams);
        if (response.getItems() == null) return List.of();
        return response.getItems();
    }
}
