package com.azyf.finalyearproject;

import com.google.protobuf.compiler.PluginProtos;
import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.pdfbox.debugger.ui.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


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

    public void compileAndRun(SpriteController spriteController) {
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
                    default:
                        System.out.println("something went wrong");
                        break;
                }


            }
        }
    }



    private Sprite moveSprite(Sprite sprite, String direction, String number) {
        int steps = 20;
        if(direction.equals("RIGHT")) {
            sprite.setXPos(sprite.getXPos() + steps);
        } else if (direction.equals("LEFT")) {
            sprite.setXPos(sprite.getXPos() - steps);
        }

        return sprite;
    }

    private Sprite flipSprite(Sprite sprite, String direction) {
      if(direction.equals("RIGHT")) {
          sprite.setFlipRight(true);
      } else if (direction.equals("LEFT")) {
          sprite.setFlipLeft(true);
      }
        return sprite;
    }


}
