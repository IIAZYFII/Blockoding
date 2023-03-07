package com.azyf.finalyearproject;

import java.io.File;

public class FileController {
    public FileController() {}

    public void setupSprite() {

    }


    public static String getAbsolutePath() {
        File path = new File("");
        String systemPath = path.getAbsolutePath();
        System.out.println(systemPath);
        return systemPath;
    }

}
