package com.azyf.finalyearproject;

import com.google.api.client.http.FileContent;
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
import java.awt.image.RenderedImage;
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
    private static int currentSpriteIndex = NO_SPRITE_INDEX;
    private static Canvas canvas;
    private Interpreter interpreter = new Interpreter();
    private TextExtractor textExtractor = new TextExtractor();
    private ImageProcessor imageProcessor = new ImageProcessor();

    private HBox variableBox;
    private VariableManager variableManager = new VariableManager();
    private static TerminalComponent terminal;


    private SpriteController spriteController = new SpriteController();

    private SoundController soundController = new SoundController();
    private static double currentMouseXPos = 0;
    private static double currentMouseYPos = 0;
    private static VBox leftPanel;
    private static VBox rightPanel;
    private static boolean compiled = false;
    private static Scene scene;
    private GUIBuilder builder;
    public static Timeline frameTimeline;
    private static Queue<Block> emptyLoopBlocks = new LinkedList<>();
    private static   BorderPane root;

    private SceneController sceneController  = new SceneController();

    private static Image sceneBackground;





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


        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
        scene = new Scene(root, screenWidth, screenHeight);
        stage.setMaximized(true);
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
            interpreter.switchStatement(blockName, loopBlocks, spriteController, variableManager, soundController, sceneController);
        }
        interpreter.setInputBoxValueIndex(tempIndexValue);
        interpreter.setTerminated(false);

        drawScene(sceneController, spriteController);
        builder.drawVariableBox(variableManager);
        frameCounter++;

    }


    /**
     * ALlows the user the drag a sprite around the canvas
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

                    drawScene(sceneController, spriteController);
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
    public static void drawScene(SceneController sceneController, SpriteController spriteController) {
        if(sceneController.getChangeSceneTo() != null && !(sceneController.getChangeSceneTo().equals("Default"))) {
            for(int i =0; i < sceneController.getScenes().size(); i++) {
                if(sceneController.getScene(i).getName().equals(sceneController.getChangeSceneTo())) {
                    sceneBackground = sceneController.getScene(i).getImage();
                }
            }
           sceneController.setChangeSceneTo("Default");
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(255, 253, 208));
        gc.fillRoundRect(0, 0, 728, 597, 20.0, 20.0);
        gc.strokeRoundRect(0, 0, 728, 597, 20.0, 20.0);
        if(sceneBackground != null) {
            gc.drawImage(sceneBackground,0,0);
        }


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
 *
 */
    /**
     * Builds the Pane for the GUI.
     *
     * @param stage The stage which GUI is going to be placed on.
     * @return The pane which contains the GUI.
     */
    private Pane buildGUI(Stage stage) {

        GUIBuilder builder = new GUIBuilder();
       BorderPane root = new BorderPane();
       root.setStyle("-fx-background-color: #FF5438; ");



        canvas = new Canvas(728, 597);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255, 253, 208));
        gc.fillRoundRect(0, 0, 728, 597, 20.0, 20.0);

        gc.strokeRoundRect(0, 0, 728, 597, 20.0, 20.0);
        canvas.setOnMouseMoved(e -> {
            currentMouseXPos = e.getX();
            currentMouseYPos = e.getY();
        });
        canvas.setOnMouseClicked(e-> {
            if(e.getButton() ==MouseButton.SECONDARY) {
                System.out.println("second mouse clicked");
            }
        });


        HBox topBar = builder.createTopBar(imageProcessor, textExtractor, interpreter, variableManager,
                soundController, spriteController, sceneController);
        root.setTop(topBar);

        leftPanel = builder.buildLeftPane();
        root.setLeft(leftPanel);

        HBox bottomPanel = builder.buildBottomPane(terminal);
        root.setBottom(bottomPanel);

        rightPanel = builder.buildRightPane(sceneController, spriteController, stage);
        root.setRight(rightPanel);

        root.setCenter(canvas);
        return  root;
    }

    /**
     * Allows the user to drag a sprite from the sprite box to the canvas.
     *  @param imageView  The imageview of the sprite.
     * @param sprite     The image of the sprite.
     * @param spriteName The name of the sprite.
     * @param spriteController
     */
    public static void dragAndDrop(ImageView imageView, Image sprite, String spriteName, SpriteController spriteController) {
        Image finalSprite = sprite;
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
                        double x = event.getX() - finalSprite.getWidth() / 2.0;
                        double y = event.getY() - finalSprite.getHeight() / 2.0;

                        String s = String.format("You dropped at (%f, %f) relative to the canvas.", x, y);
                        System.out.println(s);
                        GraphicsContext gc = canvas.getGraphicsContext2D();


                          spriteController.addSprite(spriteName, x, y, finalSprite);
                            System.out.println("added sprite");
                            currentSpriteIndex = spriteController.size() - 1;
                            gc.drawImage(finalSprite, x, y);

                            imageView.setOnDragDetected(null);

                        event.consume();
                    }
                });


            }

        });

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


    public static void setSceneBackground(Image scene) {
        sceneBackground = scene;

    }


    public static boolean getCompiled() {
        return compiled;
    }

    public static void setCompiled(boolean compile) {
        compiled = compile;
    }

    public static void setLeftPanel(VBox leftPane) {
        leftPanel = leftPane;
        root.setLeft(leftPanel);
    }

    public static HBox getVariableBox() {
       ScrollPane scrollPane = (ScrollPane) rightPanel.getChildren().get(1);
       return (HBox) scrollPane.getContent();
    }

    public static void setVariableBox(HBox variableBox){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(variableBox);
        rightPanel.getChildren().set(1, scrollPane);
        root.setRight(rightPanel);
    }

    public static double getCurrentMouseXPos() {
        return currentMouseXPos;
    }

    public static double getCurrentMouseYPos() {
        return currentMouseYPos;
    }
}
