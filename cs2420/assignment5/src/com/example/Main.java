package com.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static HashTable<String> dictionary = new HashTable<>();
    static ArrayList<String> listDict = new ArrayList<>();

    static void handleWord(String word, HashTable<WordFreq> misspelledWords) {
        word = word.strip();
        if (word.equals(" ") || word.equals("")) return;

        if (!dictionary.contains(word)) {
            WordFreq newWord = new WordFreq(word);
            WordFreq contained = misspelledWords.find(newWord);

            if (contained != null) {
                contained.increase();
            } else {
                misspelledWords.insert(newWord);
                handleMisspelledWord(word, listDict);
            }
        }
    }

    static void handleMisspelledWord(String word, ArrayList<String> acceptedWords) {
        System.out.println("Found misspelled :" + word + ":");
        StringBuilder sb = new StringBuilder();
        final int MAX_LENGTH = 20;
        int currentLength = 0;
        for (String possibleWord: acceptedWords) {
            int distance = Util.minDistance(word, possibleWord);
            if (distance <= 2) {
                sb.append(possibleWord).append("(").append(distance).append(") ");
                if (currentLength >= MAX_LENGTH) {
                    break;
                } else { currentLength++; }
            }
        }
        sb.append("\n");
        System.out.println(sb.toString());
    }

    static void initializeDicts(HashTable<String> avl, ArrayList<String> list) {
        Scanner reader = Util.getReader("data/dictionary.txt");
        while (reader.hasNext()){
            String line = reader.nextLine();
            avl.insert(line);
            list.add(line);
        }
        reader.close();
    }

    public static void main(String[] args) {
        String[] files = {"p.txt", "paragraph1.txt", "paragraph2.txt", "paragraph3.txt"};
        initializeDicts(dictionary, listDict);

        for (String file: files) {
            System.out.println("Checking: " + file);
            Scanner reader = Util.getReader("data/" + file);
            HashTable<WordFreq> misspelledWords = new HashTable<>();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.toLowerCase().replaceAll("\\p{Punct}", "").strip();

                String[] words = line.split(" ");
                for (String word: words) {
                    handleWord(word, misspelledWords);
                }
            }

            System.out.println(misspelledWords.toString(100));
            System.out.println("------------------------------------------------------------------------");
            reader.close();
        }
    }
}


class WordFreq implements Comparable<WordFreq> {
    final private String word;
    private int count;

    public WordFreq(String word) {
        this(word, 1);
    }

    public WordFreq(String word, int count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public int compareTo(WordFreq other) {
        return word.compareTo(other.getWord());
    }

    public boolean equals(Object other) {
        return this.word.equals(((WordFreq)other).getWord());
    }

    public String toString() {
        return ":" + this.word + ":" + " " + count;
    }

    public String getWord() { return word; };
    public void increase() {this.count++;}

    public int hashCode() { return this.word.hashCode(); }
}