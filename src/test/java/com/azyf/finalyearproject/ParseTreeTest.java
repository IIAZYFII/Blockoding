package com.azyf.finalyearproject;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ParseTreeTest {

    @Test
    void checkValidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));
        Queue<Block> testBlocks = new LinkedList<>();


        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block right = new Block("RIGHT", Category.Actions);
        testBlocks.add(right);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);

        WindowBuilder windowBuilder = new WindowBuilder();

        assertTrue(interpreter.checkSyntax(testBlocks, windowBuilder));

    }


    @Test
    void checkAnotherValidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));
        Queue<Block> testBlocks = new LinkedList<>();

        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block telport = new Block("TELPORT", Category.Actions);
        testBlocks.add(telport);
        Block to = new Block("TO", Category.Actions);
        testBlocks.add(to);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block random  = new Block("RANDOM", Category.Actions);
        testBlocks.add(random);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);

        WindowBuilder windowBuilder = new WindowBuilder();

        assertTrue(interpreter.checkSyntax(testBlocks, windowBuilder));

    }


    @Test
    void checkInvalidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));
        Queue<Block> testBlocks = new LinkedList<>();

        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block telport = new Block("TELPORT", Category.Actions);
        testBlocks.add(telport);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);

        WindowBuilder windowBuilder = new WindowBuilder();

        assertFalse(interpreter.checkSyntax(testBlocks, windowBuilder));

    }


    @Test
    void checkAnotherInValidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));
        Queue<Block> testBlocks = new LinkedList<>();


        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);

        WindowBuilder windowBuilder = new WindowBuilder();

        assertFalse(interpreter.checkSyntax(testBlocks, windowBuilder));

    }



}