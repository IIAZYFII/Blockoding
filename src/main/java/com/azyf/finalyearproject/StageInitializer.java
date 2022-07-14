package com.azyf.finalyearproject;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {
        BorderPane root = new BorderPane();

        Stage stage = event.getStage();
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
}
