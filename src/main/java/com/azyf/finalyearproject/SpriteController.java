package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class SpriteController {
   private ArrayList<Sprite> sprites;

    public SpriteController() {
        sprites = new ArrayList<>();
    }

    public Sprite getSprite(int i) {
       return sprites.get(i);
    }

    public void setSprite(int index, Sprite sprite) {
        sprites.set(index, sprite);
    }

    public void addSprite(String spriteName, double xPos, double yPos, Image defaultOutfit) {
        Sprite spriteObject = new Sprite("default",xPos, yPos,defaultOutfit);
        sprites.add(spriteObject);

    }

    public int findSprite(double xPos, double yPos) {
        for(int i = 0; i < sprites.size(); i++) {
           double x =  sprites.get(i).getXPos();
           double y = sprites.get(i).getYPos();

           double xDifference = Math.abs(x - xPos);
           double yDifference = Math.abs(y - yPos);

           if(xDifference <= 40 && yDifference <= 40) {
              return i;
           }
        }
        return -1;
    }

    public void changeSpriteName(String newSpriteName, String oldSpriteName) {
        for(int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.get(i);
            if(sprite.getSpriteName().equals(newSpriteName)) {
                throw new UnsupportedOperationException();
            } else if (sprite.getSpriteName().equals(oldSpriteName)) {
                sprites.get(i).setSpriteName(newSpriteName);
            }
        }
    }




    public int size() {
        return  sprites.size();
    }

    public  void addSpriteCode(Queue<Block> code, int i) {
        sprites.get(i).addSpriteCode(code);
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public String[] getSpriteNameAsArray() {
        String[] spriteNames = new String[this.getSprites().size()];
        for(int i= 0; i < this.getSprites().size(); i++) {
            spriteNames[i] = this.getSprite(i).getSpriteName();
        }
        return spriteNames;
    }

    public Queue<Block> getSpriteCodeBlocks(int i) {
       return sprites.get(i).getCodeBlocks();
    }



}
