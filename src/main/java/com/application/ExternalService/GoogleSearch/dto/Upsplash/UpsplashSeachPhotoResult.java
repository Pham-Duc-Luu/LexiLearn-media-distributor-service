package com.application.ExternalService.GoogleSearch.dto.Upsplash;

import com.application.dto.ImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UpsplashSeachPhotoResult {
    private String id;
    private String slug;
    private Map<String, String> alternative_slugs;
    private String description;
    private String alt_description;
    private Map<String, String> urls;

    public ImageDto mapToImage() {
        ImageDto imageDto = new ImageDto();
        imageDto.setUrl(urls != null ? urls.getOrDefault("small", null) : null);
        imageDto.setDescription(description != null ? description : alt_description);
        imageDto.setTitle(slug != null ? slug.replace("-", " ") : null);
        imageDto.setWidth(null); // Unsplash search không trả về width/height trong search response
        imageDto.setHeight(null);

        return imageDto;
    }
}
