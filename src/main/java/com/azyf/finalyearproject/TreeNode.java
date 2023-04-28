package com.azyf.finalyearproject;

/**
 * This class represents a singular node within a Tree.
 * @author Hussain Asif.
 * @version 1.0.
 */

import org.apache.pdfbox.debugger.ui.Tree;

import java.util.ArrayList;

public class TreeNode {

    private Block node;
    private ArrayList<TreeNode> children;

    /**
     * Constructs a node with the parent node.
     * @param node The parent node.
     */
    public TreeNode(Block node) {
        this.node = node;
        children = new ArrayList<>();
    }

    /**
     * Gets the parent node.
     * @return The parent node.
     */
    public Block getNode() {
        return node;
    }

    /**
     * Gets the index of the category.
     * @param nodeCategory the category to find the index of.
     * @return The index of the category.
     */
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

    /**
     * add a node to the parent.
     * @param child The child node.
     */
    public void addChildNode(TreeNode child) {
        children.add(child);
    }

    /**
     * Gets the children of the parent node.
     * @return An arraylist of children.
     */
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
