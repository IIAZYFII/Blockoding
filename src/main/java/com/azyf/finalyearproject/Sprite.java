package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Sprite {
    private String spriteName;
    private int xPos;
    private int yPos;
    private ArrayList<Image> spriteOutfits = new ArrayList<Image>();

    public Sprite(String spriteName, int xPos, int yPos, Image defaultOutfit) {
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

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getTPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

}
