package com.azyf.finalyearproject;

import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;


public class Interpreter {
    private ParseTree parseTree;
    private int inputBoxValueIndex = 0;
    private HashMap<String, String> inputBoxesValues;
    private ArrayList<String> inputBoxes;
    private double mouseX;
    private double mouseY;
    private boolean terminated = false;
    private boolean notActive = false;
    private Queue<Block> storedBlocks = new LinkedList<>();
    private int numberOfConditionBlocks = 0;
    private int numberOfWhileBlocks = 0;
    private boolean doOnce = false;



    /**
     * An empty constructor to create an Interpreter.
     * @throws FileNotFoundException
     */
    public Interpreter() throws FileNotFoundException {
        parseTree = new ParseTree();
    }

    /**
     * Passes the tree data and initialises the parse tree
     * @param treeData The tree data file
     * @throws UnsupportedEncodingException
     */
    public void loadTree(File treeData) throws UnsupportedEncodingException {
        parseTree.initialiseParseTree(treeData);
    }



    public ParseTree getParseTree() {
        return parseTree;
    }

    /**
     * Converts the text to a block representation.
     * @param text The text that is going to be converted.
     * @return The converted blocks in the queue.
     */
    public Queue<Block> textToBlocks(String text) {
        Queue blocks = new LinkedList();
        Scanner in = null;
        try {
            in = new Scanner(text);
            while(in.hasNext()) {
                String blockData = in.next();
                Block block = parseTree.getBlocks().get(blockData);
                blocks.add(block);

            }
        } catch (Exception e) {
            System.out.println("text stored for code is null");
        }
        System.out.println(blocks.toString());

        return  blocks;
    }

    /**
     * Checks the syntax of the program.
     * @param blocks The blocks that are going to be checked.
     * @return A boolean value representing if the syntax is correct or not.
     */
    public boolean checkSyntax(Queue<Block> blocks) {
        Queue<Block> syntaxBlocks = new LinkedList<>(blocks);
        int numberOfLoops = syntaxBlocks.size();
        TreeNode position = parseTree.root;
        Pair pair = new Pair(position, true);
         for(int i = 0; i < numberOfLoops; i++) {
             Block block = syntaxBlocks.remove();
             /**
             if(block.getName().equals("WHENEVER")) {
                 numberOfConditionBlocks++;
                 numberOfConditionBlocks++;
             } else if (block.getName().equals("CONDITION")) {
                 numberOfConditionBlocks--;
             } else if(block.getName().equals("THEN")) {
                 numberOfConditionBlocks--;
             } else if (block.getName().equals("LOOP")) {
                 numberOfLoops++;
             } else if (block.getName().equals("TERMINATE")) {
                 numberOfLoops--;
             }
              */


             pair = parseTree.compileProgram(block, (TreeNode) pair.getKey());
             if( (boolean) pair.getValue() == false) {
                 return false;
             }
         }
         if(numberOfConditionBlocks != 0) {
             System.out.println("Syntax Error Whenever and condition finished statemetn notmatchign");
             return false;
         }
         return (boolean) pair.getValue();
    }

    /**
     * Runs the Program.
     * @param spriteController The controller for the sprites.
     * @param xMouse The current x position of the mouse.
     * @param yMouse The current y position of the mouse.
     * @param inputBxsVls
     * @param inputBxs
     * @param variableManager The controller for the variables.
     * @param soundController The controller for the sound files.
     * @param sceneController the controller for the scenes.
     */
    public void compileAndRun(SpriteController spriteController, double xMouse, double yMouse, HashMap<String, String> inputBxsVls,
                              ArrayList<String> inputBxs, VariableManager variableManager,
                              SoundController soundController, SceneController sceneController) {
        inputBoxValueIndex = 0;
        inputBoxesValues = inputBxsVls;
        inputBoxes = inputBxs;
        mouseX = xMouse;
        mouseY = yMouse;
        doOnce = false;
        Queue<Block> blocks = new LinkedList<>(storedBlocks);
            while(blocks.size() > 0) {
                Block block = blocks.remove();
                String blockName = block.getName();
                spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
            }
        StageInitializer.playButton.setDisable(true);
        StageInitializer.stopButton.setDisable(false);
    }


    public SpriteController switchStatement(String blockName, Queue<Block> blocks, SpriteController spriteController,
                                            VariableManager variableManager, SoundController soundController, SceneController sceneController) {
        String direction = "";
        String content = "";
        Block block;
        Sprite tmpSprite;
        int index;
        Variable tmpVariable = null;
        Pair<Variable, Integer> variableIntegerPair = null;
        Pair<Sprite, Integer> spriteIntegerPair = null;
        switch (blockName) {
            case "MOVE":
                blocks.remove();
                block = blocks.remove();
                direction = block.getName();
                blocks.remove();

                spriteIntegerPair = getSprite(spriteController);
                tmpSprite = spriteIntegerPair.getKey();
                index = spriteIntegerPair.getValue();



                tmpSprite = moveSprite(tmpSprite, direction, getContent());
                spriteController.setSprite(index, tmpSprite);
                StageInitializer.setCurrentKey(null);
                break;
            case "END":
                System.out.println("Program finished");
                break;
            case "START":
                System.out.println("Program starting");
                break;
            case "FLIP":
                blocks.remove();
                direction = blocks.remove().getName();
                spriteIntegerPair = getSprite(spriteController);
                tmpSprite = spriteIntegerPair.getKey();
                index = spriteIntegerPair.getValue();
                tmpSprite = flipSprite(tmpSprite, direction);
                spriteController.setSprite(index, tmpSprite);
                StageInitializer.setCurrentKey(null);
                break;
            case "TELPORT":
                blocks.remove();
                blocks.remove();
                block = blocks.remove();
                String position = block.getName();
                spriteIntegerPair = getSprite(spriteController);
                tmpSprite = spriteIntegerPair.getKey();
                index = spriteIntegerPair.getValue();
                if (position.equals("X")) {
                    block = blocks.remove();
                    String xCoord = getContent();
                    String yCoord = getContent();

                    tmpSprite = teleportSprite(tmpSprite, xCoord, yCoord);
                } else if (position.equals("RANDOM")) {
                    tmpSprite = teleportSprite(tmpSprite);
                } else if (position.equals("MOUSE")) {
                    tmpSprite = teleportSprite(tmpSprite, mouseX, mouseY);
                } else if (position.equals("SPRITE")) {
                    spriteIntegerPair = getSprite(spriteController);
                    Sprite secondSprite = spriteIntegerPair.getKey();
                    tmpSprite = teleportSprite(tmpSprite, secondSprite);

                }

                spriteController.setSprite(index, tmpSprite);
                StageInitializer.setCurrentKey(null);
                break;
            case "ROTATE":
                blocks.remove();
                System.out.println(inputBoxesValues.size());
                String orientation = blocks.remove().getName();
                blocks.remove();

                spriteIntegerPair = getSprite(spriteController);
                tmpSprite = spriteIntegerPair.getKey();
                index = spriteIntegerPair.getValue();


                content = getContent();

                tmpSprite = rotateSprite(tmpSprite, orientation, content);

                spriteController.setSprite(index, tmpSprite);

                StageInitializer.setCurrentKey(null);
                break;
            case "PAUSE":
                content = getContent();
                pauseProgram(content);
                StageInitializer.setCurrentKey(null);
                break;
            case "WHENEVER":
            case "AND":
            case "OR":
               spriteController =  wheneverStatement(blocks, spriteController, variableManager,soundController, sceneController);
               break;
            case "CONDITION":
                blocks.remove();
                return  spriteController;
            case "ELSE":
                checkConditionFinished(blocks);
                break;
            case "LOOP":
                if(blocks.remove().getName().equals("ALWAYS")) {
                    StageInitializer.setEmptyLoopBlocks(getLoopBlocks(blocks));
                    StageInitializer.frameTimeline.playFromStart();
                    enterLoop(blocks);
                }
                break;
            case "TERMINATE":
                System.out.println("Reached Terminate");
                terminated = true;
                break;
            case "SET":
                blocks.remove();
                blocks.remove();
                blockName =  blocks.remove().getName();

                variableIntegerPair = getVariable(variableManager);
                tmpVariable = variableIntegerPair.getKey();
                index = variableIntegerPair.getValue();

               content = getContent();
                if(blockName.equals("ASK")) {
                    blocks.remove();
                    StageInitializer.askTerminalContent(content);

                } else {
                    tmpVariable.setValue(Integer.parseInt(content));
                }

                variableManager.getVariables().set(index, tmpVariable);

                break;
            case "VARIABLE":
                blocks.remove();
                variableIntegerPair = getVariable(variableManager);
                tmpVariable = variableIntegerPair.getKey();
                index = variableIntegerPair.getValue();
                int newValue = mathStatement(blocks, variableManager);
                tmpVariable.setValue(newValue);
                variableManager.getVariables().set(index, tmpVariable);
                break;
            case "SPEAK":
                blocks.remove();
                content = getContent();
                StageInitializer.setTerminalContent(content);

                break;
            case "CONTINUE":
                System.out.println("Detected continue block");
                break;
            case "PLAY":
                blocks.remove();
                content = getContent();

                soundController.playSound(content);
                break;
            case "LOOPS":
                blocks.remove();

               content = getContent();


                soundController.loopSound(content);
                break;
            case "INCREASE":
                blocks.remove();
                soundController.increaseVolume();
                break;
            case "DECREASE":
                blocks.remove();
                soundController.decreaseVolume();
                break;
            case "CHANGE":
                blocks.remove();

                String scene= getContent();

                sceneController.setChangeSceneTo(scene);
                break;
            case "DO":
                if(doOnce == false) {
                    switchStatement(blocks.remove().getName(), blocks, spriteController,
                            variableManager, soundController, sceneController);
                    doOnce = true;
                } else {
                    skipOnce(blocks);
                }
                break;
            case "ONCE":
                doOnce = true;
                break;
            default:
                System.out.println(blockName + " something went wrong");
                break;
        }
        return spriteController;
    }



    private static Sprite moveSprite(Sprite sprite, String direction, String number) {
        int steps = Integer.parseInt(number);
        if(direction.equals("RIGHT")) {
            sprite.setXPos(sprite.getXPos() + steps);
        } else if (direction.equals("LEFT")) {
            sprite.setXPos(sprite.getXPos() - steps);
        } else if (direction.equals("UP")) {
            sprite.setYPos(sprite.getYPos() - steps);
        } else if (direction.equals("DOWN")) {
            sprite.setYPos(sprite.getYPos() + steps);
        }

        return sprite;
    }

    /**
     * Flips the sprite in the respective direction.
     * @param sprite The sprite that will be flipped.
     * @param direction The direction in which the sprite wil be flipped.
     * @return The flipped sprite.
     */
    private Sprite flipSprite(Sprite sprite, String direction) {
      sprite.setSpriteOutfit(0, ImageProcessor.flipImage(sprite.getSpriteOutfit(0), direction));
        return sprite;
    }

    /**
     * Teleports the sprite at coordinates X,Y.
     * @param sprite The sprite that will be teleported.
     * @param x The new x pos of the sprite.
     * @param y The new y pos of the sprite.
     * @return The sprite with new X,Y coords.
     */
    private Sprite teleportSprite(Sprite sprite, String x, String y) {
        System.out.println(x);
        int xCoord = Integer.parseInt(x);
        int yCoord = Integer.parseInt(y);
        sprite.setXPos(xCoord);
        sprite.setYPos(yCoord);
        return sprite;

    }

    /**
     * Teleports the sprite in a random position.
     * @param sprite The sprite that will be teleported.
     * @return The sprite with random coords.
     */
    private Sprite teleportSprite(Sprite sprite) {
        Random rand = new Random();
        double  xPos = rand.nextDouble(728 - 50);
        double yPos = rand.nextDouble(597 - 50);
        sprite.setXPos(xPos);
        sprite.setYPos(yPos);

        return sprite;
    }


    /**
     * Teleports the sprite to the current mouse position.
     * @param sprite The sprite that will be teleported.
     * @param xPos The X pos of the mouse.
     * @param yPos The Y pos of the mouse.
     * @return The sprite with the coords set to the mouse position.
     */
    private Sprite teleportSprite(Sprite sprite, double xPos, double yPos) {
        System.out.println("---mouse--");
        if(yPos < 0) {
           yPos = 10;
        } else if (yPos > 597) {
            yPos = 550;
        }

        if(xPos < 0) {
            xPos = 10;
        } else if (xPos > 728) {
            xPos = 690;
        }
        sprite.setXPos(xPos);
        sprite.setYPos(yPos);
        return sprite;
    }


    /**
     * Rotates the sprite in a given orientation by a certain amount.
     * @param sprite The sprite that will be rotated.
     * @param orientation The orientation that the sprite will be rotated in.
     * @param amount The amount the sprite will be rotated by in degrees.
     * @return The rotated Sprite.
     */
    private  Sprite rotateSprite(Sprite sprite, String orientation, String amount) {
        sprite.setSpriteOutfit(0, ImageProcessor.rotateImage(sprite.getSpriteOutfit(0), orientation , amount));
        return sprite;
    }

    //add teleport to spirte

    private Sprite teleportSprite(Sprite sprite, Sprite sprite2) {
        double xPos = sprite2.getXPos();
        double yPos = sprite2.getYPos();
        sprite.setXPos(xPos);
        sprite.setYPos(yPos);
        return sprite;
    }

    /**
     * Pauses the running program.
     * @param number The amount of time the program will be paused for in seconds.
     */
    private static void pauseProgram(String number) {
        int delay = (Integer.parseInt(number) * 1000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    private SpriteController wheneverStatement(Queue<Block> blocks, SpriteController spriteController,
                                               VariableManager variableManager, SoundController soundController,
                                               SceneController sceneController) {
        String condition = blocks.remove().getName();
        String spriteName = "";
        String inputBoxAsString = "";
        String blockName = "";
        String content  = "";
        switch (condition) {
            case "NOT":
                notActive = true;
                spriteController = wheneverStatement(blocks, spriteController, variableManager, soundController, sceneController);
                break;
            case "PRESSES":
                blocks.remove();
                blockName = blocks.remove().getName();
                String key = getContent();

                KeyCode keyCondition = KeyCode.getKeyCode(key);
                System.out.println(keyCondition);
                System.out.println(StageInitializer.getCurrentKey());
                if((keyCondition == StageInitializer.getCurrentKey() && notActive == false) || ((!(keyCondition == StageInitializer.getCurrentKey())) && notActive == true)) {
                    System.out.println("Switch Active");
                    if(blockName.equals("THEN")) {
                        blockName = blocks.remove().getName();
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                    } else if (blockName.equals("AND")) {
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                    } else if(blockName.equals("OR")) {
                       spriteController = ORCondition(blocks, spriteController, variableManager, soundController, sceneController);
                    }
                    notActive = false;
                    return spriteController;
                } else if(blockName.equals("OR")) {
                    spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                }else {
                  checkConditionFinished(blocks);
                }
                break;
            case "HOVERS":
                blocks.remove();
                blockName = blocks.remove().getName();
                double[] mousePosition = StageInitializer.getMousePosition();
                mouseX = mousePosition[0];
                mouseY = mousePosition [1];
                int spriteIndex = spriteController.findSprite(mouseX, mouseY);
                String tmpSpriteName = "";
                if(spriteIndex != -1) {
                    tmpSpriteName = spriteController.getSprite(spriteIndex).getSpriteName();
                }

                spriteName = getContent();

                if((tmpSpriteName.equals(spriteName) && notActive == false) ||
                        (!(tmpSpriteName.equals(spriteName)) && notActive == true)) {
                    notActive = false;
                    System.out.println("Active");
                    if(blockName.equals("THEN")) {
                        blockName = blocks.remove().getName();
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager,soundController, sceneController);
                        return spriteController;
                    } else if(blockName.equals("AND")) {
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                    } else if (blockName.equals("OR")) {
                        spriteController = ORCondition(blocks, spriteController, variableManager, soundController, sceneController);
                    }
                    return spriteController;
                } else if(blockName.equals("OR")) {
                    spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                } else {
                    checkConditionFinished(blocks);
                }
                break;
            case "CLICKS":
                blocks.remove();
                blockName = blocks.remove().getName();
                spriteName = getContent();

                Sprite tmpSprite = null;
                boolean found = false;
                int j = 0;
                while(found == false) {
                    if((spriteController.getSprite(j).getSpriteName().equals(spriteName))) {
                        tmpSprite = spriteController.getSprite(j);
                        if((tmpSprite.isClicked() && notActive == false) ||
                                (!(tmpSprite.isClicked()) && notActive == true) ) {
                            tmpSprite.setClicked(false);
                            if(blockName.equals("THEN")) {
                                blockName = blocks.remove().getName();
                                spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);

                                spriteController.setSprite(j,tmpSprite);
                                return spriteController;
                            } else if(blockName.equals("AND")){
                                spriteController = switchStatement(blockName, blocks,  spriteController, variableManager, soundController, sceneController);
                            } else if (blockName.equals("OR")) {
                                spriteController = ORCondition(blocks, spriteController, variableManager, soundController, sceneController);
                            }
                            notActive = false;
                            return spriteController;
                        } else if(blockName.equals("OR")) {
                            spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                        } else {
                           checkConditionFinished(blocks);

                        }
                        found = true;
                    }
                    j++;

                }
                break;
            case "VARIABLE":
                Pair<Variable, Integer> variableIntegerPair = getVariable(variableManager);
                Variable tmpVariable = variableIntegerPair.getKey();
                int variableValue = tmpVariable.getValue();

               String checkEquals=  blocks.remove().getName();
               if(checkEquals.equals("EQUALS")) {
                   blockName = blocks.remove().getName();

                   if(blockName.equals("NUMBER")) {

                       content = getContent();

                       String secondBlockName = blocks.remove().getName();
                       if(secondBlockName.equals("THEN")) {

                           blockName = blocks.remove().getName();
                           if(variableValue == Integer.parseInt(content)) {
                               if(blockName.equals("THEN")) {
                                   blockName = blocks.remove().getName();
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               } else if(blockName.equals("AND")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               } else if (blockName.equals("OR")) {
                                   spriteController = ORCondition(blocks, spriteController, variableManager, soundController, sceneController);
                               }
                           } else if(blockName.equals("OR")) {
                               spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                           }else {
                               checkConditionFinished(blocks);
                           }
                       } else if(secondBlockName.equals("ADD")) {
                           blockName = blocks.remove().getName();
                           if(blockName.equals("NUMBER")) {

                               String secondContent = getContent();

                               if(variableValue == (Integer.parseInt(content) + Integer.parseInt(secondContent))) {
                                   spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController, sceneController);
                               } else if(blockName.equals("OR")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               }else {
                                   checkConditionFinished(blocks);
                               }


                           }
                       } else if (secondBlockName.equals("SUBTRACT")) {
                           blockName = blocks.remove().getName();
                           if(blockName.equals("NUMBER")) {

                               String secondContent = getContent();

                               if(variableValue == (Integer.parseInt(content) - Integer.parseInt(secondContent))) {
                                   spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController, sceneController);
                               } else if(blockName.equals("OR")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               }else {
                                   checkConditionFinished(blocks);
                               }


                           }

                       } else if((secondBlockName.equals("MULTIPLY"))) {
                           blockName = blocks.remove().getName();
                           if(blockName.equals("NUMBER")) {
                               String secondContent = getContent();

                               if(variableValue == (Integer.parseInt(content) * Integer.parseInt(secondContent))) {
                                   spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController, sceneController);
                               } else if(blockName.equals("OR")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager,soundController, sceneController);
                               }else {
                                   checkConditionFinished(blocks);
                               }


                           }
                       } else if((secondBlockName.equals("DIVIDE"))) {
                           blockName = blocks.remove().getName();
                           if(blockName.equals("NUMBER")) {

                               String secondContent = getContent();

                               if(variableValue == (Integer.parseInt(content) / Integer.parseInt(secondContent))) {
                                   spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController,sceneController);
                               } else if(blockName.equals("OR")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               }else {
                                   checkConditionFinished(blocks);
                               }


                           }
                       } else if((secondBlockName.equals("MODULUS"))) {
                           blockName = blocks.remove().getName();
                           if(blockName.equals("NUMBER")) {

                               String secondContent = getContent();

                               if(variableValue == (Integer.parseInt(content) % Integer.parseInt(secondContent))) {
                                   spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController,sceneController);
                               } else if(blockName.equals("OR")) {
                                   spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                               }else {
                                   checkConditionFinished(blocks);
                               }


                           }
                       }

                   }

               } else {
                   blocks.remove();
                   blocks.remove();
                   inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                   content = inputBoxesValues.get(inputBoxAsString);
                   inputBoxValueIndex++;
                   if(checkEquals.equals("LESS")) {
                       if(variableValue < Integer.parseInt(content)) {
                           spriteController = performWheneverStatement(blocks, spriteController, variableManager, soundController, sceneController);
                       } else if(blockName.equals("OR")) {
                           spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
                       }else {
                           checkConditionFinished(blocks);
                       }
                   } else if(checkEquals.equals("GREATER")) {
                       if(variableValue > Integer.parseInt(content)) {
                           spriteController = performWheneverStatement(blocks, spriteController, variableManager,soundController,sceneController);
                       } else if(blockName.equals("OR")) {
                           spriteController = switchStatement(blockName, blocks, spriteController, variableManager,soundController,sceneController);
                       }else {
                           checkConditionFinished(blocks);
                       }
                   }

               }

                break;
            default:
                System.out.println("something went wrong within condition");
                break;

        }
        return  spriteController;
    }

    private SpriteController performWheneverStatement(Queue<Block> blocks, SpriteController spriteController, VariableManager variableManager, SoundController soundController, SceneController sceneController) {
        String blockName;
        blockName = blocks.remove().getName();
        if(blockName.equals("THEN")) {
            blockName = blocks.remove().getName();
            spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
        } else if(blockName.equals("AND")) {
            spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
        } else if (blockName.equals("OR")) {
            spriteController = ORCondition(blocks, spriteController, variableManager,soundController, sceneController);
        }
        return spriteController;
    }

    private int mathStatement(Queue<Block> blocks, VariableManager variableManager){
        String firstNumber = "";
        String secondNumber = "";
        String inputBoxAsString = "";
        int finalNumber = 0;
        Pair<Variable, Integer> variableIntegerPair = null;
        Variable tmpVariable = null;
        if(blocks.remove().getName().equals("NUMBER")) {
            inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
            firstNumber = inputBoxesValues.get(inputBoxAsString);
            inputBoxValueIndex++;
        } else {
            variableIntegerPair = getVariable(variableManager);
            tmpVariable = variableIntegerPair.getKey();
            firstNumber = String.valueOf(tmpVariable.getValue());
        }
       String operation = blocks.remove().getName();
        if(blocks.remove().getName().equals("NUMBER")) {
            inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
            secondNumber = inputBoxesValues.get(inputBoxAsString);
            inputBoxValueIndex++;
        } else {
            variableIntegerPair = getVariable(variableManager);
            tmpVariable = variableIntegerPair.getKey();
            secondNumber = String.valueOf(tmpVariable.getValue());
        }
        if(operation.equals("ADD")) {
            finalNumber = Integer.parseInt(firstNumber) + Integer.parseInt(secondNumber);
        } else if(operation.equals("SUBTRACT")) {
            finalNumber = Integer.parseInt(firstNumber) - Integer.parseInt(secondNumber);
        } else if (operation.equals("MULTIPLY")) {
            finalNumber = Integer.parseInt(firstNumber) * Integer.parseInt(secondNumber);
        } else if (operation.equals("DIVIDE")) {
            finalNumber = Integer.parseInt(firstNumber) / Integer.parseInt(secondNumber);
        } else if (operation.equals("MODULUS")) {
            finalNumber = Integer.parseInt(firstNumber) % Integer.parseInt(secondNumber);
        }

        return  finalNumber;
    }

    private void checkConditionFinished(Queue<Block> blocks) {
        String blockName = "";
        while ((!(blockName.equals("FINISHED"))) && (!(blockName.equals("ELSE"))) ) {
            blockName = blocks.remove().getName();
            if(blockName.equals("KEY") || blockName.equals("SPRITE") || blockName.equals("NUMBER")) {
                inputBoxValueIndex++;
            }
        }

    }

    private SpriteController ORCondition(Queue<Block> blocks, SpriteController spriteController, VariableManager variableManager , SoundController soundController, SceneController sceneController) {
       String blockName = "PRESSES";
        while(blockName.equals("PRESSES") || blockName.equals("KEY") || blockName.equals("CLICKS")
                || blockName.equals("SPRITE") || blockName.equals("HOVERS") || blockName.equals("NOT") || blockName.equals("THEN")) {
            blockName = blocks.remove().getName();
            if(blockName.equals("SPRITE") || blockName.equals("KEY")) {
                inputBoxValueIndex++;
            }
        }
        spriteController = switchStatement(blockName, blocks, spriteController, variableManager, soundController, sceneController);
        return spriteController;
    }

    private void skipOnce(Queue<Block> blocks) {
        boolean skipped = false;
        while (!skipped) {
            if(blocks.remove().getName().equals("ONCE")) {
                skipped = true;
            }
        }
    }



    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    private Queue<Block> getLoopBlocks(Queue<Block> blocks) {
        String blockName = "";
        Queue<Block> loopBlocks = new LinkedList<>();
        while(!(blockName.equals("TERMINATE"))) {
            Block block = blocks.remove();
            loopBlocks.add(block);
            blockName = block.getName();
        }

        return loopBlocks;
    }

    private void enterLoop(Queue<Block> blocks) {
        while(!blocks.isEmpty()) {
            blocks.remove();
        }
    }

    public int getInputBoxValueIndex() {
        return inputBoxValueIndex;
    }

    public void setInputBoxValueIndex(int inputBoxValueIndex) {
        this.inputBoxValueIndex = inputBoxValueIndex;
    }

    public void loadBlocks(Queue<Block> storedBlocks) {
        this.storedBlocks = storedBlocks;
    }

    private Pair<Sprite, Integer> getSprite(SpriteController spriteController) {
       String inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
        String spriteName = inputBoxesValues.get(inputBoxAsString);
        inputBoxValueIndex++;
        Sprite tmpSprite = null;
        boolean found = false;
        int count = -1;

        while(!found) {
            count++;
            if(spriteController.getSprite(count).getSpriteName().equals(spriteName)) {
                tmpSprite = spriteController.getSprite(count);
                found = true;
            }
        }
        Pair<Sprite, Integer> spriteIntegerPair = new Pair<>(tmpSprite, count);
        return spriteIntegerPair;
    }

    private Pair<Variable, Integer> getVariable(VariableManager variableManager) {
        String inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
        String variableName = inputBoxesValues.get(inputBoxAsString);
        inputBoxValueIndex++;
        Variable tmpVariable = null;
        boolean found = false;
        int count = -1;

        while(!found) {
            count++;
            if(variableManager.getVariables().get(count).getName().equals(variableName)) {
                tmpVariable = variableManager.getVariables().get(count);
                found = true;
            }
        }
        Pair<Variable, Integer> variableIntegerPair = new Pair<>(tmpVariable, count);
        return variableIntegerPair;
    }

    private String getContent() {
        String  inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
        String content = inputBoxesValues.get(inputBoxAsString);
        inputBoxValueIndex++;
        return content;
    }


}
