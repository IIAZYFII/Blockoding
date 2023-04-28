/**
 * This class represents the background of the scene.
 * @author Hussain Asif
 * @version 1.0
 */
package com.azyf.finalyearproject;

import javafx.scene.image.Image;

public class Scene {
    private Image image;
    private String name;


    /**
     * The constructor for the scene.
      * @param image The image of the scene.
     * @param name The name of the scene.
     */
    public Scene(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    /**
     * Constructor for the scene.
     * @param name Name of the scene.
     */
    public Scene (String name) {
        this.name = name;
    }

    /**
     * Gets the image of the scene
     * @return The scene background.
     */
    public Image getImage() {
        return image;
    }


    /**
     * Gets the name of the scene.
     * @return The name of the scene.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the background of the scene.
     * @param image The scene background.
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
