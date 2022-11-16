package com.azyf.finalyearproject;
import org.apache.pdfbox.debugger.ui.Tree;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;


public class  ParseTree {
    public TreeNode root = null;
    private final static int NUMBER_OF_BLOCKS = 51;
    private HashMap<String,Block> Blocks = new HashMap<>();

    public ParseTree() throws FileNotFoundException {
        loadBlocks();
    }


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
        int currentBlockIndex  = 0;
        TreeNode currentNode = null;
        while(in.hasNext()) {
            String data = in.next();
            if(root == null) {
                Block rootBlock = Blocks.get(data);
                TreeNode rootNode = new TreeNode(rootBlock);
                root = rootNode;
                currentNode = root;
            } else if (data.equals("->")) {
               String childNotation = in.next();
                if(childNotation.equals("[")) {
                    String  blockData = in.next();
                    Block currentBlock = Blocks.get(blockData);
                    TreeNode newNode =  new TreeNode(currentBlock);
                    currentNode.getChildren().add(newNode);
                    currentNode = newNode;
                } else {
                    throw new UnsupportedEncodingException("expected [" + "actually encoded: " + childNotation);
                }
            } else if (data.equals("]")) {
                currentNode = findParent(currentNode,root);
                System.out.println("no more children for " +  currentNode.getNode().getName());


            }  else {
                Block currentBlock = Blocks.get(data);
                TreeNode newNode =  new TreeNode(currentBlock);
                currentNode = findParent(currentNode,root);
                currentNode.getChildren().add(newNode);
                currentNode = newNode;
            }
        }

    }

    private TreeNode findParent(TreeNode checkNode, TreeNode position) {
        if(checkNode == root) {
            return  root;
        } else {
            for(int i = 0; i < position.getChildren().size(); i++) {
               TreeNode treeNode = position.getChildren().get(i);
               if(checkNode == treeNode) {
                    return position;
                } else if (treeNode.getChildren().size() != 0) {
                    findParent(checkNode, treeNode);
                }
            }
        }
        return position;
    }

/*
    private void insertNodeIntoParseTree(TreeNode blockNode) {
        Category NodeCategory =  blockNode.getNode().getCategory();
        if(root.getChildren().size() == 0) {
            root.addChildNode(blockNode);
        } else {
            int index = returnIndex(blockNode);
            if(index == -1) {
                root.getChildren().add(blockNode);
            } else  {
                root.getChildren().get(index).getChildren().add(blockNode);
            }
        }


    }

 */
/*
    public int returnIndex(TreeNode nodeCategory) {

        if(root.getChildren().size() == 0) {
            return - 1;
        } else {
            for(int i = 0; i < root.getChildren().size(); i++) {
                if(root.getChildren().get(i).getNode().getCategory() == nodeCategory.getNode().getCategory()) {
                    return i;
                }

            }
        }
        return - 1;

    }

 */

     /*public boolean doesCategoryExist(TreeNode currentNode, Category NodeCategory) {
        if(currentNode.getChildren() != null) {
            if(currentNode.getNode().getCategory() == NodeCategory) {
                return true;
            }
            for(int i = 0; i < currentNode.getChildren().size(); i++) {
                this.doesCategoryExist(currentNode.getChildren().get(i), NodeCategory);
            }
        }

        return false;
    }


      */



}


