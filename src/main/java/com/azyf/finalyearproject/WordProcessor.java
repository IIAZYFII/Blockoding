package com.azyf.finalyearproject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class WordProcessor {
    PriorityQueue<Word> wordPriorityQueue;
    ArrayList<String> dictionary;
    int ocrError = 0;
    int[][] matrix;

    public WordProcessor() {
        dictionary = new ArrayList<>();
        wordPriorityQueue = new PriorityQueue<>();
    }


    public void processWord(String extractedText) {
        System.out.println("--------------------------");
       extractedText = extractedText.replaceAll("[^A-Z\n \s]","");
        System.out.println(extractedText);
        Scanner in = null;
        try {
             in = new Scanner(extractedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("--------------------------");
        while (in.hasNextLine()) {
            String newText = in.nextLine();
           String removeSpace = newText.replaceAll("[\s]", "");
            if(removeSpace.length() >= 2) {
                stringBuilder.append(removeSpace);
                stringBuilder.append("\n");
            }
        }
        in.close();
        System.out.println(stringBuilder.toString());
        extractedText = stringBuilder.toString();

        in = new Scanner(extractedText);
        while(in.hasNext()) {
            String firstWord = in.next();
            for(int i = 0; i < dictionary.size(); i++) {
                String secondWord = dictionary.get(i);
                int distance = editDistance(firstWord, secondWord);
                Word newWord = new Word(secondWord, distance);
                wordPriorityQueue.add(newWord);
            }
            System.out.println("-----------------------------------");
            System.out.println(firstWord);
            stringBuilder = new StringBuilder();
            for(int i = 0; i <= wordPriorityQueue.size(); i++) {
                Word newWord = wordPriorityQueue.remove();
                System.out.println(newWord.toString());
                if(newWord.getWord().equals(firstWord)) {
                    stringBuilder.append(firstWord);
                    stringBuilder.append("\n");
                    //Put word on stack
                    wordPriorityQueue.clear();
                } else if(newWord.getWord().length() >= firstWord.length()) {
                    wordPriorityQueue.clear();
                    ocrError = ocrError + 1;
                    //predict word on stack
                    stringBuilder.append("Error");
                    stringBuilder.append("\n");
                } else {
                    stringBuilder.append(newWord.getWord());
                    stringBuilder.append("\n");
                   // put first word on stack
                    //maybe do comparison thing
                    wordPriorityQueue.clear();
                }



            }
        }
        in.close();
        System.out.println("-------------------------------------");
        System.out.println(stringBuilder.toString());


    }

    public void addWordsToDictionary(File wordList) {
        Scanner in = null;
        try {
            in = new Scanner(wordList);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot find file " + wordList.getName());
            System.exit(0);
        }

        while(in.hasNextLine()) {
            String word = in.nextLine();
            dictionary.add(word);
        }
        in.close();

    }

    private int editDistance(String firstWord, String secondWord) {
        int[][] matrix = new int[firstWord.length() + 1][secondWord.length() + 1];
        for (int i = 0; i < firstWord.length() + 1;  i++) {
            matrix[i][0] = i;
        }
        for (int j = 1; j < secondWord.length() + 1; j++) {
            matrix[0][j] = j;
        }


        for(int j = 1; j <  secondWord.length()+1; j++) {
            for (int i = 1; i < firstWord.length()+1; i++) {
                String characterOne = String.valueOf(firstWord.charAt(i-1));
                String characterTwo = String.valueOf(secondWord.charAt(j-1));
                if (characterOne.equals(characterTwo)) {
                    matrix[i][j] = matrix[i-1][j-1];
                } else {
                    int topLeft = matrix[i-1][j-1];
                    int bottomLeft = matrix[i-1][j];
                    int topRight = matrix[i][j-1];
                    if(topLeft < bottomLeft && topLeft < topRight) {
                        matrix[i][j] = topLeft + 1;
                    } else if(topRight < bottomLeft && topRight < topLeft) {
                        matrix[i][j] = topRight + 1;
                    } else {
                        matrix[i][j] = bottomLeft + 1;
                    }
                }
            }
        }
        return matrix[firstWord.length()][secondWord.length()];

    }

}
