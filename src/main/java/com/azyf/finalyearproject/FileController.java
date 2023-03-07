package com.azyf.finalyearproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.imgcodecs.Imgcodecs;


import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class FileController {
    private final static String defaultSpritePath =  getAbsolutePath() + "/Assets/Images/Sprites/Default.png";
    private final static String newDefaultSpritePath = getAbsolutePath() + "/Cache/Default.png";

    public FileController() {}

    public void setupSprite() throws IOException {
        Image image = new Image(defaultSpritePath);
        String spriteName = "Default";
        System.out.println(spriteName);
        File tmp = new File(newDefaultSpritePath);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", tmp);
    }


    public static String getAbsolutePath() {
        File path = new File("");
        String systemPath = path.getAbsolutePath();
        System.out.println(systemPath);
        return systemPath;
    }

}
