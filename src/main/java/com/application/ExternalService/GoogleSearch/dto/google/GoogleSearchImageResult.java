package com.application.ExternalService.GoogleSearch.dto.google;

import com.application.dto.ImageDto;
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

    public ImageDto mapToImageDto() {
        ImageDto dto = new ImageDto();

        dto.setTitle(this.title);
        dto.setUrl(this.link); // link chính là URL hình ảnh
        dto.setDescription(this.snippet);

        if (this.image != null) {
            dto.setWidth(image.getWidth() != null ? image.getWidth().intValue() : null);
            dto.setHeight(image.getHeight() != null ? image.getHeight().intValue() : null);
        }


        return dto;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageDetails {
        private String contextLink;
        private String thumbnailLink;
        private Number height;
        private Number width;
        private Number byteSize;
        private Number thumbnailHeight;
        private Number thumbnailWidth;
    }
}


