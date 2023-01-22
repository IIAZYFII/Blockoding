package com.azyf.finalyearproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class MainTest {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadTree(new File("C:\\Users\\hussa\\Dropbox\\Computer Science\\Year 3\\Final Year Project\\FinalYearProject\\Assets\\Blocks\\parseTree.txt"));
        System.out.println(interpreter.getParseTree().root);
        //System.out.println(interpreter.getParseTree().root.getChildren().get(3).getChildren().get(0));

    }
}
