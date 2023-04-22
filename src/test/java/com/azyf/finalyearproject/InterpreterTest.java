package com.azyf.finalyearproject;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.common.annotations.VisibleForTesting;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {
    @Test
    void checkMoveSpriteRight() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
     WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

       SoundController soundController = new SoundController();
       VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
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


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(70.0, spriteController.getSprite(0).getXPos());

    }

    @Test
    void checkMoveSpriteNegativeRight() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "-20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
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


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(30.0, spriteController.getSprite(0).getXPos());

    }


    @Test
    void checkMoveSpriteLeft() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block left = new Block("LEFT", Category.Actions);
        testBlocks.add(left);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(30.0, spriteController.getSprite(0).getXPos());

    }

    @Test
    void checkMoveSpriteNegativeLeft() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "-20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block left = new Block("LEFT", Category.Actions);
        testBlocks.add(left);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(70.0, spriteController.getSprite(0).getXPos());

    }


    @Test
    void checkMoveSpriteUp() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block up = new Block("UP", Category.Actions);
        testBlocks.add(up);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(30.0, spriteController.getSprite(0).getYPos());

    }

    @Test
    void checkMoveSpriteNegativeUp() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "-20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block up = new Block("UP", Category.Actions);
        testBlocks.add(up);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(70.0, spriteController.getSprite(0).getYPos());

    }

    @Test
    void checkMoveSpriteDown() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block down = new Block("DOWN", Category.Actions);
        testBlocks.add(down);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(70.0, spriteController.getSprite(0).getYPos());

    }


    @Test
    void checkMoveSpriteNegativeDown() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("step", "-20");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("step");
        interpreter.loadTree(new File("Assets\\Blocks\\parseTree.txt"));

        Queue<Block> testBlocks = new LinkedList<>();
        Block start = new Block("START", Category.Commands);
        testBlocks.add(start);
        Block move = new Block("MOVE", Category.Actions);
        testBlocks.add(move);
        Block sprite = new Block("SPRITE", Category.Misc);
        testBlocks.add(sprite);
        Block down = new Block("DOWN", Category.Actions);
        testBlocks.add(down);
        Block number = new Block("NUMBER", Category.Maths);
        testBlocks.add(number);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(30.0, spriteController.getSprite(0).getYPos());

    }



    @Test
    void checkMoveSpriteTeleportToRandom() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
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
        Block random = new Block("RANDOM", Category.Actions);
        testBlocks.add(random);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertNotEquals(50.0, spriteController.getSprite(0).getXPos());
        assertNotEquals(50.0, spriteController.getSprite(0).getYPos());

    }


    @Test
    void checkMoveSpriteTeleportToSprite() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);
        spriteController.addSprite("test2", 100.0,100.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("secondSprite", "test2");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("secondSprite");
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
        Block sprite2 = new Block("SPRITE", Category.Actions);
        testBlocks.add(sprite2);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(100.0, spriteController.getSprite(0).getXPos());
        assertEquals(100.0, spriteController.getSprite(0).getYPos());

    }



    @Test
    void checkMoveSpriteTeleportToXY() throws FileNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, UnsupportedEncodingException {
        Interpreter interpreter = new Interpreter();
        WritableImage image = new WritableImage(50,50);

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, image);

        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();



        double mouseX = 700;
        double mouseY = 700;

        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        inputBoxValues.put("x", "255");
        inputBoxValues.put("y", "450");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");
        inputBoxes.add("x");
        inputBoxes.add("y");
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
        Block x = new Block("X", Category.Misc);
        testBlocks.add(x);
        Block y = new Block("Y", Category.Actions);
        testBlocks.add(y);
        Block end = new Block("END", Category.Commands);
        testBlocks.add(end);


        interpreter.loadBlocks(testBlocks);

        interpreter.compileAndRun(spriteController,mouseX, mouseY, inputBoxValues, inputBoxes, variableManager,
                soundController, null);

        assertEquals(255, spriteController.getSprite(0).getXPos());
        assertEquals(450, spriteController.getSprite(0).getYPos());

    }





}