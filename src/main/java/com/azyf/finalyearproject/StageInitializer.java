package com.azyf.finalyearproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.N;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class StageInitializer implements ApplicationListener<BlockApplication.StageReadyEvent> {
    private Canvas canvas;
    private Interpreter interpreter = new Interpreter();
    private TextExtractor textExtractor = new TextExtractor();
    private ImageProcessor imageProcessor = new ImageProcessor();
    private Canvas blockCanvas;
    private Image playButtonImg;
    private Image stopButtonImg;
    private Image defaultSprite;
    private ImageView defaultSpriteViewer = new ImageView();


    public StageInitializer() throws FileNotFoundException {
    }

    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {
        BorderPane root = (BorderPane) buildGUI();
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
        Stage stage = event.getStage();
        stage.setScene(new Scene(root, screenWidth, screenHeight));
        stage.show();

    }

    private Pane buildGUI() {

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

        spriteBox.getChildren().add(defaultSpriteViewer);

        dragAndDrop(defaultSpriteViewer, defaultSprite);




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
                textExtractor.extractText(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

       // Button settingButton = new Button();
        Image settingButtonImg = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\SettingsButton.png");
       // ImageView settingButtonView = new ImageView(settingButtonImg);
        //settingButton.setGraphic(settingButtonView);
        //topBar.getChildren().add(settingButton);

       /* settingButton.setOnAction(e -> {
            drawSettings();
        });

        */
        return  root;
    }

    private void drawSettings(){
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

    private void dragAndDrop(ImageView imageView, Image sprite) {

            imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);
                event.consume();
            }
        });


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
                double x = event.getX();
                double y = event.getY();

                // Print a string showing the location.
                String s = String.format("You dropped at (%f, %f) relative to the canvas.", x, y);
                System.out.println(s);

                // Draw an icon at the dropped location.
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(sprite, x - sprite.getWidth() / 2.0, y - sprite.getHeight() / 2.0);


                event.consume();
            }
        });

    }





}
