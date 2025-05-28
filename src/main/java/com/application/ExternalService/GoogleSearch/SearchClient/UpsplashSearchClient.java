package com.application.ExternalService.GoogleSearch.SearchClient;

import com.application.ExternalService.GoogleSearch.dto.Upsplash.UpsplashSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "upsplashSeachClient", url = "${spring.application.upsplash.url}")
public interface UpsplashSearchClient {
    @GetMapping("/search/photos")
    UpsplashSearchResponse searchImages(
            @RequestParam("query") String query,
            @RequestParam("client_id") String clientId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "per_page", required = false) Integer perPage,
            @RequestParam(value = "order_by", required = false) String orderBy,
            @RequestParam(value = "collections", required = false) String collections,
            @RequestParam(value = "content_filter", required = false) String contentFilter,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "orientation", required = false) String orientation
    );

    @GetMapping("/search/photos")
    UpsplashSearchResponse searchImages(
            @RequestParam("query") String query,
            @RequestParam("client_id") String clientId
    );

    @GetMapping("/search/photos")
    UpsplashSearchResponse searchImages(
            @RequestParam Map<String, String> allParams
    );

}
