package com.application.dto.upsplash.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PhotoDTO {
    private String id;

    @JsonProperty("created_at")
    private String createdAt;

    private int width;
    private int height;
    private String color;

    @JsonProperty("blur_hash")
    private String blurHash;

    private int likes;

    @JsonProperty("liked_by_user")
    private boolean likedByUser;

    private String description;
    private UserDTO user;

    @JsonProperty("current_user_collections")
    private List<Object> currentUserCollections;

    private UrlsDTO urls;
    private LinksDTO links;

    @Data
    public static class UserDTO {
        private String id;
        private String username;
        private String name;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("instagram_username")
        private String instagramUsername;

        @JsonProperty("twitter_username")
        private String twitterUsername;

        @JsonProperty("portfolio_url")
        private String portfolioUrl;

        @JsonProperty("profile_image")
        private ProfileImageDTO profileImage;

        private LinksDTO links;
    }

    @Data
    public static class ProfileImageDTO {
        private String small;
        private String medium;
        private String large;
    }

    @Data
    public static class LinksDTO {
        private String self;
        private String html;
        private String photos;
        private String likes;
    }

    @Data
    public static class UrlsDTO {
        private String raw;
        private String full;
        private String regular;
        private String small;
        private String thumb;
    }
}
