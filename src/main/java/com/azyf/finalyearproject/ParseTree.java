package com.azyf.finalyearproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class represent the tangible programming language using the tree data structure.
 *
 * @author  Hussain Asif
 */
public class  ParseTree {
    public TreeNode root = null;
    private TreeNode currentParentNode = null;
    private HashMap<String,Block> Blocks = new HashMap<>();

    /**
     * An empty constructor to create an empty Parse Tree
     * @throws FileNotFoundException
     */
    public ParseTree() throws FileNotFoundException {
        loadBlocks();
    }

    /**
     * Loads blocks inside the dictionary.
     * @throws FileNotFoundException
     */
    private void loadBlocks() throws FileNotFoundException {
        File blockFile = new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Blocks\\Default.txt");
        Scanner in = null;
        Scanner inLine = null;
        try {
            in = new Scanner(blockFile);
            int index = 0;
            while (in.hasNextLine()) {
                String blockLine = in.nextLine();
                inLine = new Scanner(blockLine);
                while(inLine.hasNext()) {
                    String blockName = inLine.next();
                    Category blockCategory = Category.valueOf(inLine.next());
                    Block newBlock = new Block(blockName, blockCategory);
                    Blocks.put(blockName, newBlock);
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("cannot find file:" + blockFile.getName());
        }


    }

    /**
     * Starts constructing the tree and filling in the parse tree.
     * @param treeData The data that the tree will be constructed from.
     * @throws UnsupportedEncodingException
     */
    public void initialiseParseTree(File treeData) throws UnsupportedEncodingException {
        Scanner in = null;
        String parseTree = "";
        try {
            in = new Scanner(treeData);
           parseTree =  in.nextLine();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find " + treeData.getName());
        }
        System.out.println(parseTree);
        in = new Scanner(parseTree);
        while(in.hasNext()) {
            String data = in.next();
            if(root == null) {
                Block rootBlock = Blocks.get(data);
                TreeNode rootNode = new TreeNode(rootBlock);
                root = rootNode;
                currentParentNode = root;
            } else if (data.equals("->")) {
               String childNotation = in.next();
                if(childNotation.equals("[")) {
                    String  blockData = in.next();
                    Block currentBlock = Blocks.get(blockData);
                    TreeNode newNode =  new TreeNode(currentBlock);
                    currentParentNode.getChildren().add(newNode);
                    currentParentNode = newNode;
                } else {
                    throw new UnsupportedEncodingException("expected [" + "actually encoded: " + childNotation);
                }
            } else if (data.equals("]")) {
                 setCurrentParentNode(currentParentNode,root);
                System.out.println("no more children for " +  currentParentNode.getNode().getName());


            }  else {
                Block currentBlock = Blocks.get(data);
                TreeNode newNode =  new TreeNode(currentBlock);
                 setCurrentParentNode(currentParentNode,root);
                currentParentNode.getChildren().add(newNode);
                currentParentNode = newNode;
            }
        }

    }

    /**
     * Sets the current parent Node depending on the position of the latest node added.
     * @param checkNode The child of the parent node that will be set.
     * @param position The current position within the Tree.
     */
    private void setCurrentParentNode(TreeNode checkNode, TreeNode position) {
        if(checkNode == root) {
            currentParentNode = root;
        } else {
            for(int i = 0; i < position.getChildren().size(); i++) {
               TreeNode treeNode = position.getChildren().get(i);
               if(checkNode == treeNode) {
                     currentParentNode = position;
                } else if (treeNode.getChildren().size() != 0) {
                    setCurrentParentNode(checkNode, treeNode);
                }
            }
        }
    }





}


