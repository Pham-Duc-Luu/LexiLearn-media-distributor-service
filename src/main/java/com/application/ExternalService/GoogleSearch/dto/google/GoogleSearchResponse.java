package com.application.ExternalService.GoogleSearch.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleSearchResponse {
    private List<GoogleSearchImageResult> items;
}
