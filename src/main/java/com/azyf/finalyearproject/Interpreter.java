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
        TreeNode position = parseTree.root;
        Pair pair = new Pair(position, true);
         for(int i = 0; i < blocks.size(); i++) {
             Block block = blocks.remove();
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
            for(int j = 0; j < spriteController.getSpriteCodeBlocks(i).size(); j ++) {
                Block block = spriteController.getSpriteCodeBlocks(i).remove();
                String blockName = block.getName();
                switch (blockName) {
                    case "MOVE":
                        block = spriteController.getSpriteCodeBlocks(i).remove();
                        blockName = block.getName();
                       sprite = moveSprite(sprite, Integer.parseInt(blockName));
                       spriteController.setSprite(i, sprite);
                        break;
                    case "FLIP":
                        block = spriteController.getSpriteCodeBlocks(i).remove();
                        blockName = block.getName();
                        sprite = flipSprite(sprite, blockName);
                        spriteController.setSprite(i, sprite);
                }


            }
        }
    }



    private Sprite moveSprite(Sprite sprite, int steps) {
        steps = 20;
        sprite.setXPos(sprite.getXPos() + steps);
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
