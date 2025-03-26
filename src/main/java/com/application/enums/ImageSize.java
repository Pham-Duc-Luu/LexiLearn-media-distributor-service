package com.application.enums;

public enum ImageSize {
    SM(240, 144, "LD"),   // Low Definition
    LD(320, 240, "LD"),   // Low Definition
    SD(720, 480, "SD"),   // Standard Definition
    HD(1280, 720, "HD"),  // High Definition
    FHD(1920, 1080, "FHD"); // Full High Definition

    private final int width;
    private final int height;
    private final String sizeName;

    // Constructor for the enum to store width and height
    ImageSize(int width, int height, String sizeName) {
        this.width = width;
        this.height = height;
        this.sizeName = sizeName;
    }


    public String getSizeName() {
        return sizeName;
    }

    // Getters for width and height
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVerticalWidth() {
        return height;
    }

    public int getVerticalHeight() {
        return width;
    }


    // Optional: Override toString() to display a more readable format
    @Override
    public String toString() {
        return name() + " (" + width + "x" + height + ")";
    }
}
