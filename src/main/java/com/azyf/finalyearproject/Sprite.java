package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Sprite {
    private String spriteName;
    private double xPos;
    private double yPos;
    private ArrayList<Image> spriteOutfits = new ArrayList<Image>();

    public Sprite(String spriteName, double xPos, double yPos, Image defaultOutfit) {
        this.spriteName = spriteName;
        this.xPos = xPos;
        this.yPos = yPos;
        spriteOutfits.add(defaultOutfit);
    }

    public Image defaultOutfit() {
        return spriteOutfits.get(0);
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public double getXPos() {
        return xPos;
    }

    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

}
