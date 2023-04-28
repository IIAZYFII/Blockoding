package com.azyf.finalyearproject;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IntegrationTesting {

  @Test
 void runValidProgramIntegration() throws IOException {
      TextExtractor textExtractor = new TextExtractor();
 File image = new File("C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\src\\test\\images\\ValidProgram.jpg");
      String text = textExtractor.extractText(image);
       Image sprite = null;

       SpriteController spriteController = new SpriteController();
       spriteController.addSprite("test", 50.0,50.0, sprite);
       HashMap<String, String> inputBoxValues = new HashMap<>();
       inputBoxValues.put("spritename", "test");
       ArrayList<String> inputBoxes = new ArrayList<>();
       inputBoxes.add("spritename");

       Interpreter interpreter = new Interpreter();
      System.out.println(text);
      Queue<Block> blocks =  interpreter.textToBlocks(text);
      WindowBuilder windowBuilder = new WindowBuilder();
      File file = new File("C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\Assets\\Blocks\\parseTree.txt");
      interpreter.loadTree(file);
      interpreter.checkSyntax(blocks, windowBuilder);
      interpreter.loadBlocks(blocks);



       SoundController soundController = new SoundController();
       VariableManager variableManager = new VariableManager();


      interpreter.compileAndRun(spriteController, 50.0, 50.0, inputBoxValues, inputBoxes,
              variableManager,soundController , null);

      assertNotEquals(50.0, spriteController.getSprite(0).getXPos());
      assertNotEquals(50.0, spriteController.getSprite(0).getYPos());

   }




    @Test
    void runInvalidProgramIntegration() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File image = new File("C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\src\\test\\images\\invalidProgram.png");
        String text = textExtractor.extractText(image);
        Image sprite = null;

        SpriteController spriteController = new SpriteController();
        spriteController.addSprite("test", 50.0,50.0, sprite);
        HashMap<String, String> inputBoxValues = new HashMap<>();
        inputBoxValues.put("spritename", "test");
        ArrayList<String> inputBoxes = new ArrayList<>();
        inputBoxes.add("spritename");

        Interpreter interpreter = new Interpreter();
        System.out.println(text);
        Queue<Block> blocks =  interpreter.textToBlocks(text);
        WindowBuilder windowBuilder = new WindowBuilder();
        File file = new File("C:\\Users\\hussa\\Documents\\Projects\\FinalYearProject\\Assets\\Blocks\\parseTree.txt");
        interpreter.loadTree(file);
        interpreter.checkSyntax(blocks, windowBuilder);
        interpreter.loadBlocks(blocks);



        SoundController soundController = new SoundController();
        VariableManager variableManager = new VariableManager();


        interpreter.compileAndRun(spriteController, 50.0, 50.0, inputBoxValues, inputBoxes,
                variableManager,soundController , null);

        assertEquals(50.0, spriteController.getSprite(0).getXPos());
        assertEquals(50.0, spriteController.getSprite(0).getYPos());

    }

}
