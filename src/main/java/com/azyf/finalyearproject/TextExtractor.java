package com.azyf.finalyearproject;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Extracts the text from an image and processes the image.
 * @author Hussain Asif
 * @version 1.0
 */
public class TextExtractor {
    //Tesseract tesseract;
    WordProcessor wordProcessor;


    public TextExtractor() {
        //tesseract = new Tesseract();
        wordProcessor = new WordProcessor();
        wordProcessor.addWordsToDictionary(new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Words\\default.txt"));

    }
/*


    public void setDataPath(String dataPath) {
        try {
            tesseract.setDatapath(dataPath);
        } catch (Exception e) {
            System.out.println("Cannot set data path to" + dataPath);
            System.exit(0);
        }

    }
*/
    public void extractText(File imageFile) throws IOException {
        System.out.println("Processing Image");
      File finalImageFile = processImage(imageFile);
        extractTextOnModified(finalImageFile);





    }
    private void extractTextOnModified(File imageFile) throws IOException {

        String extractedText = performOCR(imageFile.getPath());
        /*
         try {
            extractedText   = tesseract.doOCR(imageFile);
            System.out.println(extractedText);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
         */

        wordProcessor.processWord(extractedText);
    }


    private static String performOCR(String imageFilePath) throws IOException {

        ByteString imageBytesGoogle = ByteString.readFrom(new FileInputStream((imageFilePath)));

        Image imageGoogle = Image.newBuilder().setContent(imageBytesGoogle).build();

        Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        List<AnnotateImageRequest> requests = new ArrayList<>();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(imageGoogle).build();
        requests.add(request);

        ImageAnnotatorClient client = ImageAnnotatorClient.create();
        try {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for(AnnotateImageResponse currentResponse : responses) {
                if(currentResponse.hasError()) {
                    System.out.println("An error has occured: " + currentResponse.getError().getMessage());
                    return "error";
                } else {
                    for(EntityAnnotation annotation : currentResponse.getTextAnnotationsList()) {
                        System.out.println(annotation.getDescription());
                        return  annotation.getDescription();
                    }
                }
            }


        } finally {
            client.close();
        }
        return "error";
    }

    private File processImage(File imageFile) throws IOException {
        System.out.println("Converting to Gray Scale");
        convertToGrayScale(imageFile);
        System.out.println("Zooming into image");
        imageFile = zoomImage("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\imgGrayScale.png");
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

    }


    private File zoomImage(String fileLocation) throws IOException {
        javafx.scene.image.Image image = new javafx.scene.image.Image(fileLocation, 1050, 1024, false, false);
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\zoomimg.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", saveImage);
        return saveImage;
    }
}
