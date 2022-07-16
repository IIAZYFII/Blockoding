package com.azyf.finalyearproject;

public class BinaryImage {
    private int width;
    private int height;
    private String imageContent;

    public BinaryImage(int width, int height, String imageContent) {
        this.width = width;
        this.height = height;
        this.imageContent = imageContent;
    }

    public String getImageContent() {
        return imageContent;
    }
    @Override
    public String toString() {
        return "BinaryImage{" +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

}
