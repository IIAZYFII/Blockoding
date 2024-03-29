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
 * @version 3.0
 */
public class TextExtractor {

    String continueText = "";


    /**
     * Constructor for text extractor.
     */
    public TextExtractor() {


    }

    /**
     * Extracts the text fromm an image.
     * @param imageFile The image as a file
     * @return A string extracted text.
     * @throws IOException
     */
    public String extractText(File imageFile) throws IOException {
        String extractedText = performOCR(imageFile.getPath());
        String lastBlock = extractedText.substring(extractedText.lastIndexOf("\n") + 1);
        if(lastBlock.equalsIgnoreCase("CONTINUE")) {
            continueText = continueText + "\n" + extractedText;
            return continueText;
        } else if (lastBlock.equalsIgnoreCase("END")) {
            extractedText = continueText+ "\n" + extractedText;
            continueText = "";
        }
        System.out.println(extractedText);
        return extractedText;

    }

    /**
     * Performs OCR using Google's Library. The Google's Cloud Vision to understand and help code and use the library/
     * Here is the reference -> https://cloud.google.com/vision.
     * @param imageFilePath
     * @return
     * @throws IOException
     */
    private static String performOCR(String imageFilePath) throws IOException {
        System.out.println("Extracting text");

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





}
