package com.azyf.finalyearproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class WordProcessor {
    ArrayList<String> dictionary;
    public WordProcessor() {
        dictionary = new ArrayList<>();
    }


    public void processWord(String extractedText) {
        System.out.println("--------------------------");
       extractedText = extractedText.replaceAll("[^A-Z\n \s]","");
        System.out.println(extractedText);

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

}
