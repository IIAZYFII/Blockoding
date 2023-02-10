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



    /**
     * An empty constructor to create an Interpreter.
     * @throws FileNotFoundException
     */
    public Interpreter() throws FileNotFoundException {
        parseTree = new ParseTree();
    }

    public void runCode() {
    }


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
             pair = parseTree.compileProgram(block, (TreeNode) pair.getKey());
             if( (boolean) pair.getValue() == false) {
                 return false;
             }
         }
         return (boolean) pair.getValue();
    }

    public void compileAndRun(SpriteController spriteController, double xMouse, double yMouse, HashMap<String, String> inputBxsVls,
                              ArrayList<String> inputBxs, VariableManager variableManager) {
        inputBoxValueIndex = 0;
        inputBoxesValues = inputBxsVls;
        inputBoxes = inputBxs;
        mouseX = xMouse;
        mouseY = yMouse;
        Queue<Block> blocks = new LinkedList<>(storedBlocks);
            while(blocks.size() > 0) {
                Block block = blocks.remove();
                String blockName = block.getName();
                spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
            }
        StageInitializer.playButton.setDisable(true);
        StageInitializer.stopButton.setDisable(false);
    }


    public SpriteController switchStatement(String blockName, Queue<Block> blocks, SpriteController spriteController, VariableManager variableManager) {
        String direction = "";
        String inputBoxAsString = "";
        String amount = "";
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

                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                String steps = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;



                tmpSprite = moveSprite(tmpSprite, direction, steps);
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
                if (position.equals("X")) {
                    block = blocks.remove();
                    inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                    String xCoord = inputBoxesValues.get(inputBoxAsString);
                    inputBoxValueIndex++;

                    inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                    String yCoord = inputBoxesValues.get(inputBoxAsString);
                    inputBoxValueIndex++;




                    tmpSprite = teleportSprite(tmpSprite, xCoord, yCoord);
                } else if (position.equals("RANDOM")) {
                    tmpSprite = teleportSprite(tmpSprite);
                } else if (position.equals("MOUSE")) {
                    tmpSprite = teleportSprite(tmpSprite, mouseX, mouseY);
                }
                index = spriteIntegerPair.getValue();
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


                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                amount = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;

                tmpSprite = rotateSprite(tmpSprite, orientation, amount);

                spriteController.setSprite(index, tmpSprite);

                StageInitializer.setCurrentKey(null);
                break;
            case "PAUSE":
                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                amount = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;
                pauseProgram(amount);
                StageInitializer.setCurrentKey(null);
                break;
            case "WHENEVER":
            case "AND":
            case "OR":
               spriteController =  wheneverStatement(blocks, spriteController, variableManager);
               break;
            case "CONDITION":
                blocks.remove();
                return  spriteController;
            case "ELSE":
                checkConditionFinished(blocks);
                break;
            case "LOOP":
                StageInitializer.setEmptyLoopBlocks(getLoopBlocks(blocks));
                StageInitializer.frameTimeline.playFromStart();
                enterLoop(blocks);
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

                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                amount = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;

                if(blockName.equals("ASK")) {
                    tmpVariable.setContent(amount);
                } else {
                    tmpVariable.setValue(Integer.parseInt(amount));
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
                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                amount = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;
                StageInitializer.setTerminalContent(amount);

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

    private static Sprite flipSprite(Sprite sprite, String direction) {
      sprite.setSpriteOutfit(0, ImageProcessor.flipImage(sprite.getSpriteOutfit(0), direction));
        return sprite;
    }

    private static Sprite teleportSprite(Sprite sprite, String x, String y) {
        System.out.println(x);
        int xCoord = Integer.parseInt(x);
        int yCoord = Integer.parseInt(y);
        sprite.setXPos(xCoord);
        sprite.setYPos(yCoord);
        return sprite;

    }

    private static Sprite teleportSprite(Sprite sprite) {
            Random rand = new Random();
            double  xPos = rand.nextDouble(728 - 50);
            double yPos = rand.nextDouble(597 - 50);
        sprite.setXPos(xPos);
        sprite.setYPos(yPos);

        return sprite;
    }

    private static Sprite teleportSprite(Sprite sprite, double xPos, double yPos) {
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

    private static Sprite rotateSprite(Sprite sprite, String orientation, String amount) {
        sprite.setSpriteOutfit(0, ImageProcessor.rotateImage(sprite.getSpriteOutfit(0), orientation , amount));
        return sprite;
    }

    //add teleport to spirte

    private static void pauseProgram(String number) {
        int delay = (Integer.parseInt(number) * 1000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    private SpriteController wheneverStatement(Queue<Block> blocks, SpriteController spriteController, VariableManager variableManager) {
        String condition = blocks.remove().getName();
        String spriteName = "";
        String inputBoxAsString = "";
        String blockName = "";
        switch (condition) {
            case "NOT":
                notActive = true;
                spriteController = wheneverStatement(blocks, spriteController, variableManager);
                break;
            case "PRESSES":
                blocks.remove();
                blockName = blocks.remove().getName();
                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                String key = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;
                KeyCode keyCondition = KeyCode.getKeyCode(key);
                System.out.println(keyCondition);
                System.out.println(StageInitializer.getCurrentKey());
                if((keyCondition == StageInitializer.getCurrentKey() && notActive == false) || ((!(keyCondition == StageInitializer.getCurrentKey())) && notActive == true)) {
                    System.out.println("Switch Active");
                    if(blockName.equals("THEN")) {
                        blockName = blocks.remove().getName();
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                    } else if (blockName.equals("AND")) {
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                    } else if(blockName.equals("OR")) {
                       spriteController = ORCondition(blocks, spriteController, variableManager);
                    }
                    notActive = false;
                    return spriteController;
                } else if(blockName.equals("OR")) {
                    spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
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


                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                spriteName = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;

                if((tmpSpriteName.equals(spriteName) && notActive == false) ||
                        (!(tmpSpriteName.equals(spriteName)) && notActive == true)) {
                    notActive = false;
                    System.out.println("Active");
                    if(blockName.equals("THEN")) {
                        blockName = blocks.remove().getName();
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                        return spriteController;
                    } else if(blockName.equals("AND")) {
                        spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                    } else if (blockName.equals("OR")) {
                        spriteController = ORCondition(blocks, spriteController, variableManager);
                    }
                    return spriteController;
                } else if(blockName.equals("OR")) {
                    spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                } else {
                    checkConditionFinished(blocks);
                }
                break;
            case "CLICKS":
                blocks.remove();
                blockName = blocks.remove().getName();
                inputBoxAsString = inputBoxes.get(inputBoxValueIndex);
                spriteName = inputBoxesValues.get(inputBoxAsString);
                inputBoxValueIndex++;
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
                                spriteController = switchStatement(blockName, blocks, spriteController, variableManager);

                                spriteController.setSprite(j,tmpSprite);
                                return spriteController;
                            } else if(blockName.equals("AND")){
                                spriteController = switchStatement(blockName, blocks,  spriteController, variableManager);
                            } else if (blockName.equals("OR")) {
                                spriteController = ORCondition(blocks, spriteController, variableManager);
                            }
                            notActive = false;
                            return spriteController;
                        } else if(blockName.equals("OR")) {
                            spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
                        } else {
                           checkConditionFinished(blocks);

                        }
                        found = true;
                    }
                    j++;

                }

                break;

            default:
                System.out.println("something went wrong within condition");
                break;

        }
        return  spriteController;
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

    private SpriteController ORCondition(Queue<Block> blocks, SpriteController spriteController, VariableManager variableManager) {
       String blockName = "PRESSES";
        while(blockName.equals("PRESSES") || blockName.equals("KEY") || blockName.equals("CLICKS")
                || blockName.equals("SPRITE") || blockName.equals("HOVERS") || blockName.equals("NOT") || blockName.equals("THEN")) {
            blockName = blocks.remove().getName();
            if(blockName.equals("SPRITE") || blockName.equals("KEY")) {
                inputBoxValueIndex++;
            }
        }
        spriteController = switchStatement(blockName, blocks, spriteController, variableManager);
        return spriteController;
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


}
