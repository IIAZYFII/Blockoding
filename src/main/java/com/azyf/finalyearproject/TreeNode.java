package com.azyf.finalyearproject;

import org.apache.pdfbox.debugger.ui.Tree;

import java.util.ArrayList;

public class TreeNode {
    private Block node;
    private ArrayList<TreeNode> children;

    public TreeNode(Block node) {
        this.node = node;
        children = new ArrayList<>();
    }

    public Block getNode() {
        return node;
    }

    public void addChildNode(TreeNode child) {
        children.add(child);
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }
}
