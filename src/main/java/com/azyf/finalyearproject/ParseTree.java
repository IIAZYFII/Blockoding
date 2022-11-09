package com.azyf.finalyearproject;
import org.apache.pdfbox.debugger.ui.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ParseTree {
    private TreeNode root = null;

    public ParseTree() {

    }

    public void initialiseParseTree(Block block) {
        TreeNode blockNode   = new TreeNode(block);
        if(root == null) {
            root = blockNode;
        } else {
            insertNodeIntoParseTree(blockNode);
        }
    }

    private void insertNodeIntoParseTree(TreeNode blockNode) {


    }
}


