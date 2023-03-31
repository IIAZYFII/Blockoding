package com.azyf.finalyearproject;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class VariableBoxBuilder {
    public VariableBoxBuilder(){

    }




    public HBox drawVariableBox(HBox variableBox, VariableManager variableManager) {
        variableBox.getChildren().clear();
        for(int i = 0; i < variableManager.getVariables().size(); i++) {
           variableBox = addVariableToBox(variableManager.getVariables().get(i), variableBox);
        }
        return variableBox;

    }

    private HBox addVariableToBox(Variable variable, HBox variableBox) {
        VariableType variableType = variable.getType();
        String content = variable.getName() + " : ";
        if (variableType == VariableType.Integer) {
            content = content +  variable.getValue();
        } else if (variableType == VariableType.String) {
            content = content +  variable.getContent();
        }

        StackPane stackPane =  createStackPane(content, 255, 84, 56);
        variableBox.getChildren().add(stackPane);
        return variableBox;

    }


    private StackPane createStackPane(String blockName, int red, int green, int blue) {
        StackPane stackPane = new StackPane();
        Rectangle blockBox = new Rectangle(70, 30);
        blockBox.setFill(Color.rgb(red, green, blue));
        Rectangle blockBorder = new Rectangle(80, 40);
        Label blockText = new Label(blockName);
        stackPane.getChildren().add(blockBorder);
        stackPane.getChildren().add(blockBox);
        stackPane.getChildren().add(blockText);
        blockText.setFont(new Font("Arial", 15));
        return stackPane;
    }






}
