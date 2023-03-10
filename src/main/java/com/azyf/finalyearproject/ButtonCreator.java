package com.azyf.finalyearproject;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonCreator {
    private static final String COMPILE_BUTTON_PATH =  "/Assets/Images/CompileButton.png";
    private static final String OCR_BUTTON_PATH = "/Assets/Images/OCRButton.png";
    private static final String VARIABLE_BUTTON_PATH = "/Assets/Images/VariableButton.png";
    private static final String SETTINGS_BUTTON_PATH = "/Assets/Images/SettingsButton.png";
    private static final String PLAY_BUTTON_PATH = "/Assets/Images/PlayButton.png";
    private static final String STOP_BUTTON_PATH = "/Assets/Images/StopButton.png";
    public ButtonCreator() {

    }

    public Button createCompileButton() {
        return  createButton(COMPILE_BUTTON_PATH);
    }

    public Button createOCRButton() {
        return createButton(OCR_BUTTON_PATH);
    }

    public Button createVariableButton() {
        return createButton(VARIABLE_BUTTON_PATH);
    }

    public Button createSettingButton() {
        return createButton(SETTINGS_BUTTON_PATH);
    }

    public Button createPlayButton() {
        return createButton(PLAY_BUTTON_PATH);
    }


    private Button createButton(String buttonImagePath) {
        Button button = new Button();
        String pathButton = FileController.getAbsolutePath() + buttonImagePath;
        Image buttonImg = new Image(pathButton);
        ImageView buttonView = new ImageView(buttonImg);
        buttonView.setFitHeight(50);
        buttonView.setFitWidth(50);
        button.setGraphic(buttonView);
        return  button;
    }

}
