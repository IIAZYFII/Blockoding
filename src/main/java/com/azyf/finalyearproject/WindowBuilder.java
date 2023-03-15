package com.azyf.finalyearproject;

import com.google.zxing.WriterException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public void drawSceneController(ImageProcessor imageProcessor) {
        Stage sceneControllerStage = new Stage();
        sceneControllerStage.setResizable(false);
        ScrollPane basePane = new ScrollPane();
        basePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        basePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        BorderPane sceneControllerRoot = new BorderPane();

        basePane.setContent(sceneControllerRoot);
        sceneControllerRoot.setPrefSize(350,350);
        sceneControllerRoot.setStyle("-fx-background-color: #FF5438;");

        File dir = new File(FileController.getAbsolutePath() + "/Assets/Images/Scenes/Thumbnails");
        File[] files = dir.listFiles();
        VBox vBox = new VBox();
        for(int i = 0; i < files.length; i++) {
            Image image = new Image(files[i].getAbsolutePath());
            ImageView imageView = new ImageView(image);
            HBox hBox = new HBox();
            hBox.getChildren().add(imageView);

            Label label = new Label(files[i].getName().substring(0, files[i].getName().lastIndexOf(".")));
            label.setMinSize(75,50);
            hBox.getChildren().add(label);

            Button sceneBtn = new Button();
            sceneBtn.setMinSize(100,25);
            sceneBtn.setText("Choose Scene");
            int finalI = i;
            sceneBtn.setOnAction(e-> {
                StageInitializer.setSceneBackground( new Image(FileController.getAbsolutePath() + "/Assets/Images/Scenes/" + files[finalI].getName()));
               // drawScene();
                e.consume();
            });
            hBox.getChildren().add(sceneBtn);
            vBox.getChildren().add(hBox);
        }
        Button removeSceneBtn = new Button();
        removeSceneBtn.setMinSize(100,25);
        removeSceneBtn.setText("Remove scene");
        removeSceneBtn.setOnAction(e->{
           StageInitializer.setSceneBackground(null);
           // drawScene();
        });

        Button addSceneBtn = new Button();
        addSceneBtn.setMinSize(125,25);
        addSceneBtn.setText("Add custom scene");
        addSceneBtn.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Scene");
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("Image files (*.PNG, *.JPEG, *.JPG, )", "*.PNG", "*.JPEG", "*.JPG");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(sceneControllerStage);
            if (file != null) {
                Image image = new Image(file.getAbsolutePath());
                File copyImage = new File(FileController.getAbsolutePath() + "/Assets/Images/Scenes/" + file.getName());
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    ImageIO.write(bufferedImage,"png",copyImage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                imageProcessor.produceImageThumbnails(image);
                drawSceneController(imageProcessor);
                sceneControllerStage.close();
            }

        });
        HBox hBox = new HBox();
        hBox.getChildren().add(removeSceneBtn);
        hBox.getChildren().add(addSceneBtn);

        vBox.getChildren().add(hBox);
        sceneControllerRoot.getChildren().add(vBox);
        Scene scene = new Scene(basePane, 350, 350);


        sceneControllerStage.setScene(scene);
        sceneControllerStage.show();

    }
}
