package com.azyf.finalyearproject;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.io.File;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class GUIBuilder {
    private ButtonCreator buttonCreator;
    public GUIBuilder() {
        buttonCreator = new ButtonCreator();
    }

    public HBox createTopBar(ImageProcessor imageProcessor, TextExtractor textExtractor, Interpreter interpreter) {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #FF4122;" + "-fx-border-style: solid inside;");

        Button compileButton = buttonCreator.createCompileButton();
        Button OCRButton = buttonCreator.createOCRButton();
        Button variableButton = buttonCreator.createVariableButton();
        Button settingButton = buttonCreator.createSettingButton();
        Button playButton = buttonCreator.createPlayButton();
        Button stopButton = buttonCreator.createStopButton();
        Button sceneButton = buttonCreator.createSceneButton();

        AtomicReference<String> text = new AtomicReference<>("");
        OCRButton.setOnAction(e -> {
            String pathTempIMG = FileController.getAbsolutePath() + "/Cache/img.png";
            File image = new File(pathTempIMG);
            try {
                image = imageProcessor.processImage(image);
                String tmpText = textExtractor.extractText(image);
                String lastBlock = tmpText.substring(tmpText.lastIndexOf("\n") + 1);
                if(lastBlock.equalsIgnoreCase("END")) {
                    text.set(tmpText);
                    compileButton.setDisable(false);
                } else if (lastBlock.equalsIgnoreCase("CONTINUE")) {
                    Alert moreCode = new Alert(Alert.AlertType.INFORMATION);
                    moreCode.setTitle("Detected continue block");
                    moreCode.setContentText("You have used a continue block. Please finish your code and send it through.");
                    moreCode.showAndWait();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        compileButton.setOnAction(e -> {
            Queue<Block> blocks = interpreter.textToBlocks(text.get());
            //compiled = interpreter.checkSyntax(blocks);
            Queue<Block> programBlock = new LinkedList<>(blocks);
            //drawProgramBox(programBlock);
            System.out.println("-----------------------------------------");
            /*
            if (compiled == true) {
                interpreter.loadBlocks(blocks);
            }*/
            playButton.setDisable(false);
            e.consume();
        });

        variableButton.setOnAction(e-> {
            //drawVariableManager();
            e.consume();
        });
        playButton.setOnAction(e -> {
            playButton.setDisable(true);
            stopButton.setDisable(false);
            /*
            if (compiled == true) {
                interpreter.compileAndRun(spriteController, currentMouseXPos, currentMouseYPos, inputBoxesValues,
                        inputBoxes, variableManager, soundController, sceneController);
                drawScene();
                drawVariableBox();
                // System.out.println("Post " + variableManager.getVariables().get(0).getValue());

            }

             */

        });

        stopButton.setOnAction(e -> {
            StageInitializer.frameTimeline.stop();
            //variableManager.resetToInitialValues();
           // drawVariableBox();
            playButton.setDisable(false);
            stopButton.setDisable(true);
            //soundController.pressedStopButton();
            //terminal.clear();
        });


        settingButton.setOnAction(e -> {
            //drawSettings();
        });



        sceneButton.setOnAction(e-> {
            //drawSceneController();
        });


        compileButton.setDisable(true);

        topBar.getChildren().add(settingButton);
        topBar.getChildren().add(OCRButton);
        topBar.getChildren().add(compileButton);
        topBar.getChildren().add(variableButton);
        topBar.getChildren().add(sceneButton);
        topBar.getChildren().add(playButton);
        topBar.getChildren().add(stopButton);
        topBar.setMargin(OCRButton, new Insets(0, 0, 0, 20));
        topBar.setMargin(variableButton, new Insets(0, 20, 0, 20));;
        topBar.setMargin(playButton, new Insets(0, 0, 0, 300));


       return topBar;
    }
}
