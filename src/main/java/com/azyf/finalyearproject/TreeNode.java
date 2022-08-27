package com.azyf.finalyearproject;

public class TreeNode {
    private Block node;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(Block node) {
        this.node = node;
    }

    public Block getNode() {
        return node;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
