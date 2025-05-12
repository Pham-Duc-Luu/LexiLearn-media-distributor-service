package com.application.ExternalService.GoogleSearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleSearchImageResult {

    private String title;
    private String link;
    private String snippet;
    private String mime;

    private ImageDetails image;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageDetails {
        private String contextLink;
        private String thumbnailLink;
        private int height;
        private int width;
        private int byteSize;
        private int thumbnailHeight;
        private int thumbnailWidth;
    }
}


