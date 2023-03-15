package com.azyf.finalyearproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class QRCodeBuilder {

    public  QRCodeBuilder() {

    }

    public void generateQRCode() throws WriterException, IOException {
        String localIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println(localIP);
        QRCodeWriter qrCodeGenerator = new QRCodeWriter();
        BitMatrix bitMatrix =
                qrCodeGenerator.encode(localIP, BarcodeFormat.QR_CODE, 100, 100);
        WritableImage writableImage = new WritableImage(100, 100);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        System.out.println(bitMatrix.toString());
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                if (bitMatrix.get(i, j) == true) {
                    pixelWriter.setColor(i, j, Color.color(0.0, 0.0, 0.0));
                } else {
                    pixelWriter.setColor(i, j, Color.color(1.0, 1.0, 1.0));
                }

            }
        }
        File saveImage = new File("Cache\\qrcode.png");
        BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(image, "png", saveImage);
    }
}
