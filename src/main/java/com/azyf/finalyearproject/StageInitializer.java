package com.azyf.finalyearproject;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    private Canvas canvas;
    private TextExtractor textExtractor = new TextExtractor();
    private Canvas blockCanvas;
    private Image playButtonImg;
    private Image stopButtonImg;
    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {
        BorderPane root = (BorderPane) buildGUI();

        Stage stage = event.getStage();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private Pane buildGUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #96ffa1;");

        canvas = new Canvas(600,400);
        root.setCenter(canvas);

        HBox sceneBox = new HBox();
        root.setLeft(sceneBox);

        AnchorPane bottomBar = new AnchorPane();
        root.setBottom(bottomBar);

        VBox ioBar = new VBox();
        VBox tabBar = new VBox();
        bottomBar.getChildren().add(ioBar);
        bottomBar.getChildren().add(tabBar);
        AnchorPane.setLeftAnchor(ioBar, 10d);
        AnchorPane.setRightAnchor(tabBar,10d);

      HBox topBar = new HBox();
        root.setTop(topBar);

        HBox rightPane = new HBox();
        VBox spriteBox = new VBox();

        blockCanvas = new Canvas(100,200);
        rightPane.getChildren().add(spriteBox);
        rightPane.getChildren().add(blockCanvas);

       Button playButton = new Button();
       //playButton.setStyle("-fx-background-color: transparent;");
       playButtonImg = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\Playbutton.png");
       ImageView playButtonView = new ImageView(playButtonImg);
       playButton.setGraphic(playButtonView);
       topBar.getChildren().add(playButton);

       Button stopButton = new Button();
       //stopButton.setStyle("-fx-background-color: transparent;");
       stopButtonImg = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\Stopbutton.png");
       ImageView stopButtonView = new ImageView(stopButtonImg);
       stopButton.setGraphic(stopButtonView);
       topBar.getChildren().add(stopButton);

        Button tessButton = new Button();
        topBar.getChildren().add(tessButton);
        tessButton.setOnAction(e -> {
            textExtractor.setDataPath("C:\\Users\\hussa\\OneDrive\\Desktop\\Tess4J\\tessdata");
            File image =   new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\img.png");
            try {
                textExtractor.extractText(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return  root;
    }
}
