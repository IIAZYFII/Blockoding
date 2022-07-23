package com.azyf.finalyearproject;

import javafx.scene.image.Image;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@RestController
public class BinaryImageResource {

    @PostMapping(value = "sendImage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void receiveBinaryImage(@RequestBody BinaryImage binaryImage) throws IOException {
        if (binaryImage != null) {
            //Converts image into bytes from string
            byte[] imageBytes = java.util.Base64.getDecoder().decode(binaryImage.getImageContent());

            //Creates a stream so bytes are readable
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            //Reads the bytes from the input stream and sets it to the image
            BufferedImage imageFromBytes = ImageIO.read(inputStream);

            File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\img.png");
            ImageIO.write(imageFromBytes, "png", saveImage);
            System.out.println("done");

        }
    }
}
