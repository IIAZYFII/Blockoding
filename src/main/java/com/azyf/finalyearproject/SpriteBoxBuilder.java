package com.azyf.finalyearproject;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SpriteBoxBuilder {

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
    public HBox addSpriteToBox(String spriteName, ImageView spriteViewer, SpriteController spriteController, HBox spriteBox){
        VBox spriteContainer = createSpriteContainer(spriteName, spriteViewer, spriteController);
        Label spriteLabelName = (Label) spriteContainer.getChildren().get(1);
        String spriteLabelText = spriteLabelName.getText();
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

}
