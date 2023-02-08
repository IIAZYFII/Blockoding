package com.azyf.finalyearproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.opencensus.common.Scope;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    private static final int NO_SPRITE_INDEX = -1;
    private int currentSpriteIndex = NO_SPRITE_INDEX;
    private Canvas canvas;
    private Interpreter interpreter = new Interpreter();
    private TextExtractor textExtractor = new TextExtractor();
    private ImageProcessor imageProcessor = new ImageProcessor();
    private HashMap<String, String> inputBoxesValues = new HashMap<>();
    private ArrayList<String> inputBoxes = new ArrayList<>();
    private HBox variableBox;
    private Image playButtonImg;
    private Image stopButtonImg;
    private Image compileButtonImg;
    private Image variableButtonImg;
    private VariableManager variableManager = new VariableManager();


    private Image OCRButtonImg;
    private Image defaultSprite;
    private SpriteController spriteController = new SpriteController();
    private double currentMouseXPos = 0;
    private double currentMouseYPos = 0;
    private VBox programBox;
    boolean compiled = false;
    private static Scene scene;
    public static Button playButton = new Button();
    public static Button stopButton = new Button();
    public static Timeline frameTimeline;
    private static Queue<Block> emptyLoopBlocks = new LinkedList<>();
    private static   BorderPane root;


    private static KeyCode currentKey;
    private static int frameCounter = 0;



    public StageInitializer() throws FileNotFoundException {
    }

    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {

        frameTimeline = new Timeline(new KeyFrame(Duration.millis(16.67), e -> frame()));
        frameTimeline.setCycleCount(Animation.INDEFINITE);
        Stage stage = event.getStage();
        root = (BorderPane) buildGUI(stage);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);

        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
        scene = new Scene(scrollPane, screenWidth, screenHeight);
        stage.setScene(scene);
        dragSpriteAroundCanvas(scene);
        stage.show();
        root.requestFocus();
        try {
            interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void frame() {
        int tempIndexValue =  interpreter.getInputBoxValueIndex();
        System.out.println(tempIndexValue + " temp index value");
        System.out.println("frame");
        System.out.println(getEmptyLoopBlocks());
        Queue<Block> loopBlocks = new LinkedList<>(emptyLoopBlocks);
        String blockName = "";
        while (interpreter.isTerminated() == false) {
            blockName = loopBlocks.remove().getName();
            interpreter.switchStatement(blockName, loopBlocks, spriteController, variableManager);
        }
        interpreter.setInputBoxValueIndex(tempIndexValue);
        interpreter.setTerminated(false);

        drawScene();
        drawVariableBox();
        frameCounter++;

    }


    /**
     * ALlows the user the drag a sprite around the canvas
     *
     * @param scene
     */
    private void dragSpriteAroundCanvas(Scene scene) {
        AtomicInteger spriteIndex = new AtomicInteger(NO_SPRITE_INDEX);
        canvas.setOnMousePressed(e -> {
            int index = (spriteController.findSprite(e.getX(), e.getY()));
            if (index != NO_SPRITE_INDEX) {
                spriteIndex.set(index);
                if (e.getButton() == MouseButton.SECONDARY) {
                    scene.setCursor(Cursor.CLOSED_HAND);
                }
            }

            e.consume();
        });

        canvas.setOnMouseReleased(e -> {
            if (spriteIndex.get() != NO_SPRITE_INDEX) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    double xPos = e.getX();
                    double yPos = e.getY();
                    System.out.println("canvas height:" + canvas.getHeight());
                    System.out.println("canvas width" + canvas.getWidth());

                    System.out.println("X pos " + xPos);
                    System.out.println("Y pos " + yPos);

                    if (yPos >= 0 && yPos <= canvas.getHeight() && xPos >= 0 && xPos <= canvas.getWidth()) {
                        spriteController.getSprite(spriteIndex.get()).setXPos(xPos);
                        spriteController.getSprite(spriteIndex.get()).setYPos(yPos);
                    }
                    scene.setCursor(Cursor.DEFAULT);
                    spriteIndex.set(NO_SPRITE_INDEX);

                    drawScene();
                } else if (e.getButton() == MouseButton.PRIMARY) {
                    double xPos = e.getX();
                    double yPos = e.getY();
                    if (yPos >= 0 && yPos <= canvas.getHeight() && xPos >= 0 && xPos <= canvas.getWidth()) {
                        spriteController.getSprite(spriteIndex.get()).setClicked(true);
                        System.out.println("set clicked to true");
                    }
                }

            }

            e.consume();
        });
    }

    /**
     * Draws the scene for the canvas.
     */
    private void drawScene() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(255, 253, 208));
        gc.fillRoundRect(0, 0, 728, 597, 20.0, 20.0);
        gc.strokeRoundRect(0, 0, 728, 597, 20.0, 20.0);

        if (spriteController.size() != 0) {
            for (int i = 0; i < spriteController.size(); i++) {
                double xPos = spriteController.getSprite(i).getXPos();
                double yPos = spriteController.getSprite(i).getYPos();
                Image sprite = spriteController.getSprite(i).defaultOutfit();
                gc.drawImage(sprite, xPos, yPos);

            }
            System.out.println("test");
        }

    }


    /**
     * Builds the Pane for the GUI.
     *
     * @param stage The stage which GUI is going to be placed on.
     * @return The pane which contains the GUI.
     */
    private Pane buildGUI(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FF5438; ");


        canvas = new Canvas(728, 597);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255, 253, 208));
        gc.fillRoundRect(0, 0, 728, 597, 20.0, 20.0);
        //gc.setLineWidth(20.0);
        gc.strokeRoundRect(0, 0, 728, 597, 20.0, 20.0);
        canvas.setOnMouseMoved(e -> {
            currentMouseXPos = e.getX();
            currentMouseYPos = e.getY();
        });
        root.setCenter(canvas);


        HBox sceneBox = new HBox();
        sceneBox.setStyle("-fx-background-color: #FFFDD0");
        root.setLeft(sceneBox);


        AnchorPane bottomBar = new AnchorPane();
        root.setBottom(bottomBar);

        VBox ioBar = new VBox();
        TextArea textArea = new TextArea();
        textArea.setPrefSize(700, 100);
        textArea.setMinHeight(Region.USE_COMPUTED_SIZE);
        textArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
        textArea.setMinWidth(Region.USE_COMPUTED_SIZE);
        textArea.setMaxWidth(Region.USE_COMPUTED_SIZE);
        textArea.setStyle("-fx-border-color: black;" + "text-area-background: black;");
        ioBar.setPadding(new Insets(0, 0, 100, 400));
        ioBar.getChildren().add(textArea);

        bottomBar.getChildren().add(ioBar);

        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #FF4122;" + "-fx-border-style: solid inside;");
        root.setTop(topBar);

        VBox rightPane = new VBox();
        HBox spriteBox = new HBox();

        rightPane.setPadding(new Insets(50, 10, 50, 0));
        rightPane.setSpacing(60);
        spriteBox.setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");

        spriteBox.setPadding(new Insets(0, 0, 200, 350));
        String pathDS = getAbsolutePath() + "/Assets/Images/Sprites/default.png";
        defaultSprite = new Image(pathDS);
        ImageView defaultSpriteViewer = new ImageView();
        defaultSpriteViewer.setImage(defaultSprite);



        rightPane.getChildren().add(spriteBox);
        variableBox = new HBox();
        Text variableText = new Text();
        variableText.setText("Variables");
        variableBox.getChildren().add(variableText);
        variableBox.setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");
        variableBox.setPadding(new Insets(0, 0, 200, 350));
        rightPane.getChildren().add(variableBox);
        root.setRight(rightPane);

       VBox leftPane = new VBox();

        programBox = new VBox();
        Text programText = new Text();
        programText.setText("Program Box");
        programBox.setStyle( "-fx-background-color: #FFFDD0;");
        programBox.getChildren().add(programText);
        programBox.setPadding(new Insets(0, 100, 600, 100));
        leftPane.setPadding(new Insets(50, 50, 10, 50));
        leftPane.getChildren().add(programBox);
        root.setLeft(leftPane);


        //playButton.setStyle("-fx-background-color: transparent;");


        //stopButton.setStyle("-fx-background-color: transparent;");


        Button compileButton = new Button();
        String pathCompileButton = getAbsolutePath() + "/Assets/Images/CompileButton.png";
        compileButtonImg = new Image(pathCompileButton);
        ImageView compileButtonView = new ImageView(compileButtonImg);
        compileButtonView.setFitHeight(50);
        compileButtonView.setFitWidth(50);
        compileButton.setGraphic(compileButtonView);

        Button OCRButton = new Button();
        String pathOCRButton = getAbsolutePath() + "/Assets/Images/OCRButton.png";
        OCRButtonImg = new Image(pathOCRButton);
        ImageView OCRButtonView = new ImageView(OCRButtonImg);
        OCRButtonView.setFitHeight(50);
        OCRButtonView.setFitWidth(50);
        OCRButton.setGraphic(OCRButtonView);
        topBar.getChildren().add(OCRButton);
        AtomicReference<String> text = new AtomicReference<>("");
        OCRButton.setOnAction(e -> {
            //textExtractor.setDataPath("C:\\Users\\hussa\\OneDrive\\Desktop\\Tess4J\\tessdata");
            String pathTempIMG = StageInitializer.getAbsolutePath() + "/Cache/img.png";
            File image = new File(pathTempIMG);
            try {
                image = imageProcessor.processImage(image);
                text.set(textExtractor.extractText(image));
                compileButton.setDisable(false);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        topBar.setMargin(OCRButton, new Insets(0, 20, 0, 40));


        compileButton.setDisable(true);
        topBar.getChildren().add(compileButton);

        compileButton.setOnAction(e -> {
            Queue<Block> blocks = interpreter.textToBlocks(text.get());
            compiled = interpreter.checkSyntax(blocks);
            Queue<Block> programBlock = new LinkedList<>(blocks);
            drawProgramBox(programBlock);
            System.out.println("-----------------------------------------");
            if (compiled == true) {
                interpreter.loadBlocks(blocks);
            }
            playButton.setDisable(false);
            e.consume();
        });


        Button variableButton = new Button();
        //String pathVariableButton = getAbsolutePath() + "/Assets/Images/CompileButton.png";
        //variableButtonImg = new Image(pathCompileButton);
        //ImageView variableButtonView = new ImageView(compileButtonImg);
        //comButtonView.setFitHeight(50);
        //compileButtonView.setFitWidth(50);
        //compileButton.setGraphic(compileButtonView);

        variableButton.setOnAction(e-> {
            drawVariableManager();
            e.consume();
        });

        topBar.getChildren().add(variableButton);



        AtomicReference<ContextMenu> contextMenu = new AtomicReference<>(new ContextMenu());
        spriteBox.setOnMouseClicked(e -> {
            if (contextMenu.get() != null) {
                contextMenu.get().hide();
                contextMenu.set(null);
            }
            if (e.getButton() == MouseButton.SECONDARY) {

                MenuItem menuItem1 = new MenuItem("Upload Sprite");
                menuItem1.setOnAction(actionEvent -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Upload Sprite");
                    FileChooser.ExtensionFilter extFilter =
                            new FileChooser.ExtensionFilter("Image files (*.PNG, *.JPEG, *.JPG, )", "*.PNG", "*.JPEG", "*.JPG");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        Image createdSprite = new Image(file.getPath());
                        String tmpSpriteName = file.getName();
                        String spriteName = tmpSpriteName.substring(0, tmpSpriteName.lastIndexOf('.'));
                        boolean nameExist = checkNameExist(spriteBox, spriteName);
                        if(nameExist == false) {
                            System.out.println(spriteName);
                            ImageView imageView = new ImageView();
                            imageView.setImage(createdSprite);
                            VBox tmpSpriteContainer = createSpriteContainer(spriteName, imageView);
                            spriteBox.getChildren().add(tmpSpriteContainer);

                            dragAndDrop(imageView, createdSprite, spriteName);
                        }

                    }

                });
                contextMenu.set(new ContextMenu());
                contextMenu.get().getItems().add(menuItem1);

                spriteBox.setOnContextMenuRequested(event -> {
                    contextMenu.get().hide();
                    event.consume();
                });

                spriteBox.setOnContextMenuRequested(event -> {
                    contextMenu.get().show(spriteBox, e.getScreenX(), e.getScreenY());
                    event.consume();
                });
                e.consume();
            }
        });

        playButton.setDisable(true);
        String pathPlayButton = getAbsolutePath() + "/Assets/Images/PlayButton.png";
        playButtonImg = new Image(pathPlayButton);
        ImageView playButtonView = new ImageView(playButtonImg);
        playButton.setGraphic(playButtonView);
        topBar.getChildren().add(playButton);
        topBar.setMargin(playButton, new Insets(0, 0, 0, 700));


        playButton.setOnAction(e -> {
            playButton.setDisable(true);
            stopButton.setDisable(false);
           // System.out.println("Pre " + variableManager.getVariables().get(0).getValue());
            if (compiled == true) {
                interpreter.compileAndRun(spriteController, currentMouseXPos, currentMouseYPos, inputBoxesValues,
                        inputBoxes, variableManager);
                drawScene();
                drawVariableBox();
               // System.out.println("Post " + variableManager.getVariables().get(0).getValue());

            }

        });

        stopButton.setDisable(true);
        String pathStopButton = getAbsolutePath() + "/Assets/Images/StopButton.png";
        stopButtonImg = new Image(pathStopButton);
        ImageView stopButtonView = new ImageView(stopButtonImg);
        stopButton.setGraphic(stopButtonView);
        topBar.getChildren().add(stopButton);

        stopButton.setOnAction(e -> {
            frameTimeline.stop();
            variableManager.resetToInitialValues();
            drawVariableBox();
            playButton.setDisable(false);
            stopButton.setDisable(true);
        });

        Button settingButton = new Button();
        String pathSettingButton = getAbsolutePath() + "/Assets/Images/SettingsButton.png";
        Image settingButtonImg = new Image(pathSettingButton);
        ImageView settingButtonView = new ImageView(settingButtonImg);
        settingButtonView.setFitHeight(50);
        settingButtonView.setFitWidth(50);
        settingButton.setGraphic(settingButtonView);
        topBar.getChildren().add(settingButton);
        topBar.setMargin(settingButton, new Insets(0, 20, 0, 830));

        settingButton.setOnAction(e -> {
            drawSettings();
        });

        VBox spriteContainer = createSpriteContainer("default", defaultSpriteViewer);
        Label spriteLabelName = (Label) spriteContainer.getChildren().get(1);
        String spriteName = spriteLabelName.getText();
        spriteBox.getChildren().add(spriteContainer);
        dragAndDrop(defaultSpriteViewer, defaultSprite, spriteName);
        return root;

    }

    private void drawProgramBox(Queue<Block> blocks) {
        programBox.getChildren().clear();
        Text programText = new Text(spriteController.getSprite(currentSpriteIndex).getSpriteName() + " Program Box");
        programBox.setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");
        programBox.getChildren().add(programText);

        //Rectangle rect = new Rectangle(30,40);
        //programBox.getChildren().add(rect);


        while (blocks.size() > 0) {
            Block block = blocks.remove();
            String blockName = block.getName();
            StackPane stackPane = new StackPane();
            HBox hBox = null;
            String secondBlockName = "";
            String thirdBlockName = "";
            switch (blockName) {
                case "START":
                case "END":
                    stackPane = (StackPane) drawBlock(blockName, 0, 255, 0);
                    programBox.getChildren().add(stackPane);
                    break;
                case "ROTATE":
                case "MOVE":
                    blocks.remove();
                    block = blocks.remove();
                    blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    break;

                case "CONDITION":
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    break;
                case "FLIP":
                    blocks.remove();
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    break;
                case "PAUSE":
                    block = blocks.remove();
                    hBox = (HBox) drawBlock(blockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    break;
                case "TELPORT":
                    blockName = "TELPORT";
                    blocks.remove();
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();


                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    if (secondBlockName.equals("X")) {
                        blocks.remove();
                    }
                    break;
                case "WHENEVER":
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    if (secondBlockName.equals("PRESSES")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("HOVERS")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("CLICKS")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("NOT")) {
                        stackPane = (StackPane) drawBlock(secondBlockName, 192, 240, 22);
                        secondBlockName = blocks.remove().getName();
                        if (secondBlockName.equals("PRESSES") ||(secondBlockName.equals("CLICKS")
                                || secondBlockName.equals("HOVERS"))) {
                            blocks.remove();
                        }
                        thirdBlockName = blocks.remove().getName();
                        hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 192, 240, 22);
                        hBox.getChildren().add(1,stackPane);
                        programBox.getChildren().add(hBox);
                        break;
                    }
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 95, 31);
                    programBox.getChildren().add(hBox);
                    break;
                case "ELSE":
                case "LOOP":
                case "TERMINATE":
                    stackPane = (StackPane) drawBlock(blockName, 192, 240, 22);
                    programBox.getChildren().add(stackPane);
                    break;
                case "HOVERS":
                case "PRESSES":
                case "CLICKS":
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 192, 240, 22);
                    programBox.getChildren().add(hBox);
                    break;
                case "SET":
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 255, 255);
                    programBox.getChildren().add(hBox);
                    break;
                case "VARIABLE":
                    stackPane = createStackPane(blocks.remove().getName(),255,255,255);
                    secondBlockName = blocks.remove().getName();
                    StackPane equalsStackPane = createStackPane(blocks.remove().getName(),255,255,255);
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 255, 255);
                    hBox.getChildren().add(1,stackPane);
                    hBox.getChildren().add(3,equalsStackPane);
                    programBox.getChildren().add(hBox);
                    break;
                case "SPEAK":
                    secondBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 19, 3, 252);
                    programBox.getChildren().add(hBox);
                    break;
                default:
                    System.out.println("test");
                    break;


            }
        }
    }

    private Node drawBlock(String blockName, int red, int green, int blue) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        if (blockName.equals("PAUSE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            TextField textField = createTextField();
            hBox.getChildren().add(textField);

            return hBox;
        }
        return stackPane;
    }

    private Node drawBlock(String blockName, String secondBlockName, int red, int green, int blue) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        if (blockName.equals("ROTATE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);


            String dropDown[] = {"90", "180", "270"};
            comboBox = createComboBox(dropDown);
            hBox.getChildren().add(comboBox);


            return hBox;
        } else if (blockName.equals("MOVE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);


            ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

          TextField textField = createTextField();
            hBox.getChildren().add(textField);
            return hBox;

        } else if (blockName.equals("TELPORT")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            stackPane = createStackPane("TO", red, green, blue);
            hBox.getChildren().add(stackPane);
            ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);
            if (secondBlockName.equals("X")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                TextField textField = createTextField();
                hBox.getChildren().add(textField);

                stackPane = createStackPane("Y", red, green, blue);
                hBox.getChildren().add(stackPane);
                textField = createTextField();
                hBox.getChildren().add(textField);

                return hBox;
            } else if (secondBlockName.equals("MOUSE")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                return hBox;
            } else if (secondBlockName.equals("SPRITE")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                String options[] = {"no options"};
                comboBox = new ComboBox(FXCollections.observableArrayList(options));
                hBox.getChildren().add(comboBox);
                return hBox;
            } else if (secondBlockName.equals("RANDOM")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                return hBox;
            }

        } else if (blockName.equals("FLIP") ) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);
            return hBox;

        } else if(blockName.equals("CONDITION")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);
            return hBox;

        }else if (blockName.equals("PRESSES")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                    "UP", "DOWN", "LEFT", "RIGHT", "SPACE"};
            ComboBox comboBox = createComboBox(keys);
            hBox.getChildren().add(comboBox);
            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("CLICKS") || blockName.equals("HOVERS")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("SPEAK")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            TextField textField = createTextField();
            hBox.getChildren().add(textField);
            return hBox;

        }
        return stackPane;
    }

    private Node drawBlock(String blockName, String secondBlockName, String thirdBlockName, int red, int green, int blue) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        HBox hBox = null;
        if (blockName.equals("WHENEVER")) {
            if (secondBlockName.equals("PRESSES")) {
                hBox = new HBox();
                hBox.getChildren().add(stackPane);
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                        "UP", "DOWN", "LEFT", "RIGHT", "SPACE"};
                ComboBox comboBox = createComboBox(keys);
                hBox.getChildren().add(comboBox);
            } else if (secondBlockName.equals("HOVERS") || secondBlockName.equals("CLICKS")) {
                hBox = new HBox();
                hBox.getChildren().add(stackPane);
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                ComboBox comboBox = createComboBox(spriteController.getSpriteNameAsArray());
                hBox.getChildren().add(comboBox);

            }

            stackPane = createStackPane(thirdBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("SET")) {
            hBox = new HBox();
            ComboBox comboBox = createComboBox(variableManager.getVariableNamesAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            if(thirdBlockName.equals("NUMBER")) {
                TextField textField = createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }

            return hBox;


        } else if(blockName.equals("VARIABLE")) {
            hBox = new HBox();
            ComboBox comboBox = createComboBox(variableManager.getVariableNamesAsArray());
            hBox.getChildren().add(comboBox);

            if(secondBlockName.equals("NUMBER")) {
                TextField textField = createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }

            if(thirdBlockName.equals("NUMBER")) {
                TextField textField = createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }
            return hBox;
        }

        return stackPane;
    }




    private StackPane createStackPane(String blockName, int red, int green, int blue) {
        StackPane stackPane = new StackPane();
        Rectangle blockBox = new Rectangle(70, 30);
        blockBox.setFill(Color.rgb(red, green, blue));
        Rectangle blockBorder = new Rectangle(80, 40);

        Label blockText = new Label(blockName);
        if(blockName.equals("SPEAK")) {
            blockText.setTextFill(Color.color(1,1,1));
        }

        stackPane.getChildren().add(blockBorder);
        stackPane.getChildren().add(blockBox);
        stackPane.getChildren().add(blockText);
        blockText.setFont(new Font("Arial", 15));
        return stackPane;
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        String textFieldAsString = textField.toString();
        textField.setOnAction(e -> {
            if (getInputBoxIndex(textFieldAsString) == -1) {
                System.out.println(textField.getText());
                inputBoxesValues.put(textFieldAsString, (String) textField.getText());
                inputBoxes.add(textFieldAsString);
            } else {
                inputBoxesValues.remove(textFieldAsString);
                inputBoxesValues.put(textFieldAsString, (String) textField.getText());
            }
        });
        return textField;
    }

    private ComboBox createComboBox(String[] dropDown) {
        ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(dropDown));
        String comboBoxAsString = comboBox.toString();
        comboBox.setOnAction(e -> {
            if (getInputBoxIndex(comboBoxAsString) == -1) {
                System.out.println(comboBox.getValue());
                inputBoxesValues.put(comboBoxAsString, (String) comboBox.getValue());
                inputBoxes.add(comboBoxAsString);
            } else {
                inputBoxesValues.remove(comboBoxAsString);
                inputBoxesValues.put(comboBoxAsString, (String) comboBox.getValue());
            }

        });
        return comboBox;
    }


    private void drawSettings() {
        Stage settingStage = new Stage();
        HBox hBox = new HBox();
        BorderPane setttingsRoot = new BorderPane();
        setttingsRoot.setStyle("-fx-background-color: #FF5438;");
        Button linkAppButton = new Button();
        linkAppButton.setMinSize(100, 100);
        linkAppButton.setText("Link App");
        linkAppButton.setOnAction(e -> {
            try {
                generateQRCode();
                hBox.getChildren().clear();
                Image qrCode = new Image("Cache\\qrcode.png");
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
        Scene scene = new Scene(setttingsRoot, 350, 350);
        settingStage.setScene(scene);
        settingStage.showAndWait();
    }

    private void drawVariableManager(){
        Stage variableManagerStage = new Stage();
        variableManagerStage.setResizable(false);
        BorderPane variableManagerRoot = drawBaseVariableManager(variableManagerStage);


        Scene scene = new Scene(variableManagerRoot, 350, 350);
        variableManagerStage.setScene(scene);
        variableManagerStage.showAndWait();
    }

    private BorderPane drawBaseVariableManager(Stage variableManagerStage) {
        AtomicReference<BorderPane> variableManagerRoot  = new AtomicReference<>(new BorderPane());
        variableManagerRoot.get().setStyle("-fx-background-color: #FF5438;");

        VBox content = new VBox();
        Button create = new Button();
        create.setText("Create Variable");
        create.setMinSize(200,50);
        create.setOnAction(e-> {
            content.getChildren().clear();
            Label variableName = new Label();
            variableName.setText("Name of Variable");
            variableName.setMinSize(100,50);
            content.getChildren().add(variableName);


            TextField enterVariableName = new TextField();
            enterVariableName.setMinSize(200,50);
            content.getChildren().add(enterVariableName);

            Label initialValue = new Label();
            initialValue.setText("initial Value");
            content.getChildren().add(initialValue);

            TextField enterInitialValue = new TextField();
            enterInitialValue.setMinSize(200,50);
            content.getChildren().add(enterInitialValue);


            HBox buttonsBox = new HBox();
            Button cancelButton = new Button();
            cancelButton.setText("Cancel");
            cancelButton.setMinSize(75,25);
            cancelButton.setOnAction(event -> {
                event.consume();
            });
            buttonsBox.getChildren().add(cancelButton);

            Button confirmButton = new Button();
            confirmButton.setText("OK");

            confirmButton.setOnAction(event -> {
                   String name =  enterVariableName.getText();
                    int variableValue;
                   try {
                       variableValue = Integer.parseInt(enterInitialValue.getText());
                       Variable variable = new Variable(variableValue, name);
                       System.out.println("Added Variable");
                       boolean alreadyExist = variableManager.addVariable(variable);
                       if(alreadyExist == false) {
                           addVariableToCanvas(variable);
                       }
                   } catch (Exception ex) {
                       throw new IllegalArgumentException("Expected a number");
                   } finally {
                       variableManagerStage.close();
                   }


            });
            confirmButton.setMinSize(50,25);


            buttonsBox.getChildren().add(confirmButton);

            content.getChildren().add(buttonsBox);


            e.consume();
        });
        content.getChildren().add(create);
        content.setPadding(new Insets(0,0,0,0));
        variableManagerRoot.get().getChildren().add(content);
        return variableManagerRoot.get();
    }

    private void generateQRCode() throws WriterException, IOException {
        String localIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println(localIP);
        QRCodeWriter qrCodeGenerator = new QRCodeWriter();
        BitMatrix bitMatrix =
                qrCodeGenerator.encode(localIP, BarcodeFormat.QR_CODE, 100, 100);
        WritableImage writableImage = new WritableImage(100, 100);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        System.out.println(bitMatrix.toString());
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                if (bitMatrix.get(i, j) == true) {
                    pixelWriter.setColor(i, j, Color.color(0.0, 0.0, 0.0));
                } else {
                    pixelWriter.setColor(i, j, Color.color(1.0, 1.0, 1.0));
                }

            }
        }
        File saveImage = new File("Cache\\qrcode.png");
        BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(image, "png", saveImage);
    }

    /**
     * Allows the User to change the label text.
     *
     * @param e               The current Event.
     * @param spriteContainer The Container which contains the sprite label.
     */
    private void clickOnLabel(Event e, VBox spriteContainer) {
        System.out.println("Detected Click");
        Label currentLabel = (Label) spriteContainer.getChildren().get(1);

        TextField enterSpriteNameField = new TextField(currentLabel.getText());
        enterSpriteNameField.setPrefSize(50, 20);
        enterSpriteNameField.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            String inputText = enterSpriteNameField.getText();
            if (event.getCode() == KeyCode.ENTER && !(inputText.equals(""))) {
                System.out.println("detected enter");

                Label newSpriteLabel = new Label(inputText);
                String oldName = currentLabel.getText();
                System.out.println(oldName);

                boolean changedName =
                        spriteController.changeSpriteName(inputText, oldName);
                if(changedName == true) {
                    spriteContainer.getChildren().remove(1);
                    spriteContainer.getChildren().add(newSpriteLabel);
                    newSpriteLabel.setOnMouseClicked(labelEvent -> clickOnLabel(labelEvent, spriteContainer));

                }


            }
            event.consume();
        });
        spriteContainer.getChildren().remove(1);
        spriteContainer.getChildren().add(enterSpriteNameField);
    }


    /**
     * Allows the user to drag a sprite from the sprite box to the canvas.
     *
     * @param imageView  The imageview of the sprite.
     * @param sprite     The image of the sprite.
     * @param spriteName The name of the sprite.
     */
    private void dragAndDrop(ImageView imageView, Image sprite, String spriteName) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);
                event.consume();

                canvas.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        if (event.getGestureSource() == imageView) {
                            event.acceptTransferModes(TransferMode.ANY);
                            event.consume();
                        }
                    }
                });
                canvas.setOnDragDropped(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        double x = event.getX() - sprite.getWidth() / 2.0;
                        double y = event.getY() - sprite.getHeight() / 2.0;

                        String s = String.format("You dropped at (%f, %f) relative to the canvas.", x, y);
                        System.out.println(s);
                        GraphicsContext gc = canvas.getGraphicsContext2D();

                         spriteController.addSprite(spriteName, x, y, sprite);
                            System.out.println("added sprite");
                            currentSpriteIndex = spriteController.size() - 1;
                            gc.drawImage(sprite, x, y);

                            imageView.setOnDragDetected(null);
                            programBox.getChildren().clear();
                            Text programText = new Text();
                            programText.setText(spriteName + "'s " + "Program Box");
                            programBox.getChildren().add(programText);


                        event.consume();
                    }
                });


            }

        });

    }

    private int getInputBoxIndex(String inputBox) {
        for (int i = 0; i < inputBoxes.size(); i++) {
            if (inputBoxes.get(i).equals(inputBox)) {
                return i;
            }
        }
        return -1;
    }

    public static KeyCode getCurrentKey() {

       root.setOnKeyPressed(e -> {
            KeyCode keyCode = e.getCode();
            currentKey = keyCode;

        });
        System.out.println(currentKey + " Is being pressed");
        return currentKey;
    }

    public static void setCurrentKey(KeyCode currentKey) {
        StageInitializer.currentKey = currentKey;
    }

    public static double[] getMousePosition() {
        double x = scene.getX();
        double y = scene.getY();
        double[] mousePosition = {x, y};
        return mousePosition;
    }


    public static Queue<Block> getEmptyLoopBlocks() {
        return emptyLoopBlocks;
    }

    public static void setEmptyLoopBlocks(Queue<Block> emptyLoopBlocks) {
        StageInitializer.emptyLoopBlocks =   new LinkedList<>(emptyLoopBlocks);
    }

    private VBox createSpriteContainer(String spriteName, ImageView spriteViewer) {
        VBox spriteContainer = new VBox();
        Label spriteLabel = new Label(spriteName);

        spriteLabel.setOnMouseClicked(e -> clickOnLabel(e, spriteContainer));


        spriteContainer.getChildren().add(spriteViewer);
        spriteContainer.getChildren().add(spriteLabel);
        spriteContainer.setAlignment(Pos.CENTER);




        return spriteContainer;
    }


    public static String getAbsolutePath() {
        File path = new File("");
        String systemPath = path.getAbsolutePath();
        System.out.println(systemPath);
        return systemPath;
    }

    private void drawVariableBox() {
        variableBox.getChildren().clear();
        for(int i = 0; i < variableManager.getVariables().size(); i++) {
            addVariableToCanvas(variableManager.getVariables().get(i));
        }
    }

    private void addVariableToCanvas(Variable variable) {
        String content = (variable.getName() +  " : " + variable.getValue());
        StackPane stackPane =  createStackPane(content, 255, 84, 56);
        variableBox.getChildren().add(stackPane);

    }

    private boolean checkNameExist(HBox spriteBox, String spriteName) {
        boolean alreadyExist = false;
        for (int i =0; i < spriteBox.getChildren().size(); i++) {
          VBox tmpSpriteContainer = (VBox) spriteBox.getChildren().get(i);
           Label tmpSpriteName = (Label) tmpSpriteContainer.getChildren().get(1);
           if(tmpSpriteName.getText().equals(spriteName)) {
               alreadyExist = true;
           }

        }
        return alreadyExist;
    }
}
