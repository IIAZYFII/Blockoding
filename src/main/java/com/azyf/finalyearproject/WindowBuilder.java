package com.azyf.finalyearproject;

import com.google.zxing.WriterException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowBuilder {
    QRCodeBuilder qrCodeBuilder;

    public WindowBuilder() {
        qrCodeBuilder = new QRCodeBuilder();
    }

    public void drawSettings() {
        Stage settingStage = new Stage();
        HBox hBox = new HBox();
        BorderPane setttingsRoot = new BorderPane();
        setttingsRoot.setStyle("-fx-background-color: #FF5438;");
        Button linkAppButton = new Button();
        linkAppButton.setMinSize(100, 100);
        linkAppButton.setText("Link App");
        linkAppButton.setOnAction(e -> {
            try {
                qrCodeBuilder.generateQRCode();
                hBox.getChildren().clear();
                String path = FileController.getAbsolutePath() + "\\Cache\\qrcode.png";
                System.out.println(path);
                Image qrCode = new Image(path);
                ImageView qrCodeViewer = new ImageView(qrCode);
                qrCodeViewer.setFitHeight(120);
                qrCodeViewer.setFitWidth(120);
                hBox.getChildren().add(qrCodeViewer);

            } catch (WriterException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        hBox.setPadding(new Insets(100, 0, 0, 120));
        hBox.getChildren().add(linkAppButton);
        setttingsRoot.getChildren().add(hBox);

        settingStage.setResizable(false);
        javafx.scene.Scene scene = new Scene(setttingsRoot, 350, 350);
        settingStage.setScene(scene);
        settingStage.showAndWait();
    }

}
