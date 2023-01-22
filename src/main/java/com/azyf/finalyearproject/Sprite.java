package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Sprite {
    private boolean flip = false;
    private boolean clicked = false;
    private String spriteName;
    private double xPos;
    private double yPos;
    private ArrayList<Image> spriteOutfits = new ArrayList<Image>();
    private Queue<Block> codeBlocks = new LinkedList<>();

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

    public void addSpriteCode(Queue<Block> codeBlocks) {
        if (this.codeBlocks.size() == 0) {
            this.codeBlocks = codeBlocks;
        } else {
            for (int i = 0; i < codeBlocks.size(); i++) {
                this.codeBlocks.add(codeBlocks.remove());
            }
        }
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean isFlip() {
        return flip;
    }

    public  Queue<Block> getCodeBlocks() {
        return codeBlocks;
    }

    public Image getSpriteOutfit(int i) {
        return spriteOutfits.get(i);
    }

    public void setSpriteOutfit(int i, Image image) {
        spriteOutfits.add(i,image);
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }
}
