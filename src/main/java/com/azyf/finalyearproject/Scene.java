package com.azyf.finalyearproject;

import javafx.scene.image.Image;

public class Scene {
    private Image image;
    private String name;



    public Scene(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    public Scene (String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setImage(Image image) {
        this.image = image;
    }
}
