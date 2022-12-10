package com.azyf.finalyearproject;

import com.google.protobuf.compiler.PluginProtos;
import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.pdfbox.debugger.ui.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.azyf.finalyearproject.ImageProcessor.flipImage;


public class Interpreter {
    private ParseTree parseTree;

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

    public void compileAndRun(SpriteController spriteController, double mouseX, double mouseY) {
        for(int i = 0; i < spriteController.size(); i++) {
            Sprite sprite = spriteController.getSprite(i);
            Queue<Block> blocks = new LinkedList<>(spriteController.getSpriteCodeBlocks(0));

                while(blocks.size() > 0) {
                Block block = blocks.remove();
                String blockName = block.getName();
                switch (blockName) {
                    case "MOVE":
                        block = blocks.remove();
                        String direction = block.getName();

                        block = blocks.remove();
                        String steps = block.getName();

                       sprite = moveSprite(sprite, direction , steps);
                       spriteController.setSprite(i, sprite);
                        break;
                    case "END":
                        System.out.println("Program finished");
                        break;
                    case "START":
                        System.out.println("Program starting");
                        break;
                    case "FLIP":
                        sprite = flipSprite(sprite);
                        spriteController.setSprite(i, sprite);
                        break;
                    case "TELEPORT":
                        blocks.remove();
                        block = blocks.remove();
                        String position = block.getName();
                        if (position.equals("X")) {
                            block = blocks.remove();
                            String yCoord = block.getName();
                            sprite =  teleportSprite(sprite, position, yCoord);
                        } else if (position.equals("RANDOM")) {
                            sprite =  teleportSprite(sprite);
                        } else if (position.equals("Mouse")) {
                            sprite = teleportSprite(sprite, mouseX, mouseY);
                        }
                        spriteController.setSprite(i, sprite);
                        break;

                    default:
                        System.out.println("something went wrong");
                        break;
                }


            }
        }
    }



    private static Sprite moveSprite(Sprite sprite, String direction, String number) {
        int steps = 20;
        if(direction.equals("RIGHT")) {
            sprite.setXPos(sprite.getXPos() + steps);
        } else if (direction.equals("LEFT")) {
            sprite.setXPos(sprite.getXPos() - steps);
        }

        return sprite;
    }

    private static Sprite flipSprite(Sprite sprite) {
      sprite.setSpriteOutfit(0, ImageProcessor.flipImage(sprite.getSpriteOutfit(0)) );
        return sprite;
    }

    private static Sprite teleportSprite(Sprite sprite, String x, String y) {
        sprite.setXPos(20.0);
        sprite.setYPos(50.0);
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

    //add teleport spirte

}
