/**
 * This class creates for the UI.
 * @author Hussain Asif.
 * @version 1.0.
 */
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
    private static final String SCENE_BUTTON_PATH = "/Assets/Images/SceneButton.png";

    private static final int BUTTON_SIZE = 50;
    public ButtonCreator() {

    }

    /**
     * Creates a compile button.
     * @return The compile button.
     */
    public Button createCompileButton() {
        return  createButton(COMPILE_BUTTON_PATH);
    }

    /**
     * Creates a OCR button.
     * @return The OCR button.
     */
    public Button createOCRButton() {
        return createButton(OCR_BUTTON_PATH);
    }

    /**
     * Creates a variable button.
     * @return The variable button.
     */
    public Button createVariableButton() {
        return createButton(VARIABLE_BUTTON_PATH);
    }

    /**
     * Creates a settings button.
     * @return The settings button.
     */
    public Button createSettingButton() {
        return createButton(SETTINGS_BUTTON_PATH);
    }

    /**
     * Creates a play button.
     * @return The play button.
     */
    public Button createPlayButton() {
        return createButton(PLAY_BUTTON_PATH);
    }

    /**
     * Creates a stop button.
     * @return The stop button.
     */
    public Button createStopButton() {
        return createButton(STOP_BUTTON_PATH);
    }

    /**
     * Creates a scene button.
     * @return The scene button.
     */
    public Button createSceneButton() {
        return createButton(SCENE_BUTTON_PATH);
    }


    /**
     * Creates a generic button that is used to create all other buttons.
     * @param buttonImagePath The path of the Image used for the button.
     * @return A Button.
     */
    private Button createButton(String buttonImagePath) {
        Button button = new Button();
        String pathButton = FileController.getAbsolutePath() + buttonImagePath;
        Image buttonImg = new Image(pathButton);
        ImageView buttonView = new ImageView(buttonImg);
        buttonView.setFitHeight(BUTTON_SIZE);
        buttonView.setFitWidth(BUTTON_SIZE);
        button.setGraphic(buttonView);
        return  button;
    }




}
