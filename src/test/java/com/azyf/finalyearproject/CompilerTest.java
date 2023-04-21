package com.azyf.finalyearproject;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    @Test
    void compileValidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        String text = "START MOVE SPRITE RIGHT NUMBER END";

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

        Interpreter interpreter = new Interpreter();


        
        Queue<Block> blocks =  interpreter.textToBlocks(text);

        for(int i =0; i < testBlocks.size(); i++) {
            assertEquals(testBlocks.remove(), blocks.remove());
        }

        assertEquals(testBlocks.size(), blocks.size());



    }





    @Test
    void compileAnotherValidProgram() throws FileNotFoundException, UnsupportedEncodingException {
        String text = "START TELPORT TO SPRITE RANDOM END";

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

        Interpreter interpreter = new Interpreter();



        Queue<Block> blocks =  interpreter.textToBlocks(text);

        for(int i =0; i < testBlocks.size(); i++) {
            assertEquals(testBlocks.remove(), blocks.remove());
        }

        assertEquals(testBlocks.size(), blocks.size());
    }

    @Test
    void compileInvalidWordsProgram() throws FileNotFoundException {
        String text = "The quick brown fox jumps over the lazy dog";



        Interpreter interpreter = new Interpreter();


        Queue<Block> blocks = interpreter.textToBlocks(text);
        for (int i = 0; i <= blocks.size(); i++) {
            assertNull(blocks.remove());
        }


    }

    @Test
    void compileAnotherInvalidWordsProgram() throws FileNotFoundException {
        String text = "THE CAT LOVES ITSELF";
        Interpreter interpreter = new Interpreter();


        Queue<Block> blocks = interpreter.textToBlocks(text);
        for (int i = 0; i <= blocks.size(); i++) {
            assertNull(blocks.remove());
        }

    }

    @Test
    void compileMixtureOfValidAndInvalidWords() throws FileNotFoundException {
        String text = "START TEST HELLO END";
        Block start = new Block("START", Category.Commands);
        Block end = new Block("END", Category.Commands);
        Interpreter interpreter = new Interpreter();


        Queue<Block> blocks = interpreter.textToBlocks(text);
        for (int i = 0; i <= blocks.size(); i++) {
            if(i == 0) {
                assertEquals(start, blocks.remove());
            } else if (i == 3) {
                assertEquals(end, blocks.remove());
            } else {
                assertNull(blocks.remove());
            }

        }

    }

}
