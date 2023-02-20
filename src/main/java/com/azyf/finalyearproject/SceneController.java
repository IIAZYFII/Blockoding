package com.azyf.finalyearproject;

import java.util.ArrayList;

public class SceneController {
    ArrayList<Scene> scenes;

    public SceneController() {
        scenes = new ArrayList<>();
    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    public Scene getScene(int i) {
        return scenes.get(i);
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    
}
