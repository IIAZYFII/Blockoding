package com.azyf.finalyearproject;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;

public class SceneController {
    private ArrayList<Scene> scenes;
  private  File[] files;
   private String dirPath;
    FileFilter imageFileFilter;
    private  String changeSceneTo;


    public String getChangeSceneTo() {
        return changeSceneTo;
    }

    public void setChangeSceneTo(String changeSceneTo) {
        this.changeSceneTo = changeSceneTo;
    }


    public SceneController() {
        scenes = new ArrayList<>();
        changeSceneTo = "Default";
        dirPath =  StageInitializer.getAbsolutePath() + "/Assets/Images/Scenes";
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
        Scene scene = new Scene( "Default");
        this.addScene(scene);
        loadDefaultScenes();

    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    public String[] getScenesAsList() {
        String[] listOfScenes = new String[scenes.size()];
        for (int i = 0; i < scenes.size(); i++) {
            listOfScenes[i] = scenes.get(i).getName();
        }
        return listOfScenes;
    }

    public Scene getScene(int i) {
        return scenes.get(i);
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    private void addScene(String filePath) {
        Image image  = new Image(filePath);
        String name = filePath.substring(filePath.lastIndexOf("\\") + 1,  filePath.lastIndexOf("."));
        Scene scene = new Scene(image, name);
        scenes.add(scene);
    }

    private void loadDefaultScenes() {
        File dir = new File(dirPath);
        files =  dir.listFiles(imageFileFilter);
        for(int i = 0; i < files.length; i++) {
            addScene(files[i].getAbsolutePath());
            if(!(scenes.get(i).getName().equals("Default"))) {
                ImageProcessor.produceImageThumbnails(scenes.get(i).getImage());
            }

        }


    }






}
