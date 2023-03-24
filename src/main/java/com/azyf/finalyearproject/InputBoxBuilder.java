package com.azyf.finalyearproject;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

public class InputBoxBuilder {
    private ArrayList<String> inputBoxes;
    private HashMap<String, String> inputBoxesValues;
    public InputBoxBuilder(){
        ArrayList<String> inputBoxes  = new ArrayList<>();
        HashMap<String, String> inputBoxesValues = new HashMap<>();
    }


    public ArrayList<String> getInputBoxes() {
        return inputBoxes;
    }

    public HashMap<String, String> getInputBoxesValues() {
        return inputBoxesValues;
    }

    public TextField createTextField() {
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

    public ComboBox createComboBox(String[] dropDown) {
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

    private int getInputBoxIndex(String inputBox) {
        for (int i = 0; i < inputBoxes.size(); i++) {
            if (inputBoxes.get(i).equals(inputBox)) {
                return i;
            }
        }
        return -1;
    }



}
