package com.azyf.finalyearproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    private static final int NO_SPRITE_INDEX = -1;
    private Canvas canvas;
    private Interpreter interpreter = new Interpreter();
    private TextExtractor textExtractor = new TextExtractor();
    private ImageProcessor imageProcessor = new ImageProcessor();
    private Canvas blockCanvas;
    private Image playButtonImg;
    private Image stopButtonImg;
    private Image defaultSprite;
    private ImageView defaultSpriteViewer = new ImageView();
    private SpriteController spriteController = new SpriteController();

    public StageInitializer() throws FileNotFoundException {
    }

    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {
        Stage stage = event.getStage();
        BorderPane root = (BorderPane) buildGUI(stage);
        
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        dragSpriteAroundCanvas(scene);
        stage.show();
        try {
            interpreter.loadTree(new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Blocks\\parseTree.txt"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    /**
     * ALlows the user the drag a sprite around the canvas
     * @param scene
     */
    private void dragSpriteAroundCanvas(Scene scene) {
        AtomicInteger spriteIndex = new AtomicInteger(NO_SPRITE_INDEX);
        canvas.setOnMousePressed( e -> {

            int index = (spriteController.findSprite(e.getX(), e.getY()));
            if(index != NO_SPRITE_INDEX) {
                spriteIndex.set(index);
                scene.setCursor(Cursor.CLOSED_HAND);
            }
            e.consume();
        });

        canvas.setOnMouseReleased(e -> {
            if(spriteIndex.get() != NO_SPRITE_INDEX) {
                double xPos = e.getX();
                double yPos = e.getY();
                System.out.println("canvas height:" + canvas.getHeight());
                System.out.println("canvas width" + canvas.getWidth());

                System.out.println("X pos " + xPos);
                System.out.println("Y pos " + yPos);

                if(yPos >= 0 && yPos <= canvas.getHeight() && xPos >= 0 && xPos <= canvas.getWidth()) {
                    spriteController.getSprite(spriteIndex.get()).setXPos(xPos);
                    spriteController.getSprite(spriteIndex.get()).setYPos(yPos);
                }
                scene.setCursor(Cursor.DEFAULT);
                spriteIndex.set(NO_SPRITE_INDEX);


            }
            drawScene();
            e.consume();
        });
    }

    /**
     * Draws the scene for the canvas.
     */
    private void drawScene() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(255,253,208));
        gc.fillRoundRect(0,0,728,597,20.0,20.0);
        gc.strokeRoundRect(0,0,728,597,20.0,20.0);

        if(spriteController.size() != 0 ) {
            for(int i = 0; i < spriteController.size(); i ++) {
                double xPos  = spriteController.getSprite(i).getXPos();
                double yPos  = spriteController.getSprite(i).getYPos();
                Image sprite =  spriteController.getSprite(i).defaultOutfit();
                gc.drawImage(sprite, xPos , yPos);

            }
        }

    }


    /**
     * Builds the Pane for the GUI.
     * @param stage The stage which GUI is going to be placed on.
     * @return The pane which contains the GUI.
     */
    private Pane buildGUI(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FF5438; ");


        canvas = new Canvas(728,597);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255,253,208));
        gc.fillRoundRect(0,0,728,597,20.0,20.0);
        //gc.setLineWidth(20.0);
        gc.strokeRoundRect(0,0,728,597,20.0,20.0);
        root.setCenter(canvas);


        HBox sceneBox = new HBox();
        sceneBox.setStyle("-fx-background-color: #FFFDD0");
        root.setLeft(sceneBox);



        AnchorPane bottomBar = new AnchorPane();
        root.setBottom(bottomBar);

        VBox ioBar = new VBox();
        TextArea textArea = new TextArea();
        textArea.setPrefSize(700,100);
        textArea.setMinHeight(Region.USE_COMPUTED_SIZE);
        textArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
        textArea.setMinWidth(Region.USE_COMPUTED_SIZE);
        textArea.setMaxWidth(Region.USE_COMPUTED_SIZE);
        textArea.setStyle("-fx-border-color: black;" + "text-area-background: black;" );
        ioBar.setPadding(new Insets(0,0,100,400));
        ioBar.getChildren().add(textArea);

        bottomBar.getChildren().add(ioBar);

        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #FF4122;" + "-fx-border-style: solid inside;");
        root.setTop(topBar);

        VBox rightPane = new VBox();
        HBox spriteBox = new HBox();

        rightPane.setPadding(new Insets(50,10,50,0));
        rightPane.setSpacing(60);
        spriteBox.setStyle("-fx-border-style: solid inside;" +  "-fx-background-color: #FFFDD0;" );

        spriteBox.setPadding(new Insets(0, 0, 200, 350));
        defaultSprite = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\Sprites\\default-sprite.png");
        defaultSpriteViewer.setImage(defaultSprite);
        VBox spriteContainer = new VBox();
        Label spriteLabel = new Label("Default Sprite");



        spriteLabel.setOnMouseClicked(e -> clickOnLabel(e, spriteContainer));


        spriteContainer.getChildren().add(defaultSpriteViewer);
        spriteContainer.getChildren().add(spriteLabel);
        spriteContainer.setAlignment(Pos.CENTER);



        spriteBox.getChildren().add(spriteContainer);






        rightPane.getChildren().add(spriteBox);
        HBox stageBox = new HBox();
        Text stageText = new Text();
        stageText.setText("stage");
        stageBox.getChildren().add(stageText);
        stageBox.setStyle("-fx-border-style: solid inside;" +  "-fx-background-color: #FFFDD0;");
        stageBox.setPadding(new Insets(0, 0, 200, 350));
        rightPane.getChildren().add(stageBox);
        root.setRight(rightPane);

        VBox leftPane = new VBox();
        HBox programBox = new HBox();
        Text programText = new Text();
        programText.setText("Program Box");
        programBox.setStyle("-fx-border-style: solid inside;" +  "-fx-background-color: #FFFDD0;");
        programBox.getChildren().add(programText);
        programBox.setPadding(new Insets(0,100,600,100));
        leftPane.setPadding(new Insets(50,50,10,50));
        leftPane.getChildren().add(programBox);
        root.setLeft(leftPane);




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
            //textExtractor.setDataPath("C:\\Users\\hussa\\OneDrive\\Desktop\\Tess4J\\tessdata");
            File image =   new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\img.png");
            try {
                image = imageProcessor.processImage(image);
                String text = textExtractor.extractText(image);
                Queue<Block> blocks =  interpreter.textToBlocks(text);
                interpreter.compile(blocks);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        Button settingButton = new Button();
        Image settingButtonImg = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\SettingsButton.png");
        // ImageView settingButtonView = new ImageView(settingButtonImg);
        //settingButton.setGraphic(settingButtonView);
        topBar.getChildren().add(settingButton);

        settingButton.setOnAction(e -> {
            drawSettings();
        });

        AtomicReference<ContextMenu> contextMenu = new AtomicReference<>(new ContextMenu());
        spriteBox.setOnMouseClicked(e -> {
            if (contextMenu.get() !=null) {
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
                        ImageView imageView = new ImageView();
                        imageView.setImage(createdSprite);
                        spriteBox.getChildren().add(imageView);
                        //dragAndDrop(imageView, createdSprite, programBox);
                    }

                });
                contextMenu.set(new ContextMenu());
                contextMenu.get().getItems().add(menuItem1);

                spriteBox.setOnContextMenuRequested(event-> {
                    contextMenu.get().hide();
                    event.consume();
                });

                spriteBox.setOnContextMenuRequested(event-> {
                    contextMenu.get().show(spriteBox, e.getScreenX(), e.getScreenY());
                    event.consume();
                });
                e.consume();
            }
        });


        Label spriteLabelName = (Label) spriteContainer.getChildren().get(1);
        String spriteName = spriteLabelName.getText();
        dragAndDrop(defaultSpriteViewer, defaultSprite, programBox, spriteName);
        return  root;

    }

    private void changeSpriteName() {

    }





    private void drawSettings() {
        Stage settingStage = new Stage();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FF5438;");
        Button linkAppButton = new Button();
        linkAppButton.setMinSize(100,100);
        linkAppButton.setText("Link App");
        linkAppButton.setOnAction(e -> {
            try {
                generateQRCode();
                root.getChildren().clear();
                Image qrCode = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache");
                ImageView qrCodeViewer = new ImageView(qrCode);
                root.getChildren().add(qrCodeViewer);

            } catch (WriterException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        HBox hBox = new HBox();
        hBox.getChildren().add(linkAppButton);
        root.getChildren().add(hBox);
        Scene scene = new Scene(root, 350,350);
        settingStage.setScene(scene);
        settingStage.showAndWait();
    }

    private void generateQRCode() throws WriterException, IOException {
        String localIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println(localIP);
        QRCodeWriter qrCodeGenerator = new QRCodeWriter();
        BitMatrix bitMatrix =
                qrCodeGenerator.encode(localIP, BarcodeFormat.QR_CODE,100,100);
        WritableImage writableImage = new WritableImage(100,100);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        System.out.println(bitMatrix.toString());
        for(int j = 0; j < 100; j++) {
            for(int i = 0; i < 100; i++) {
                if(bitMatrix.get(i,j) == true) {
                    pixelWriter.setColor(i,j, Color.color(0.0,0.0,0.0));
                } else {
                    pixelWriter.setColor(i,j, Color.color(1.0,1.0,1.0));
                }

            }
        }
        File saveImage = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Cache\\qrcode.png");
        BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(image, "png", saveImage);
    }

    /**
     * Allows the User to change the label text.
     * @param e The current Event.
     * @param spriteContainer The Container which contains the sprite label.
     */
    private void clickOnLabel(Event e, VBox spriteContainer) {
        System.out.println("Detected Click");
        Label currentLabel  = (Label) spriteContainer.getChildren().get(1);

        TextField enterSpriteNameField = new TextField(currentLabel.getText());
        enterSpriteNameField.setPrefSize(50,20);
        enterSpriteNameField.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            String inputText = enterSpriteNameField.getText();
            if (event.getCode() == KeyCode.ENTER && !(inputText.equals(""))) {
                System.out.println("detected enter");

                Label newSpriteLabel = new Label(inputText);
                spriteContainer.getChildren().remove(1);
                spriteContainer.getChildren().add(newSpriteLabel);
                newSpriteLabel.setOnMouseClicked(labelEvent->clickOnLabel(labelEvent, spriteContainer));
            }
            event.consume();
        });
        spriteContainer.getChildren().remove(1);
        spriteContainer.getChildren().add(enterSpriteNameField);
    }


    /**
     * Allows the user to drag a sprite from the sprite box to the canvas.
     * @param imageView The imageview of the sprite.
     * @param sprite The image of the sprite.
     * @param programBox The program box itself.
     * @param spriteName The name of the sprite.
     */
    private void dragAndDrop(ImageView imageView, Image sprite, HBox programBox, String spriteName) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);
                event.consume();

                canvas.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        if (event.getGestureSource() ==   imageView)  {
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
                        spriteController.addSprite(spriteName,x, y, sprite);
                        gc.drawImage(sprite, x , y);

                        imageView.setOnDragDetected(null);
                        programBox.getChildren().clear();
                        Text programText = new Text();
                        programText.setText(spriteName + "'s " +"Program Box");
                        programBox.getChildren().add(programText);

                        event.consume();
                    }
                });


            }

        });

    }



}
