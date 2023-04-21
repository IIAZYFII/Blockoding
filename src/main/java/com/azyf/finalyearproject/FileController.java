/**
 * This class deals with the file system and external files within the operating system.
 * @author  Hussain Asif.
 * @version 1.0
 */
package com.azyf.finalyearproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class FileController {
    private final static String defaultSpritePath =  getAbsolutePath() + "/Assets/Images/Sprites/Default.png";
    private final static String newDefaultSpritePath = getAbsolutePath() + "/Cache/Default.png";

    /**
     * An empty constructor for the FileController.
     */
    public FileController() {}

    /**
     * Sets up the default sprite.
     * @throws IOException
     */
    public void setupSprite() throws IOException {
        Image image = new Image(defaultSpritePath);
        String spriteName = "Default";
        System.out.println(spriteName);
        File tmp = new File(newDefaultSpritePath);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", tmp);
    }

    /**
     * Gets the absolute path of the program.
     * @return The absolute path as a String.
     */
    public static String getAbsolutePath() {
        File path = new File("");
        String systemPath = path.getAbsolutePath();
        System.out.println(systemPath);
        return systemPath;
    }

}
