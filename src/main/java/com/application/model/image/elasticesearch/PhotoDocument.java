package com.application.model.image.elasticesearch;

public enum PhotoDocument {
    PHOTOS("photos"),
    USER_IMAGE("user_images");

    private final String value;

    PhotoDocument(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}