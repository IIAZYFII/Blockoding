package com.azyf.finalyearproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageProcessor {

    public ImageProcessor() {

    }

    File processImage(File imageFile) throws IOException {
        System.out.println("Converting to Gray Scale");
        convertToGrayScale(imageFile);
        System.out.println("Zooming into image");
        imageFile = zoomImage("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\rotateimg.png");
        return imageFile;
    }

    private void convertToGrayScale(File imageFile) throws IOException {
        javafx.scene.image.Image editImage = new javafx.scene.image.Image(new FileInputStream(imageFile));
        PixelReader pixelReader = editImage.getPixelReader();
        int height = (int) editImage.getHeight();
        int width = (int) editImage.getWidth();
        BufferedImage image = SwingFXUtils.fromFXImage(editImage, null);
        double d =
                image.getRGB(image.getTileWidth() / 2, image.getTileHeight() / 2);
        System.out.println("The d is" + d);
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
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\imgGrayScale.png");
        image = SwingFXUtils.fromFXImage(grayScaleImage, null);
        ImageIO.write(image, "png", saveImage);

        if(grayScaleImage.getWidth() > grayScaleImage.getHeight()) {
            rotateImage(new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\imgGrayScale.png"));
        }


    }


    private File zoomImage(String fileLocation) throws IOException {
        javafx.scene.image.Image image = new javafx.scene.image.Image(fileLocation, 1050, 1024, false, false);
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\zoomimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", saveImage);
        return saveImage;
    }

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
                double scalingRatioX =  yA / xB;
                double scalingRatioY =  xA / yB;

                    double val = pixelReader.getColor(j,(int)(yA - i - 1)).getRed();
                    Color color=Color.color(val,val,val);
                    PixelWriter image_writer = rotatedImage.getPixelWriter();
                    image_writer.setColor((int) i, (int)j, color);

            }
        }
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\rotateimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(rotatedImage, null);
        ImageIO.write(bufferedImage, "png", saveImage);
    }
}

