package com.azyf.finalyearproject;

import javafx.scene.image.WritableImage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class TextExtractor {
Tesseract tesseract;


    public TextExtractor() {
        tesseract = new Tesseract();
    }

    public void setDataPath(String dataPath) {
        try {
            tesseract.setDatapath(dataPath);
        } catch (Exception e) {
            System.out.println("Cannot set data path to" +  dataPath);
            System.exit(0);
        }

    }

    public void extractText (File imageFile) {
            try {
                String extractedText = tesseract.doOCR(imageFile);
                System.out.println(extractedText);
            } catch (TesseractException e) {
                e.printStackTrace();
            }


    }

    private void processImage(WritableImage imageFile) {
        WritableImage editImage = imageFile;



    }


}
