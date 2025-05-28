package com.application.ExternalService.GoogleSearch.SearchService.Upsplash;

import com.application.ExternalService.GoogleSearch.SearchClient.UpsplashSearchClient;
import com.application.ExternalService.GoogleSearch.dto.Upsplash.UpsplashSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UpsplashSeachService {
    @Autowired
    private UpsplashSearchClient upsplashSearchClient;

    @Value("${spring.application.upsplash.access-token}")
    private String clientId;

    public UpsplashSearchResponse upsplashSearch(String query) {
        return upsplashSearchClient.searchImages(query, clientId);
    }

    public UpsplashSearchResponse upsplashSearch(Map<String, String> allParams) {
        allParams.put("client_id", clientId);
        return upsplashSearchClient.searchImages(allParams);
    }
}
