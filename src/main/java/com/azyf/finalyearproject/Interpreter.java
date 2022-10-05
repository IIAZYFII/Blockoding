package com.azyf.finalyearproject;

import com.google.protobuf.compiler.PluginProtos;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Interpreter {
    private final static int NUMBER_OF_BLOCKS = 51;
    private Block[] Blocks = new Block[NUMBER_OF_BLOCKS];

    public Interpreter() throws FileNotFoundException {
        loadBlocks();
    }

    public void runCode() {
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
                   Blocks[index] = newBlock;
               }
               index++;
           }
       } catch (FileNotFoundException e) {

       }


    }


}
