package com.azyf.finalyearproject;
/**
 * Builds the Window for the UI.
 * @author Hussain Asif.
 * @version 1/0
 */

import com.google.zxing.WriterException;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class WindowBuilder {
    QRCodeBuilder qrCodeBuilder;
    private boolean alreadyExist;


    /**
     * Constructor for the window builder.
     */
    public WindowBuilder() {
        qrCodeBuilder = new QRCodeBuilder();
        alreadyExist = false;
    }

    /**
     * Draws the settings window.
     */
    public void drawSettings() {
        Stage settingStage = new Stage();
        HBox hBox = new HBox();
        BorderPane setttingsRoot = new BorderPane();
        setttingsRoot.setStyle("-fx-background-color: #FF5438;");
        Button linkAppButton = new Button();
        linkAppButton.setMinSize(100, 100);
        linkAppButton.setText("Link App");
        linkAppButton.setOnAction(e -> {
            try {
                qrCodeBuilder.generateQRCode();
                hBox.getChildren().clear();
                String path = FileController.getAbsolutePath() + "\\Cache\\qrcode.png";
                System.out.println(path);
                Image qrCode = new Image(path);
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
        javafx.scene.Scene scene = new Scene(setttingsRoot, 350, 350);
        settingStage.setScene(scene);
        settingStage.showAndWait();
    }

    /**
     * Draws the window to set the background.
     * @param imageProcessor Image processor for thumbnails.
     * @param sceneController Controller for the scene background.
     * @param spriteController Controller for the sprite.
     */
    public void drawSceneController(ImageProcessor imageProcessor, SceneController sceneController,
                                    SpriteController spriteController) {
        Stage sceneControllerStage = new Stage();
        sceneControllerStage.setResizable(false);
        ScrollPane basePane = new ScrollPane();
        basePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        basePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        File dir = new File(FileController.getAbsolutePath() + "/Assets/Images/Scenes/Thumbnails");
        File[] files = dir.listFiles();
        VBox vBox = new VBox();
        vBox.setPrefSize(350,350);
        vBox.setMaxWidth(350);
        vBox.setStyle("-fx-background-color: #FF5438;");
        for(int i = 0; i < files.length; i++) {
            Image image = new Image(files[i].getAbsolutePath());
            ImageView imageView = new ImageView(image);
            HBox hBox = new HBox();
            hBox.getChildren().add(imageView);

            Label label = new Label(files[i].getName().substring(0, files[i].getName().lastIndexOf(".")));
            label.setMinSize(75,50);
            hBox.getChildren().add(label);

            Button sceneBtn = new Button();
            sceneBtn.setMinSize(100,25);
            sceneBtn.setText("Choose Scene");
            int finalI = i;
            sceneBtn.setOnAction(e-> {
                StageInitializer.setSceneBackground( new Image(FileController.getAbsolutePath() + "/Assets/Images/Scenes/" + files[finalI].getName()));
                StageInitializer.drawScene(sceneController, spriteController);
                e.consume();
            });
            hBox.getChildren().add(sceneBtn);
            vBox.getChildren().add(hBox);
        }
        Button removeSceneBtn = new Button();
        removeSceneBtn.setMinSize(100,25);
        removeSceneBtn.setText("Remove scene");
        removeSceneBtn.setOnAction(e->{
           StageInitializer.setSceneBackground(null);
           StageInitializer.drawScene(sceneController, spriteController);
        });

        Button addSceneBtn = new Button();
        addSceneBtn.setMinSize(125,25);
        addSceneBtn.setText("Add custom scene");
        addSceneBtn.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Scene");
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("Image files (*.PNG, *.JPEG, *.JPG, )", "*.PNG", "*.JPEG", "*.JPG");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(sceneControllerStage);
            if (file != null) {
                Image image = new Image(file.getAbsolutePath());
                File copyImage = new File(FileController.getAbsolutePath() + "/Assets/Images/Scenes/" + file.getName());
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    ImageIO.write(bufferedImage,"png",copyImage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                imageProcessor.produceImageThumbnails(image);
                drawSceneController(imageProcessor,sceneController ,spriteController);
                sceneControllerStage.close();
            }

        });
        HBox hBox = new HBox();

        hBox.getChildren().add(removeSceneBtn);
        hBox.setMargin(removeSceneBtn, new Insets(0,20,0,0));
        hBox.getChildren().add(addSceneBtn);

        vBox.getChildren().add(hBox);
       basePane.setContent(vBox);
        Scene scene = new Scene(basePane, 350, 350);


        sceneControllerStage.setScene(scene);
        sceneControllerStage.show();

    }


    /**
     * Draws the syntax error window.
     */
    public void drawSyntaxError() {
        String title = "Syntax Error";
        String contentText = "You have a SYNTAX ERROR. This means your code is wrong.";
        Image image = new Image("C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\Assets\\Images\\DialogIcons\\SyntaxErrorIcon.png  ");
        drawErrorWindow(title, contentText, image);

    }

    /**
     * Draws the error when the sprite is not on the canvas.
     */
    public void drawSpriteNotOnCanvasError(){
        String title = "Cannot Remove Sprite";
        String contentText = "This sprite cannot be removed as it has not been placed on the canvas";
        Image image = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\DialogIcons\\access-denied-icon.png");
        drawErrorWindow(title, contentText, image);
    }


    /**
     * Draws the null variable error.
     */
    public void drawDeleteNullVariable() {
        String title = "Cannot delete a non-selected variable";
        String contentText = "There is no variable selected to be deleted.";
        Image image = new Image("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Images\\DialogIcons\\access-denied-icon.png");
        drawErrorWindow(title, contentText, image);
    }

    /**
     * Draws a generic window for the errors.
     * @param title Title of the window.
     * @param contentText The message that will be displayed.
     * @param image
     */
    private void drawErrorWindow(String title, String contentText, Image image) {
        Alert  errorWindow = new Alert(Alert.AlertType.ERROR);
        errorWindow.setTitle(title);
        errorWindow.setContentText(contentText);
        ImageView imageView = new ImageView(image);
        errorWindow.setGraphic(imageView);
        errorWindow.showAndWait();
    }


    /**
     * Draws the variable manager window.
     * @param variableManager
     */
    public void drawVariableManager(VariableManager variableManager) {
        Stage variableManagerStage = new Stage();
        variableManagerStage.setResizable(false);
        BorderPane variableManagerRoot = drawBaseVariableManager(variableManagerStage,variableManager);


        Scene scene = new Scene(variableManagerRoot, 350, 350);
        variableManagerStage.setScene(scene);
        variableManagerStage.showAndWait();
    }

    /**
     * Draws the contents inside the variable window.
     * @param variableManagerStage Stage for variable manager.
     * @param variableManager The manager for variable.
     * @return A pane containing the contents of the variable manager windows.
     */
    private BorderPane drawBaseVariableManager(Stage variableManagerStage, VariableManager variableManager) {
        AtomicReference<BorderPane> variableManagerRoot  = new AtomicReference<>(new BorderPane());
        variableManagerRoot.get().setStyle("-fx-background-color: #FF5438;");

        VBox content = new VBox();

        Button createButton = new Button();
        createButton.setText("Create Variable");
        createButton.setMinSize(200,50);

        createButton.setOnAction(e-> {
            content.getChildren().clear();
            Label variableName = new Label();
            variableName.setText("Name of Variable");
            variableName.setFont(Font.font("Sitka Display", FontWeight.BOLD,14));
            variableName.setTextFill(Color.WHITESMOKE);
            variableName.setMinSize(200,50);
            content.getChildren().add(variableName);


            TextField enterVariableName = new TextField();
            enterVariableName.setMinSize(200,50);
            content.getChildren().add(enterVariableName);

            Label initialType = new Label();
            initialType.setText("Initial Type");
            initialType.setFont(Font.font("Sitka Display", FontWeight.BOLD,14));
            initialType.setTextFill(Color.WHITESMOKE);
            initialType.setMinSize(100,50);
            content.getChildren().add(initialType);

            String[] dropDown = {"Integer", "String"};

            ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(dropDown));
            comboBox.setMinWidth(100);
            content.getChildren().add(comboBox);

            System.out.println(javafx.scene.text.Font.getFamilies().toString());


            Label initialValue = new Label();
            initialValue.setText("Initial Value");
            initialValue.setTextFill(Color.WHITESMOKE);
            initialValue.setMinSize(100,50);
            initialValue.setFont(Font.font("Sitka Display", FontWeight.BOLD,14));
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
                String variableType = (String) comboBox.getValue();
                Variable variable = null;
                if(variableType.equals("Integer")) {
                    int variableValue;
                    try {
                        variableValue = Integer.parseInt(enterInitialValue.getText());
                        VariableType type = VariableType.Integer;
                        variable = new Variable(variableValue, name, type);
                        System.out.println("Added Variable");
                        boolean alreadyExist = variableManager.addVariable(variable);
                        if(alreadyExist == false) {
                            //addVariableToCanvas(variable);
                        }


                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Expected a number");
                    } finally {
                        variableManagerStage.close();
                    }
                } else if(variableType.equals("String")) {
                    String variableValue;
                    variableValue = enterInitialValue.getText();
                    VariableType type = VariableType.String;
                    variable = new Variable(variableValue, name, type);
                    System.out.println("Added Variable");
                    boolean alreadyExist = variableManager.addVariable(variable);
                    if(alreadyExist == false) {

                    }

                }




            });
            confirmButton.setMinSize(50,25);


            buttonsBox.getChildren().add(confirmButton);
            buttonsBox.setMargin(confirmButton, new Insets(0,0,0,10));

            content.getChildren().add(buttonsBox);


            e.consume();
        });
        Button deleteButton = new Button();
        deleteButton.setText("Delete Variable");
        deleteButton.setMinSize(200,50);

        deleteButton.setOnAction(e -> {
            content.getChildren().clear();
            Label variableLabel = new Label();
            variableLabel.setText("List of Variables");
            variableLabel.setFont(Font.font("Sitka Display", FontWeight.BOLD,14));
            variableLabel.setTextFill(Color.WHITESMOKE);
            variableLabel.setMinSize(200,50);

            VBox vbox = new VBox();
            vbox.getChildren().add(variableLabel);

            ComboBox comboBox =
                    new ComboBox(FXCollections.observableArrayList(variableManager.getVariableNamesAsArray()));

            comboBox.setMinWidth(100);

            vbox.getChildren().add(comboBox);

            Button deleteBtn = new Button();
            deleteBtn.setMinSize(100,50);
            deleteBtn.setText("Delete Variable");
            deleteBtn.setOnAction(event-> {
               String variableName = (String) comboBox.getValue();
               if(variableName == null) {
                  drawDeleteNullVariable();
               } else {
                   variableManager.deleteVariable(variableName);
                   variableManagerStage.close();
               }

               event.consume();
            });
            vbox.getChildren().add(deleteBtn);

            vbox.setMargin(variableLabel, new Insets(20,0,0,110));
            vbox.setMargin(comboBox, new Insets(40,0,0,120));
            vbox.setMargin(deleteBtn, new Insets(40,0,0,120));


            variableManagerRoot.get().getChildren().add(vbox);



        });


        content.getChildren().add(createButton);
        content.getChildren().add(deleteButton);

        content.setMargin(createButton, new Insets(50,0,0,70));

       content.setMargin(deleteButton, new Insets(20,0,0,70));

        content.setPadding(new Insets(0,0,0,0));
        variableManagerRoot.get().getChildren().add(content);
        return variableManagerRoot.get();
    }


    /**
     * Gets a boolean value of the variable already exist.
     * @return Variable already exists or not.
     */
    public boolean getAlreadyExist(){
        return alreadyExist;
    }



}
