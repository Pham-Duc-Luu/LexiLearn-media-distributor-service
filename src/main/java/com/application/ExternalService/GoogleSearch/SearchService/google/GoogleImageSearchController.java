package com.application.ExternalService.GoogleSearch.SearchService.google;

import com.application.ExternalService.GoogleSearch.dto.google.GoogleSearchImageResult;
import com.application.dto.ImageDto;
import com.application.dto.QueryPaginationResult;
import com.application.model.image.elasticesearch.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "${apiPrefix}/testing/google")
public class GoogleImageSearchController {

    @Autowired
    private GoogleImageSearchService googleImageSearchService;

    @GetMapping("/search")
    public ResponseEntity<QueryPaginationResult<List<ImageDto>>> searchPhoto(@RequestParam Map<String, String> allParams) {
        List<Photo> photos = new ArrayList<>();

        // * using google sear api
        List<GoogleSearchImageResult> googleSearchImageResults = googleImageSearchService.search(allParams);


        return ResponseEntity.ok(new QueryPaginationResult<List<ImageDto>>(photos.size(), 0, 0, googleSearchImageResults.stream().map(item -> item.mapToImageDto()).toList()));

    }

}
