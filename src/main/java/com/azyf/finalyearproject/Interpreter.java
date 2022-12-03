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


}
