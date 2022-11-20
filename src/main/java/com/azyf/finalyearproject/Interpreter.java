package com.azyf.finalyearproject;

import com.google.protobuf.compiler.PluginProtos;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
}