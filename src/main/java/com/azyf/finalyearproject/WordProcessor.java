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

        in.close();

        for (int i = 0; i < dictionary.size(); i++) {
            System.out.println(dictionary.get(i));
        }
    }

}
