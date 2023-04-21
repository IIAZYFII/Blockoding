/**
 * This is responsible for a multitude of functionalities. These include pre-processing the image and rotating the image
 * for OCR. This class also deals with sprite images and rotates or flips them. Furthermore, the class also generates
 * thumbnails for the background of the scenes.
 * @auhtor Hussain Asif.
 * @version 2.0
 */

package com.azyf.finalyearproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageProcessor {
    private static final int RESOLUTION_HEIGHT = 1050;
    private static final int RESOLUTION_WIDTH = 1024;

    /**
     * The constructor for the image processor. The constructor loads the OpenCV2 library.
     */
    public ImageProcessor() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);
    }

    File processImage(File imageFile) throws IOException {
        System.out.println("Converting to Gray Scale");
        convertToGrayScale(imageFile);
        System.out.println("Zooming into image");
        String pathTempRotateImg =  FileController.getAbsolutePath() + "/Cache/rotateimg.png";
        imageFile = zoomImage(pathTempRotateImg);
        return imageFile;
    }


    /**
     * Converts the image to grayscale.
     * @param imageFile The image that will be converted to grayscale.
     * @throws IOException
     */
    private void convertToGrayScale(File imageFile) throws IOException {


        javafx.scene.image.Image editImage = new javafx.scene.image.Image(new FileInputStream(imageFile));
        PixelReader pixelReader = editImage.getPixelReader();
        int height = (int) editImage.getHeight();
        int width = (int) editImage.getWidth();

        WritableImage grayScaleImage = new WritableImage(width, height);
        PixelWriter pixelWriter = grayScaleImage.getPixelWriter();



        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                double redVal = pixelReader.getColor(i, j).getRed();
                double greenVal = pixelReader.getColor(i, j).getGreen();
                double blueVal = pixelReader.getColor(i, j).getBlue();
                double finalVal = (redVal + greenVal + blueVal) / 3;
                Color color = Color.color(finalVal, finalVal, finalVal);
                pixelWriter.setColor(i, j, color);
            }
        }
        File saveImage = new File("Cache\\imgGrayScale.png");
        BufferedImage image = SwingFXUtils.fromFXImage(grayScaleImage, null);
        ImageIO.write(image, "png", saveImage);

        if(grayScaleImage.getWidth() > grayScaleImage.getHeight()) {
            rotateImage(new File("Cache\\imgGrayScale.png"));
        }


    }


    /**
     * Zooms and stretches the image using.
     * @param fileLocation The location of the image.
     * @return the image with the changed resolution.
     * @throws IOException
     */
    private File zoomImage(String fileLocation) throws IOException {
        javafx.scene.image.Image image = new javafx.scene.image.Image(fileLocation,
                RESOLUTION_HEIGHT, RESOLUTION_WIDTH, false, false);
        File saveImage = new File("Cache\\zoomimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", saveImage);
        return saveImage;
    }

    /**
     * Rotating the orientation
     * @param imageFile
     * @throws IOException
     */
    public static void rotateImage(File imageFile) throws IOException {
        javafx.scene.image.Image rotateImage =  new javafx.scene.image.Image(new FileInputStream(imageFile));
        double yB = rotateImage.getWidth(); //rotated height
        double xB = rotateImage.getHeight(); //rotated width
        double xA = rotateImage.getWidth();
        double yA = rotateImage.getHeight();

        WritableImage rotatedImage = new WritableImage((int)xB, (int)yB);
        PixelReader pixelReader = rotateImage.getPixelReader();

        for(int j = 0; j <= yB - 1; j++) {
            for(int i = 0; i <= xB -1; i++) {


                    double val = pixelReader.getColor(j,(int)(yA - i - 1)).getRed();
                    Color color=Color.color(val,val,val);
                    PixelWriter image_writer = rotatedImage.getPixelWriter();
                    image_writer.setColor((int) i, (int)j, color);

            }
        }
        File saveImage = new File("Cache\\rotateimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(rotatedImage, null);
        ImageIO.write(bufferedImage, "png", saveImage);
    }

    public static Image flipImage(Image sprite, String direction) {
        Mat src = Imgcodecs.imread(sprite.getUrl());
        Mat newSprite = new Mat();
        if(direction.equals("VERTICAL")){
            Core.flip(src, newSprite, 1);
        } else if (direction.equals("HORIZONTAL")) {
            Core.flip(src, newSprite, 0);
        }
        System.out.println(sprite.getUrl());
        Imgcodecs.imwrite(sprite.getUrl(), newSprite);
        sprite = new Image(sprite.getUrl());
        return sprite;
    }

    public static Image rotateImage(Image sprite, String direction, String amount) {
        Mat src = Imgcodecs.imread(sprite.getUrl());
        Mat newSprite = new Mat(src.rows(), src.cols(), src.type());
        if((direction.equals("RIGHT") && amount.equals("90")) || (direction.equals("LEFT") && amount.equals("270"))) {
            Core.rotate(src,  newSprite,Core.ROTATE_90_CLOCKWISE);
            Imgcodecs.imwrite(sprite.getUrl(), newSprite);
            sprite = new Image(sprite.getUrl());
            return sprite;
        } else if (amount.equals("180")) {
            Core.rotate(src,  newSprite,Core.ROTATE_180);
            Imgcodecs.imwrite(sprite.getUrl(), newSprite);
            sprite = new Image(sprite.getUrl());
            return sprite;
        } else if ((direction.equals("LEFT") && amount.equals("90"))  || (direction.equals("RIGHT") && amount.equals("270"))) {
            Core.rotate(src,  newSprite,Core.ROTATE_90_COUNTERCLOCKWISE);
            Imgcodecs.imwrite(sprite.getUrl(), newSprite);
            sprite = new Image(sprite.getUrl());
            return sprite;
        }

        Imgcodecs.imwrite(sprite.getUrl(), newSprite);
        sprite = new Image(sprite.getUrl());
        return sprite;
    }

    /**
     * Produces a thumbnail for the backgorund .
     * @param image
     */
    public static void produceImageThumbnails(Image image) {
       Mat srcImage = Imgcodecs.imread(image.getUrl());
       Mat dstThumbnail = new Mat();
       Imgproc.resize(srcImage, dstThumbnail, new Size(75,75), Imgproc.INTER_CUBIC);

        String imagePath  = image.getUrl();
        String fileName = imagePath.substring(imagePath.lastIndexOf('\\'));
        String path = FileController.getAbsolutePath() + "/Assets/Images/Scenes/Thumbnails/" + fileName;
       Imgcodecs.imwrite(path, dstThumbnail);
    }
}

