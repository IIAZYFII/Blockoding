package com.azyf.finalyearproject;

import javafx.scene.layout.HBox;

public class GUIBuilder {
    public GUIBuilder() {

    }

    public HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #FF4122;" + "-fx-border-style: solid inside;");
       return topBar;
    }
}
