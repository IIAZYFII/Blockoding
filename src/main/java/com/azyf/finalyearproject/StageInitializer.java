package com.azyf.finalyearproject;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    private Canvas canvas;
    private Canvas blockCanvas;
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







        return  root;
    }
}
