/**
 * This class is used to build the input boxes to allow the users to enter to choose sprites and enter numbers as well
 * as strings.
 * @author Hussain Asif.
 * @version 1.0.
 */

package com.azyf.finalyearproject;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

public class InputBoxBuilder {
    private ArrayList<String> inputBoxes = new ArrayList<>();
    private HashMap<String, String> inputBoxesValues  = new HashMap<>();


    /**
     * An empty constructor to build the Input Box Builder.
     */
    public InputBoxBuilder(){

    }

    /**
     * Gets the input boxes.
     * @return The input boxes.
     */
    public ArrayList<String> getInputBoxes() {
        return inputBoxes;
    }

    /**
     * Gets the input box values.
     * @return a hashmap of  input box values.
     */
    public HashMap<String, String> getInputBoxesValues() {
        return inputBoxesValues;
    }

    /**
     * Creates a text field for the GUI.
     * @return an empty text field.
     */
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
        System.out.println(inputBoxes.size() + "Created  text field");
        return textField;
    }

    /**
     * Creates a combo box for the GUI.
     * @param dropDown The contents of the combo box.
     * @return A combo box with the contents.
     */
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
        System.out.println(inputBoxes.size() + "Created  Combo box");
        return comboBox;
    }

    /**
     * Gets the index of the input box.
     * @param inputBox The input box as a string.
     * @return The input box index.
     */
    private int getInputBoxIndex(String inputBox) {
        for (int i = 0; i < inputBoxes.size(); i++) {
            if (inputBoxes.get(i).equals(inputBox)) {
                return i;
            }
        }
        return -1;
    }



}
