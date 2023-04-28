/**
 * This scene is used to store and control multiple scenes.
 * @author Hussain Asif.
 * @version 1.0.
 */
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
    private static String changeSceneTo;


    /**
     * Gets the new scene.
     * @return  The new scene.
     */
    public static String getChangeSceneTo() {
        return changeSceneTo;
    }


    /**
     * Sets the new scene.
     * @param changeScene The scene the background will be set up.
     */
    public static void setChangeSceneTo(String changeScene) {
        changeSceneTo = changeScene;
    }


    /**
     * The constructor for the scene controller.
     */
    public SceneController() {
        scenes = new ArrayList<>();
        changeSceneTo = "Default";
        dirPath =  FileController.getAbsolutePath() + "/Assets/Images/Scenes";
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

    /**
     * Gets an array list of the scene.
     * @return Array list of the scene.
     */
    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    /**
     * Returns the scene as an array.
     * @return The scene as an array.
     */
    public String[] getScenesAsList() {
        String[] listOfScenes = new String[scenes.size()];
        for (int i = 0; i < scenes.size(); i++) {
            listOfScenes[i] = scenes.get(i).getName();
        }
        return listOfScenes;
    }

    /**
     * Gets a scene at a specific index.
     * @param i Index of the scene.
     * @return The scene as the specific index.
     */
    public Scene getScene(int i) {
        return scenes.get(i);
    }


    /**
     * Adds a scene to the controller.
     * @param scene the scene that will added to the controller.
     */
    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    /**
     * Adds a scene to the controller using file path.
     * @param filePath The file path of the new scene.
     */
    private void addScene(String filePath) {
        Image image  = new Image(filePath);
        String name = filePath.substring(filePath.lastIndexOf("\\") + 1,  filePath.lastIndexOf("."));
        Scene scene = new Scene(image, name);
        scenes.add(scene);
    }

    /**
     * Loads all the default scenes.
     */
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
