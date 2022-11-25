package com.azyf.finalyearproject;

import java.util.ArrayList;

public class SpriteController {
   private ArrayList<Sprite> sprites;

    public SpriteController() {
        sprites = new ArrayList<>();
    }

    public Sprite getSprite(int i) {
       return sprites.get(i);
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }


}
