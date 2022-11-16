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

    public int categoryIndex(Category nodeCategory) {
        int index = 0;
        for(int i = 0; i < children.size(); i++) {
           Category currentCategory = children.get(i).getNode().category;
           if(currentCategory == nodeCategory) {
               index = i;
           }
        }
        return index;
    }

    public void addChildNode(TreeNode child) {
        children.add(child);
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "TreeNode{" + node +
                ", children=" + children +
                '}';
    }
}
