package com.azyf.finalyearproject;

public class Word implements  Comparable<Word>{
    String word;
    int editDistance;

    public Word(String word, int editDistance) {
        this.word = word;
        this.editDistance = editDistance;
    }


    @Override
    public int compareTo(Word otherWord) {
        if (this.editDistance > otherWord.editDistance) {
            return 1;
        } else if (this.editDistance < otherWord.editDistance) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return word + ": " + editDistance;
    }
}
