package com.application.ExternalService.GoogleSearch.SearchClient;

import com.application.ExternalService.GoogleSearch.dto.GoogleSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "googleSearchClient", url = "${google.search.url}")
public interface GoogleSearchClient {
    @GetMapping
    GoogleSearchResponse searchImages(
            @RequestParam("key") String apiKey,
            @RequestParam("cx") String cx,
            @RequestParam("q") String query,
            @RequestParam("searchType") String searchType,
            @RequestParam("start") Number start
    );

    @GetMapping
    GoogleSearchResponse searchImages(
            @RequestParam("key") String apiKey,
            @RequestParam("cx") String cx,
            @RequestParam("q") String query,
            @RequestParam("searchType") String searchType,
            @RequestParam("start") Number start,
            @RequestParam(value = "lr", required = false) String languageRestrict
    );

    @GetMapping
    GoogleSearchResponse searchImages(
            @RequestParam Map<String, String> allParams
    );

}
