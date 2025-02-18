package com.application.enums;

public enum ImageSize {
    SD(720, 480),   // Standard Definition
    HD(1280, 720),  // High Definition
    FHD(1920, 1080); // Full High Definition

    private final int width;
    private final int height;

    // Constructor for the enum to store width and height
    ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Getters for width and height
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Optional: Override toString() to display a more readable format
    @Override
    public String toString() {
        return name() + " (" + width + "x" + height + ")";
    }
}
