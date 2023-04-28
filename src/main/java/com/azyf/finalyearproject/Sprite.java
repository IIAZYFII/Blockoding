/**
 * This class represents the sprite class.
 */
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

    /**
     * The constructor for the sprite.
     * @param spriteName The name of the sprite.
     * @param xPos The x position of the sprite.
     * @param yPos The y position of the sprite.
     * @param defaultOutfit The default outfit for the sprite.
     */
    public Sprite(String spriteName, double xPos, double yPos, Image defaultOutfit) {
        this.spriteName = spriteName;
        this.xPos = xPos;
        this.yPos = yPos;
        spriteOutfits.add(defaultOutfit);
    }

    /**
     * Gets the default outfit for the sprite.
     * @return The default outfit of the sprite.
     */
    public Image defaultOutfit() {
        return spriteOutfits.get(0);
    }

    /**
     * Gets the name of the sprite.
     * @return The name of the sprite.
     */
    public String getSpriteName() {
        return spriteName;
    }

    /**
     * Sets the name of the sprite.
     * @param spriteName The name of the sprite.
     */
    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    /**
     * Gets the x position of the sprite.
     * @return The x position of the sprite.
     */
    public double getXPos() {
        return xPos;
    }

    /**
     * Sets the x position of the sprite.
     * @param xPos The x position of the sprite.
     */
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    /**
     * Gets the y position of the sprite.
     * @return The y position of the sprite.
     */
    public double getYPos() {
        return yPos;
    }
    /**
     * Sets the y position of the sprite.
     * @return The y position of the sprite.
     */
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }


    /**
     * Gets the sprite outfit at a specific index.
     * @param i The index of the sprite.
     * @return The sprite outfit at the specific index.
     */
    public Image getSpriteOutfit(int i) {
        return spriteOutfits.get(i);
    }

    /**
     * Sets the sprite outfit at a index.
     * @param i The index of the sprite.
     * @param image The outfit of the sprite.
     */
    public void setSpriteOutfit(int i, Image image) {
        spriteOutfits.add(i,image);
    }

    /**
     * Sets the clicked update value of the sprite.
     * @param clicked The clicked value of the sprite.
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Gets the clicked value of the sprite.
     * @return True if the sprite has been clicked else it is false.
     */
    public boolean isClicked() {
        return clicked;
    }
}
