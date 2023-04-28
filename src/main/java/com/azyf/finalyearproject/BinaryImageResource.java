/**
 * The class is a resource for binary image which allows the user to send an image through a http request.
 * @author Hussain Asif
 * @version  1.0
 */
package com.azyf.finalyearproject;
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

    /**
     * Allows the user to send an image through a http request.
     * @param binaryImage The image the user wants to send
     * @throws IOException An IOException maybe thrown if the image cannot be read or written to a file.
     */
    @PostMapping(value = "sendImage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void receiveBinaryImage(@RequestBody BinaryImage binaryImage) throws IOException {
        if (binaryImage != null) {
            byte[] imageBytes = java.util.Base64.getDecoder().decode(binaryImage.getImageContent());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage imageFromBytes = ImageIO.read(inputStream);
            File saveImage = new File("Cache\\img.png");
            ImageIO.write(imageFromBytes, "png", saveImage);
            System.out.println("Completed");


        }
    }
}
