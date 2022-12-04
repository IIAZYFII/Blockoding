package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Sprite {
    private boolean flipRight = false;
    private boolean flipLeft = false;
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

    public  Queue<Block> getCodeBlocks() {
        return codeBlocks;
    }

    public Image getSpriteOutfit(int i) {
        return spriteOutfits.get(i);
    }

    public boolean isFlipLeft() {
        return flipLeft;
    }

    public boolean isFlipRight() {
        return flipRight;
    }

    public void setFlipLeft(boolean flipLeft) {
        this.flipLeft = flipLeft;
    }

    public void setFlipRight(boolean flipRight) {
        this.flipRight = flipRight;
    }
}
