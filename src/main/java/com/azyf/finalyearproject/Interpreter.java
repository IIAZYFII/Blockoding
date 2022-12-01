package com.azyf.finalyearproject;

import com.google.protobuf.compiler.PluginProtos;
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

    public Interpreter() throws FileNotFoundException {
        parseTree = new ParseTree();
    }

    public void runCode() {
    }


    public void loadTree(File treeData) throws UnsupportedEncodingException {
        parseTree.initialiseParseTree(treeData);
    }


/*
    public void loadTree(File treeData) {

        Scanner in = null;
        try {
            in = new Scanner(treeData);
            Boolean testBool = true;
            while(in.hasNext()) {
                String currentNode = in.next();
                String subString = currentNode.substring(0,2);
               // System.out.println(subString);

                if(subString.equals("</")) {

                } else {
                   String blockName = currentNode.substring(1,currentNode.length() -1 );
                    Block currentBlock = Blocks.get(blockName);
                    parseTree.initialiseParseTree(currentBlock);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + treeData.getName());
        }
    }

 */


    public ParseTree getParseTree() {
        return parseTree;
    }

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

    public void compile(Queue<Block> blocks) {
        TreeNode position = parseTree.root;
        Pair pair = new Pair(position, true);
         for(int i = 0; i < blocks.size(); i++) {
             Block block = blocks.remove();
             pair = parseTree.compileProgram(block, (TreeNode) pair.getKey());
         }
    }


}
