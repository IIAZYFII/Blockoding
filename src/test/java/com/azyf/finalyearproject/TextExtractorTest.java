package com.azyf.finalyearproject;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TextExtractorTest {
    @Test
    public void checkExtractsComputerTextCorrectly() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnScreenWords.png");
        assertEquals("The quick brown fox jumps over the lazy dog.", textExtractor.extractText(file));

    }


    @Test
    public void testShouldEqualAnotherTestUsingComputerText() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnScreenAnotherTest.png");
        assertEquals("Another Test", textExtractor.extractText(file));

    }

    @Test
    public void testShouldNotEqualTestAnotherUsingComputerText() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnScreenAnotherTest.png");
        assertNotEquals("Test Another", textExtractor.extractText(file));

    }

    @Test
    public void checkExtractsComputerTextIncorrectly() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnScreenWords.png");
        assertNotEquals("The quick brown fox does not jump over the lazy dog.", textExtractor.extractText(file));

    }


    @Test
    public void testShouldEqualOnPaperWords() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnPaperWords.png");
        assertEquals("Test One", textExtractor.extractText(file));

    }

    @Test
    public void testShouldNotEqualOnPaperWords() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnPaperWords.png");
        assertNotEquals("One Test", textExtractor.extractText(file));

    }

    @Test
    public void anotherTestShouldEqualOnPaperWords() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnPaperAnotherTest.png");
        assertEquals("Another Test", textExtractor.extractText(file));

    }

    @Test
    public void anotherTestShouldNotEqualOnPaperWords() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\OnPaperAnotherTest.png");
        assertNotEquals("Another Test Test", textExtractor.extractText(file));

    }

    @Test
    public void testShouldEqualOnBlock() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\TestOnBlock.png");
        assertEquals("WHENEVER", textExtractor.extractText(file));

    }



    @Test
    public void testShouldNotEqualOnBlock() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\TestOnBlock.png");
        assertNotEquals("Whenever", textExtractor.extractText(file));

    }

    @Test
    public void anotherTestShouldEqualOnBlock() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\AnotherTestOnBlock.jpg");
        assertEquals("HORIZONTAL", textExtractor.extractText(file));

    }


    @Test
    public void anotherTestShouldNotEqualOnBlock() throws IOException {
        TextExtractor textExtractor = new TextExtractor();
        File file = new File(FileController.getAbsolutePath() + "\\src\\test\\images\\AnotherTestOnBlock.jpg");
        assertNotEquals("horizontal", textExtractor.extractText(file));

    }




}