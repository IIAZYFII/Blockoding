package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class SceneController {
    ArrayList<Scene> scenes;
    File[] files;
    String dirPath;
    FileFilter imageFileFilter;

    public SceneController() {
        scenes = new ArrayList<>();
//        dirPath = StageInitializer.getAbsolutePath() + "/Assets/Scenes";
        dirPath = "C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\Assets\\Images\\Scenes";
        imageFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                String fileExtension = "";
                try {
                  fileExtension  = fileName.substring(fileName.lastIndexOf('.') + 1);
                } catch (Exception ex) {
                    return  false;
                }

                if(fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("jpeg")) {
                    return  true;
                }
                return false;
            }
        };

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

    private void addScene(String filePath) {
        Image image  = new Image(filePath);
        String name = filePath.substring(0,  filePath.lastIndexOf("."));
        Scene scene = new Scene(image, name);
        scenes.add(scene);
    }

    public void loadDefaultScenes() {
        File dir = new File(dirPath);
        files =  dir.listFiles(imageFileFilter);
        for(int i = 0; i < files.length; i++) {
            addScene(files[i].getAbsolutePath());
           ImageProcessor.produceImageThumbnails(scenes.get(i).getImage());
        }


    }






}
