package com.application.ExternalService.GoogleSearch.dto.Upsplash;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpsplashSearchResponse {
    private Integer total;
    private Integer total_pages;
    private List<UpsplashSeachPhotoResult> results;

}
