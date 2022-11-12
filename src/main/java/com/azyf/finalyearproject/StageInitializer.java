package com.azyf.finalyearproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.N;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
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

    public StageInitializer() throws FileNotFoundException {
    }

    @Override
    public void onApplicationEvent(BlockApplication.StageReadyEvent event) {
        BorderPane root = (BorderPane) buildGUI();

        Stage stage = event.getStage();
        stage.setScene(new Scene(root, 1300, 800));
        stage.show();

    }

    private Pane buildGUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FF5438;");

        canvas = new Canvas(728,597);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255,253,208));
        gc.fillRoundRect(0,0,728,597,20.0,20.0);
        //gc.setLineWidth(20.0);
        gc.strokeRoundRect(0,0,728,597,20.0,20.0);

        root.setCenter(canvas);


        HBox sceneBox = new HBox();
        root.setLeft(sceneBox);

        AnchorPane bottomBar = new AnchorPane();
        root.setBottom(bottomBar);

        VBox ioBar = new VBox();
        VBox tabBar = new VBox();
        bottomBar.getChildren().add(ioBar);
        bottomBar.getChildren().add(tabBar);
        AnchorPane.setLeftAnchor(ioBar, 10d);
        AnchorPane.setRightAnchor(tabBar,10d);

      HBox topBar = new HBox();
      topBar.setStyle("-fx-background-color: #FF4122;" + "-fx-border-style: solid inside;");
      root.setTop(topBar);

        HBox rightPane = new HBox();
        VBox spriteBox = new VBox();

        blockCanvas = new Canvas(100,200);
        rightPane.getChildren().add(spriteBox);
        rightPane.getChildren().add(blockCanvas);

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

        /*x
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem settings = new MenuItem("Settings");
        file.getItems().add(settings);
        menuBar.getMenus().add(file);
        root.getChildren().add(menuBar);
         */
        Button settingButton = new Button();
        Image settingButtonImg = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\SettingsButton.png");
       // ImageView settingButtonView = new ImageView(settingButtonImg);
        //settingButton.setGraphic(settingButtonView);
        topBar.getChildren().add(settingButton);
        settingButton.setOnAction(e -> {
            drawSettings();
        });
        return  root;
    }

    private void drawSettings(){
        Stage settingStage = new Stage();
        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color: #96ffa1;");


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
}
