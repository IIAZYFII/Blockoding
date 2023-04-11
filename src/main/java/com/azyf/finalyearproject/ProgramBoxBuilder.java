package com.azyf.finalyearproject;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Queue;

public class ProgramBoxBuilder {
    private InputBoxBuilder inputBoxBuilder;

    public ProgramBoxBuilder() {
        inputBoxBuilder = new InputBoxBuilder();
    }



    public VBox drawProgramBox(Queue<Block> blocks, VariableManager variableManager, SoundController soundController,
                                SpriteController spriteController, SceneController sceneController)  {


    VBox programBox = new VBox();
    programBox.setStyle("-fx-border-style: solid inside;" + "-fx-background-color: #FFFDD0;");


        while (blocks.size() > 0) {
            Block block = blocks.remove();
            String blockName = block.getName();
            StackPane stackPane = new StackPane();
            HBox hBox = null;
            String secondBlockName = "";
            String thirdBlockName = "";
            switch (blockName) {
                case "START":
                case "END":
                    stackPane = (StackPane) drawBlock(blockName, 0, 255, 0, soundController);
                    programBox.getChildren().add(stackPane);
                    break;
                case "DO":
                case "ONCE":
                    stackPane = (StackPane) drawBlock(blockName, 255, 95, 31, soundController);
                    programBox.getChildren().add(stackPane);
                    break;
                case "ROTATE":
                case "MOVE":
                    blocks.remove();
                    block = blocks.remove();
                    blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;

                case "CONDITION":
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                case "FLIP":
                    blocks.remove();
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                case "PAUSE":
                    block = blocks.remove();
                    hBox = (HBox) drawBlock(blockName, 255, 95, 31, soundController);
                    programBox.getChildren().add(hBox);
                    break;
                case "TELPORT":
                    blockName = "TELPORT";
                    blocks.remove();
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();


                    hBox = (HBox) drawBlock(blockName, secondBlockName, 255, 95, 31, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    if (secondBlockName.equals("X")) {
                        blocks.remove();
                    }
                    break;
                case "WHENEVER":
                    block = blocks.remove();
                    secondBlockName = block.getName();
                    if (secondBlockName.equals("PRESSES")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("HOVERS")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("CLICKS")) {
                        blocks.remove();
                    } else if (secondBlockName.equals("NOT")) {
                        stackPane = (StackPane) drawBlock(secondBlockName, 192, 240, 22,soundController);
                        secondBlockName = blocks.remove().getName();
                        if (secondBlockName.equals("PRESSES") ||(secondBlockName.equals("CLICKS")
                                || secondBlockName.equals("HOVERS"))) {
                            blocks.remove();
                        }
                        thirdBlockName = blocks.remove().getName();
                        hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 192, 240, 22, spriteController, variableManager);
                        hBox.getChildren().add(1,stackPane);
                        programBox.getChildren().add(hBox);
                        break;
                    } else if (secondBlockName.equals("VARIABLE")) {
                        String checkEquals = blocks.remove().getName();
                        if(checkEquals.equals("EQUALS")) {
                            thirdBlockName = blocks.remove().getName();

                            String tmpBlockName = blocks.remove().getName();
                            if(!(tmpBlockName.equals("THEN"))) {

                                stackPane = (StackPane) drawBlock(tmpBlockName, 0, 255, 0, soundController);
                                hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 192, 240, 22, spriteController, variableManager);
                                hBox.getChildren().add(4, stackPane);
                                if(blocks.remove().getName().equals("NUMBER")) {
                                    TextField textField = inputBoxBuilder.createTextField();
                                    hBox.getChildren().add(5,textField);

                                }

                            } else {
                                hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 192, 240, 22, spriteController, variableManager);

                            }

                        }
                        else {
                            thirdBlockName = blocks.remove().getName();
                            stackPane = (StackPane) drawBlock(thirdBlockName, 0, 255, 0, soundController);
                            hBox = (HBox) drawBlock(blockName, secondBlockName,checkEquals , 192, 240, 22, spriteController, variableManager);
                            hBox.getChildren().add(stackPane);
                            if(blocks.remove().getName().equals("NUMBER")) {
                                TextField textField = inputBoxBuilder.createTextField();
                                hBox.getChildren().add(4,textField);

                            }
                            stackPane = (StackPane) drawBlock(blocks.remove().getName(), 0, 255, 0, soundController);
                            hBox.getChildren().add(stackPane);
                        }
                        programBox.getChildren().add(hBox);
                        break;

                    } else if(secondBlockName.equals("SPRITE")) {
                        blocks.remove();
                        thirdBlockName = blocks.remove().getName();
                        hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 192, 240, 22, spriteController, variableManager);
                        stackPane = createStackPane(blocks.remove().getName(), 192, 240, 22);
                        hBox.getChildren().add(stackPane);
                        programBox.getChildren().add(hBox);
                        break;
                    }
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 95, 31, spriteController, variableManager);
                    programBox.getChildren().add(hBox);
                    break;
                case "ELSE":
                case "TERMINATE":
                    stackPane = (StackPane) drawBlock(blockName, 192, 240, 22, soundController);
                    programBox.getChildren().add(stackPane);
                    break;
                case "LOOP":
                    stackPane = (StackPane) drawBlock(blockName, 192, 240, 22, soundController);
                    hBox = new HBox();
                    hBox.getChildren().add(stackPane);
                    stackPane = (StackPane) drawBlock(blocks.remove().getName(), 192, 240, 22, soundController);
                    hBox.getChildren().add(stackPane);
                    programBox.getChildren().add(hBox);
                    break;
                case "HOVERS":
                case "PRESSES":
                case "CLICKS":
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 192, 240, 22, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                case "SET":
                    blocks.remove();
                    secondBlockName = blocks.remove().getName();
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 255, 255, spriteController, variableManager);
                    programBox.getChildren().add(hBox);
                    break;
                case "VARIABLE":
                    stackPane = createStackPane(blocks.remove().getName(),255,255,255);
                    secondBlockName = blocks.remove().getName();
                    StackPane equalsStackPane = createStackPane(blocks.remove().getName(),255,255,255);
                    thirdBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, thirdBlockName, 255, 255, 255, spriteController, variableManager);
                    hBox.getChildren().add(1,stackPane);
                    hBox.getChildren().add(3,equalsStackPane);
                    programBox.getChildren().add(hBox);
                    break;
                case "SPEAK":
                    secondBlockName = blocks.remove().getName();
                    hBox = (HBox) drawBlock(blockName, secondBlockName, 19, 3, 252, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                case "PLAY":
                case "LOOPS":
                    hBox = (HBox) drawBlock(blockName, 252, 3, 136, soundController);
                    programBox.getChildren().add(hBox);

                    break;
                case "INCREASE":
                case "DECREASE":
                    hBox = (HBox) drawBlock(blockName, blocks.remove().getName(), 252, 3, 136, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                case "CHANGE":
                    hBox = (HBox) drawBlock(blockName, blocks.remove().getName(), 119, 3, 252, spriteController, sceneController);
                    programBox.getChildren().add(hBox);
                    break;
                default:
                    System.out.println("test");
                    break;


            }

        }
        return programBox;
    }

    private StackPane createStackPane(String blockName, int red, int green, int blue) {
        StackPane stackPane = new StackPane();
        Rectangle blockBox = new Rectangle(70, 30);
        blockBox.setFill(Color.rgb(red, green, blue));
        Rectangle blockBorder = new Rectangle(80, 40);

        Label blockText = new Label(blockName);
        if(blockName.equals("SPEAK") || blockName.equals("ASK")) {
            blockText.setTextFill(Color.color(1,1,1));
        }

        stackPane.getChildren().add(blockBorder);
        stackPane.getChildren().add(blockBox);
        stackPane.getChildren().add(blockText);
        blockText.setFont(new Font("Arial", 15));
        return stackPane;
    }



    private Node drawBlock(String blockName, int red, int green, int blue, SoundController soundController) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        if (blockName.equals("PAUSE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            TextField textField = inputBoxBuilder.createTextField();
            hBox.getChildren().add(textField);

            return hBox;
        } else if (blockName.equals("PLAY") || blockName.equals("LOOPS")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);


            ComboBox comboBox = inputBoxBuilder.createComboBox(soundController.getSoundFileNamesAsArray());
            hBox.getChildren().add(comboBox);

            return hBox;
        }
        return stackPane;
    }

    private Node drawBlock(String blockName, String secondBlockName, int red, int green, int blue, SpriteController spriteController,
                           SceneController sceneController) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        if (blockName.equals("ROTATE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);


            String dropDown[] = {"90", "180", "270"};
            comboBox = inputBoxBuilder.createComboBox(dropDown);
            hBox.getChildren().add(comboBox);


            return hBox;
        } else if (blockName.equals("MOVE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);


            ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            TextField textField = inputBoxBuilder.createTextField();
            hBox.getChildren().add(textField);
            return hBox;

        } else if (blockName.equals("TELPORT")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            stackPane = createStackPane("TO", red, green, blue);
            hBox.getChildren().add(stackPane);
            ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);
            if (secondBlockName.equals("X")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                TextField textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);

                stackPane = createStackPane("Y", red, green, blue);
                hBox.getChildren().add(stackPane);
                textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);

                return hBox;
            } else if (secondBlockName.equals("MOUSE")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                return hBox;
            } else if (secondBlockName.equals("SPRITE")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
                hBox.getChildren().add(comboBox);
                return hBox;
            } else if (secondBlockName.equals("RANDOM")) {
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                return hBox;
            }

        } else if (blockName.equals("FLIP") ) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);
            return hBox;

        } else if(blockName.equals("CONDITION")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);
            return hBox;

        }else if (blockName.equals("PRESSES")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                    "UP", "DOWN", "LEFT", "RIGHT", "SPACE"};
            ComboBox comboBox = inputBoxBuilder.createComboBox(keys);
            hBox.getChildren().add(comboBox);
            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("CLICKS") || blockName.equals("HOVERS")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("SPEAK")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            TextField textField = inputBoxBuilder.createTextField();
            hBox.getChildren().add(textField);
            return hBox;

        } else if (blockName.equals("DECREASE") || blockName.equals("INCREASE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);

            stackPane = createStackPane(secondBlockName, red,green, blue);
            hBox.getChildren().add(stackPane);
            return  hBox;

        } else if (blockName.equals("CHANGE")) {
            HBox hBox = new HBox();
            hBox.getChildren().add(stackPane);
            ComboBox comboBox = inputBoxBuilder.createComboBox(sceneController.getScenesAsList());
            hBox.getChildren().add(comboBox);
            return hBox;
        }
        return stackPane;
    }



    private Node drawBlock(String blockName, String secondBlockName, String thirdBlockName, int red, int green, int blue,
                           SpriteController spriteController, VariableManager variableManager) {
        StackPane stackPane = createStackPane(blockName, red, green, blue);
        HBox hBox = null;
        if (blockName.equals("WHENEVER")) {
            if (secondBlockName.equals("PRESSES")) {
                hBox = new HBox();
                hBox.getChildren().add(stackPane);
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                        "UP", "DOWN", "LEFT", "RIGHT", "SPACE"};
                ComboBox comboBox = inputBoxBuilder.createComboBox(keys);
                hBox.getChildren().add(comboBox);
            } else if (secondBlockName.equals("HOVERS") || secondBlockName.equals("CLICKS")) {
                hBox = new HBox();
                hBox.getChildren().add(stackPane);
                stackPane = createStackPane(secondBlockName, red, green, blue);
                hBox.getChildren().add(stackPane);
                ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
                hBox.getChildren().add(comboBox);

            } else if (secondBlockName.equals("VARIABLE")){
                hBox = new HBox();
                hBox.getChildren().add(stackPane);

                ComboBox comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
                if(!(thirdBlockName.equals("LESS") || thirdBlockName.equals("GREATER"))){
                    stackPane = createStackPane("EQUALS", red, green, blue);
                    hBox.getChildren().add(stackPane);
                }


                if (thirdBlockName.equals("NUMBER")) {
                    TextField textField = inputBoxBuilder.createTextField();
                    hBox.getChildren().add(textField);

                    stackPane = createStackPane("THEN", red, green, blue);
                    hBox.getChildren().add(stackPane);
                    return  hBox;
                }
            } else if(secondBlockName.equals("SPRITE")) {
                hBox = new HBox();
                hBox.getChildren().add(stackPane);

                ComboBox comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
                hBox.getChildren().add(comboBox);

                stackPane = createStackPane("TOUCHES", 192,240,22);

                hBox.getChildren().add(stackPane);

                if(thirdBlockName.equals("MOUSE")) {
                    stackPane =  createStackPane(thirdBlockName, 192, 240, 22);
                    hBox.getChildren().add(stackPane);
                } else {
                    comboBox = inputBoxBuilder.createComboBox(spriteController.getSpriteNameAsArray());
                    hBox.getChildren().add(comboBox);
                }
                return hBox;
            }

            stackPane = createStackPane(thirdBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            return hBox;
        } else if (blockName.equals("SET")) {
            hBox = new HBox();
            hBox.getChildren().add(stackPane);

            ComboBox comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
            hBox.getChildren().add(comboBox);

            stackPane = createStackPane(secondBlockName, red, green, blue);
            hBox.getChildren().add(stackPane);

            if(thirdBlockName.equals("ASK")) {
                stackPane = createStackPane(thirdBlockName,19, 3, 252);
                hBox.getChildren().add(stackPane);

                TextField textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);
            } else if(thirdBlockName.equals("NUMBER")) {
                TextField textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }





            return hBox;


        } else if(blockName.equals("VARIABLE")) {
            hBox = new HBox();
            ComboBox comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
            hBox.getChildren().add(comboBox);

            if(secondBlockName.equals("NUMBER")) {
                TextField textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }

            if(thirdBlockName.equals("NUMBER")) {
                TextField textField = inputBoxBuilder.createTextField();
                hBox.getChildren().add(textField);
            } else {
                comboBox = inputBoxBuilder.createComboBox(variableManager.getVariableNamesAsArray());
                hBox.getChildren().add(comboBox);
            }
            return hBox;
        }

        return stackPane;
    }


    public InputBoxBuilder getInputBoxBuilder() {
        return inputBoxBuilder;
    }
}
