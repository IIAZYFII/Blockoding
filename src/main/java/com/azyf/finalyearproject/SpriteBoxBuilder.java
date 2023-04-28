/**
 * Builder for the sprite box GUI.
 * @author Hussain Asif
 * @version 1.0.
 */
package com.azyf.finalyearproject;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.checkerframework.checker.units.qual.N;

import javax.naming.Context;

public class SpriteBoxBuilder {

    /**
     * An empty constructor for the sprite box builder.
     */
    public SpriteBoxBuilder() {

    }

    /**
     * Adds a sprite to the SpriteBox.
     * @param spriteName
     * @param spriteViewer
     * @param spriteController
     * @param spriteBox
     * @return
     */
    public HBox addSpriteToBox(String spriteName, ImageView spriteViewer, SceneController sceneController
            ,SpriteController spriteController, WindowBuilder windowBuilder, HBox spriteBox){
        VBox spriteContainer = createSpriteContainer(spriteName, spriteViewer, spriteController);
        Label spriteLabelName = (Label) spriteContainer.getChildren().get(1);
        String spriteLabelText = spriteLabelName.getText();


        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeSpriteMenuFromCanvasItem = new MenuItem("Remove Sprite From Canvas");
        removeSpriteMenuFromCanvasItem.setOnAction( event -> {
            String name = ((Label) spriteContainer.getChildren().get(1)).getText();
            boolean removeSprite = removeSpriteFromCanvas(name, spriteController);
            if (removeSprite == false) {
                windowBuilder.drawSpriteNotOnCanvasError();
            } else {
                StageInitializer.drawScene(sceneController, spriteController);
                StageInitializer.dragAndDrop(spriteViewer, spriteViewer.getImage(), spriteLabelText, spriteController);
            }
            event.consume();
        });

        MenuItem removeSpriteFromBox = new MenuItem("Remove Sprite From Sprite Box");
        removeSpriteFromBox.setOnAction(event -> {
            String name = ((Label) spriteContainer.getChildren().get(1)).getText();
            removeSpriteFromCanvas(name, spriteController);
            StageInitializer.drawScene(sceneController, spriteController);
            spriteBox.getChildren().remove(spriteContainer);
            StageInitializer.setSpriteBox(spriteBox);



        });

        contextMenu.getItems().add(removeSpriteMenuFromCanvasItem);
        contextMenu.getItems().add(removeSpriteFromBox);

        spriteContainer.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("a");
                contextMenu.show(spriteContainer, e.getScreenX(), e.getScreenY());
                e.consume();
            }
        }
        );
        spriteBox.getChildren().add(spriteContainer);
        StageInitializer.dragAndDrop(spriteViewer, spriteViewer.getImage(), spriteLabelText, spriteController);
        return spriteBox;
    }


    /**
     * Creates a Sprite Container.
     * @param spriteName
     * @param spriteViewer
     * @param spriteController
     * @return
     */
    private VBox createSpriteContainer(String spriteName, ImageView spriteViewer, SpriteController spriteController) {
        VBox spriteContainer = new VBox();
        Label spriteLabel = new Label(spriteName);

        spriteLabel.setOnMouseClicked(e -> clickOnLabel(e, spriteContainer, spriteController));


        spriteContainer.getChildren().add(spriteViewer);
        spriteContainer.getChildren().add(spriteLabel);
        spriteContainer.setAlignment(Pos.CENTER);




        return spriteContainer;
    }

    /**
     * Allows the User to change the label text.
     *
     * @param e               The current Event.
     * @param spriteContainer The Container which contains the sprite label.
     */
    private void clickOnLabel(Event e, VBox spriteContainer, SpriteController spriteController) {
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
                    newSpriteLabel.setOnMouseClicked(labelEvent -> clickOnLabel(labelEvent, spriteContainer, spriteController));

                }


            }
            event.consume();
        });
        spriteContainer.getChildren().remove(1);
        spriteContainer.getChildren().add(enterSpriteNameField);
    }

    /**
     * Checks if the name of the sprite already exists.
     * @param spriteBox The sprite box that contains the sprites.
     * @param spriteName The name of the current sprite.
     * @return True if the name exist otherwise it is false.
     */
    public boolean checkNameExist(HBox spriteBox, String spriteName) {
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

    /**
     * Removes the sprite from the canvas.
     * @param spriteName The name of the sprite.
     * @param spriteController The controller for the sprite.
     * @return True if the sprite was removed, otherwise it is false.
     */
    private boolean removeSpriteFromCanvas(String spriteName, SpriteController spriteController) {
        int index = 0;
        while( index < spriteController.size()) {
            if(spriteController.getSprite(index).getSpriteName().equals(spriteName)) {
                spriteController.removeSprite(index);
                return true;
            }
            index++;
        }
        return  false;
    }



}
