package com.azyf.finalyearproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TextExtractor {
    Tesseract tesseract;
    WordProcessor wordProcessor;


    public TextExtractor() {
        tesseract = new Tesseract();
        wordProcessor = new WordProcessor();
        wordProcessor.addWordsToDictionary(new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Words\\default.txt"));

    }

    public void setDataPath(String dataPath) {
        try {
            tesseract.setDatapath(dataPath);
        } catch (Exception e) {
            System.out.println("Cannot set data path to" + dataPath);
            System.exit(0);
        }

    }

    public void extractText(File imageFile) throws IOException {
        System.out.println("Processing Image");
      File finalImageFile = processImage(imageFile);
        extractTextOnModified(finalImageFile);





    }
    private void extractTextOnModified(File imageFile){
        String extractedText;
        try {
            extractedText   = tesseract.doOCR(imageFile);
            System.out.println(extractedText);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }


    private File processImage(File imageFile) throws IOException {
        System.out.println("Converting to Gray Scale");
        convertToGrayScale(imageFile);
        System.out.println("Zooming into image");
        imageFile = zoomImage("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\imgGrayScale.png");
        return imageFile;
    }


    private void convertToGrayScale(File imageFile) throws IOException {
        Image editImage = new Image(new FileInputStream(imageFile));
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

    }


    private File zoomImage(String fileLocation) throws IOException {
        Image image = new Image(fileLocation, 1050, 1024, false, false);
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\zoomimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", saveImage);
        return saveImage;
    }
}
