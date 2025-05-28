package com.application.ExternalService.GoogleSearch.SearchService.Upsplash;

import com.application.ExternalService.GoogleSearch.dto.Upsplash.UpsplashSearchResponse;
import com.application.dto.ImageDto;
import com.application.dto.QueryPaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "${apiPrefix}/testing/upsplash")
public class UpsplashSearchController {

    @Autowired
    private UpsplashSeachService upsplashSeachService;

    @GetMapping("/search")
    public ResponseEntity<QueryPaginationResult<List<ImageDto>>> searchPhoto(@RequestParam Map<String, String> allParams) {

        UpsplashSearchResponse upsplashSearchResponse = upsplashSeachService.upsplashSearch(allParams);

        return ResponseEntity.ok(new QueryPaginationResult<List<ImageDto>>(upsplashSearchResponse.getTotal(), 0, 0, upsplashSearchResponse.getResults().stream().map(item -> item.mapToImage()).toList()));

    }

}
