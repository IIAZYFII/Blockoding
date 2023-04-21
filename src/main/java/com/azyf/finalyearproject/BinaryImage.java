/**
 * This class represents a byte representation of an image.
 *
 * @author  Hussain Asif
 * @v+ersion 1.0
 */

package com.azyf.finalyearproject;
public class BinaryImage {
    private int width;
    private int height;
    private String imageContent;

    /**
     * Creates a binary image with a specified width, height and image content.
     * @param width        The width of the image.
     * @param height       The height of the image.
     * @param imageContent The actual image as a String.
     */
    public BinaryImage(int width, int height, String imageContent) {
        this.width = width;
        this.height = height;
        this.imageContent = imageContent;
    }

    /**
     * Gets the content of the binary image.
     * @return The content of the binary image.
     */
    public String getImageContent() {
        return imageContent;
    }

    /**
     * Converts the binary image in a readable String format.
     * @return The binary image in a String format.
     */
    @Override
    public String toString() {
        return "BinaryImage{" +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

}
