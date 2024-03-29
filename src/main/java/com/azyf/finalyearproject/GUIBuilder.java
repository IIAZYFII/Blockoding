/**
 * This class creates the entire GUI for the application.
 * @author Hussain Asif.
 * @version 1.0.
 */
package com.azyf.finalyearproject;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class GUIBuilder {
    private ButtonCreator buttonCreator;
    private WindowBuilder windowBuilder;
    private ProgramBoxBuilder programBoxBuilder;
    private SpriteBoxBuilder spriteBoxBuilder;
    private VariableBoxBuilder variableBoxBuilder;

    /**
     * The constructor for the GUIBuilder.
     */
    public GUIBuilder() {
        buttonCreator = new ButtonCreator();
        windowBuilder = new WindowBuilder();
        programBoxBuilder = new ProgramBoxBuilder();
        spriteBoxBuilder = new SpriteBoxBuilder();
        variableBoxBuilder = new VariableBoxBuilder();
    }

    /**
     * Creates the top bar for the GUI/
     * @param imageProcessor - The image processor.
     * @param textExtractor - The  text extractor.
     * @param interpreter - The interpreter.
     * @param variableManager - The manager for variable.
     * @param soundController - The controller for sound.
     * @param spriteController  - The controller for the sprite.
     * @param sceneController - The controller for the scene.
     * @return The top bar for the GUI.
     */
    public HBox createTopBar(ImageProcessor imageProcessor, TextExtractor textExtractor,
                             Interpreter interpreter, VariableManager variableManager, SoundController soundController,
                             SpriteController spriteController, SceneController sceneController) {
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
             StageInitializer.setCompiled(interpreter.checkSyntax(blocks, windowBuilder));
             System.out.println("is compiled " +  StageInitializer.getCompiled());
             if(StageInitializer.getCompiled() == false) {
                windowBuilder.drawSyntaxError();
             } else {
                 Queue<Block> programBlock = new LinkedList<>(blocks);

                 VBox programBox =
                         programBoxBuilder.drawProgramBox(programBlock, variableManager, soundController,
                                 spriteController, sceneController);

                 StageInitializer.setLeftPanel(buildLeftPane(programBox));
                 System.out.println("-----------------------------------------");
             }


            if (StageInitializer.getCompiled() == true) {
                interpreter.loadBlocks(blocks);
            }
            playButton.setDisable(false);
            e.consume();
        });

        variableButton.setOnAction(e-> {
            windowBuilder.drawVariableManager(variableManager);
            if(windowBuilder.getAlreadyExist() == false) {
                StageInitializer.setVariableBox(variableBoxBuilder.drawVariableBox(StageInitializer.getVariableBox(), variableManager));
            }
            e.consume();
        });



        playButton.setOnAction(e -> {
            playButton.setDisable(true);
            stopButton.setDisable(false);

            if (StageInitializer.getCompiled() == true) {
                interpreter.compileAndRun(spriteController, StageInitializer.getCurrentMouseXPos(), StageInitializer.getCurrentMouseYPos(), programBoxBuilder.getInputBoxBuilder().getInputBoxesValues(),
                        programBoxBuilder.getInputBoxBuilder().getInputBoxes(), variableManager, soundController, sceneController);
                StageInitializer.drawScene(sceneController, spriteController);
                StageInitializer.setVariableBox(variableBoxBuilder.drawVariableBox(StageInitializer.getVariableBox(), variableManager));

            }



        });

        stopButton.setOnAction(e -> {
            StageInitializer.frameTimeline.stop();
            variableManager.resetToInitialValues();
            StageInitializer.setVariableBox(variableBoxBuilder.drawVariableBox(StageInitializer.getVariableBox(), variableManager));
            playButton.setDisable(false);
            stopButton.setDisable(true);
            soundController.pressedStopButton();
            TerminalComponent.getTerminal().clear();
            e.consume();
        });


        settingButton.setOnAction(e -> {
            windowBuilder.drawSettings();
            e.consume();
        });



        sceneButton.setOnAction(e-> {
            windowBuilder.drawSceneController(imageProcessor, sceneController, spriteController);
            e.consume();
        });


        compileButton.setDisable(true);

        topBar.getChildren().add(settingButton);
        topBar.getChildren().add(OCRButton);
        topBar.getChildren().add(compileButton);
        topBar.getChildren().add(variableButton);
        topBar.getChildren().add(sceneButton);
        topBar.getChildren().add(playButton);
        topBar.getChildren().add(stopButton);
        topBar.setMargin(OCRButton, new Insets(0, 20, 0, 20));
        topBar.setMargin(variableButton, new Insets(0, 20, 0, 20));
        topBar.setMargin(playButton, new Insets(0, 0, 0, 300));


       return topBar;
    }


    /**
     * Creates the initial left pane with an empty program box for the GUI.
     * @return the left pane.
     */
    public VBox buildLeftPane() {
        VBox LeftPane = new VBox();
        VBox programBox = new VBox();
        programBox.setStyle( "-fx-background-color: #FFFDD0;");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(programBox);
        LeftPane.getChildren().add(scrollPane);
        LeftPane.setMargin(scrollPane, new Insets(50,0,0,50));
        programBox.setPrefSize(300,650);

        return LeftPane;
    }

    /**
     * An overridden method to create a left pane with a non-empty program box for the GUI.
     * @param programBox The program box to set the left pane.
     * @return A non-empty left pane.
     */
    private VBox buildLeftPane(VBox programBox) {
        VBox LeftPane = new VBox();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(programBox);
        LeftPane.getChildren().add(scrollPane);
        LeftPane.setMargin(scrollPane, new Insets(50,0,0,50));
        programBox.setPrefSize(300,650);


        return LeftPane;
    }

    /**
     * Builds the right pane for the GUI.
     * @param sceneController The controller for the scene.
     * @param spriteController The controller for the sprite.
     * @param stage The stage of the scene.
     * @return A right pane containing a sprite and variable box.
     */
    public VBox buildRightPane(SceneController sceneController, SpriteController spriteController, Stage stage) {
        VBox rightPane = new VBox();
        AtomicReference<HBox> spriteBox = new AtomicReference<>(new HBox());
        ImageView imageView = new ImageView();

        Image image = new Image( FileController.getAbsolutePath() +  "\\Assets\\Images\\Sprites\\default.png");
        imageView.setImage(image);
        spriteBox.set(spriteBoxBuilder .addSpriteToBox("Blocky Bro", imageView, sceneController,
                spriteController, windowBuilder,spriteBox.get()));

        spriteBox.get().setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");
        spriteBox.get().setPrefSize(400,200);

        AtomicReference<ContextMenu> contextMenu = new AtomicReference<>(new ContextMenu());
        HBox finalSpriteBox = spriteBox.get();
        spriteBox.get().setOnMouseClicked(e -> {
            if (contextMenu.get() != null) {
                contextMenu.get().hide();
                contextMenu.set(null);
            }
            if (e.getButton() == MouseButton.SECONDARY) {

                MenuItem uploadSpriteMenuItem = new MenuItem("Upload Sprite");
                uploadSpriteMenuItem.setOnAction(actionEvent -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Upload Sprite");
                    FileChooser.ExtensionFilter extensionFilter =
                            new FileChooser.ExtensionFilter("Image files (*.PNG, *.JPEG, *.JPG, )", "*.PNG", "*.JPEG", "*.JPG");
                    fileChooser.getExtensionFilters().add(extensionFilter);
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        Image createdSprite = new Image(file.getPath());
                        String tmpSpriteName = file.getName();
                        String spriteName = tmpSpriteName.substring(0, tmpSpriteName.lastIndexOf('.'));
                        boolean nameExist = spriteBoxBuilder.checkNameExist(finalSpriteBox, spriteName);
                        if(nameExist == false) {
                            System.out.println(spriteName);
                            ImageView newSpriteImageView = new ImageView();
                            newSpriteImageView.setImage(createdSprite);
                            spriteBox.set(spriteBoxBuilder.addSpriteToBox(spriteName, newSpriteImageView, sceneController,
                                    spriteController, windowBuilder ,finalSpriteBox));

                        }

                    }

                });
                contextMenu.set(new ContextMenu());
                contextMenu.get().getItems().add(uploadSpriteMenuItem);

                spriteBox.get().setOnContextMenuRequested(event -> {
                    contextMenu.get().hide();
                    event.consume();
                });

                spriteBox.get().setOnContextMenuRequested(event -> {
                    contextMenu.get().show(spriteBox.get(), e.getScreenX(), e.getScreenY());
                    event.consume();
                });
                e.consume();
            }
        });


        ScrollPane scrollSpritePane = new ScrollPane();
        scrollSpritePane.setContent(spriteBox.get());
        rightPane.getChildren().add(scrollSpritePane);

        HBox variableBox = new HBox();
        Text variableText = new Text();
        variableText.setText("Variables");
        variableBox.getChildren().add(variableText);
        variableBox.setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");
        variableBox.setPrefSize(400,200);

        ScrollPane variableScrollPane = new ScrollPane();
        variableScrollPane.setContent(variableBox);
        rightPane.getChildren().add(variableScrollPane);

        rightPane.setMargin(scrollSpritePane, new Insets(50,10,0,0));
        rightPane.setMargin(variableScrollPane, new Insets(50,10,0,0));

        return  rightPane;
    }

    /**
     * The bottom pane for the GUI.
     * @param terminalComponent The terminal for the GUI.
     * @return A bottom pane containing a terminal.
     */
    public  HBox buildBottomPane(TerminalComponent terminalComponent) {
        HBox ioBar = new HBox();

        TextArea tmp = new TextArea();
        tmp.setEditable(false);
        tmp.setPrefSize(700, 100);
        tmp.setMinHeight(70);
        tmp.setMaxHeight(70);
        tmp.setMinWidth(Region.USE_COMPUTED_SIZE);
        tmp.setMaxWidth(Region.USE_COMPUTED_SIZE);
        terminalComponent.setTerminal(tmp);
        ioBar.getChildren().add(terminalComponent.getTerminal());
        ioBar.setMargin(terminalComponent.getTerminal(), new Insets(0,0,50,400));
        return ioBar;

    }

    /**
     * Adds the default sprite to the sprite box.
     * @param spriteController the controller for the sprites.
     * @param spriteBox The box for sprites.
     * @return
     */
    private HBox addDefaultSprite(SpriteController spriteController, HBox spriteBox) {

        return spriteBox;
    }


    /**
     * Gets the program box builder.
     * @return The program box builder.
     */
    public ProgramBoxBuilder getProgramBoxBuilder() {
        return programBoxBuilder;
    }

    /**
     * Draws the variable box for the GUI.
     * @param variableManager The variable manager.
     */
    public void drawVariableBox(VariableManager variableManager) {
        StageInitializer.setVariableBox(variableBoxBuilder.drawVariableBox(StageInitializer.getVariableBox(), variableManager));

    }
}
