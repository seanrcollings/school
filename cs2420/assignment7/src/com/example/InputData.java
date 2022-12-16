package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for parsing and then storing the
 * data from each input file
 */
public class InputData {
    int numberOfVoters;
    ArrayList<int[]> attacks;
    ArrayList<Member> members = new ArrayList<>();

    public InputData(String filename) {
        Scanner scanner = getScanner(filename);
        numberOfVoters = Integer.parseInt(scanner.nextLine());
        attacks = new ArrayList<>();

        // Create the members for keeping track of previous attacks
        for (int i = 0; i < numberOfVoters; i++) {
            members.add(new Member(i));
        }

        // Create the 2D List / array of attacks
        while (scanner.hasNextLine()) {
            int[] attack = new int[2];
            String line = scanner.nextLine().strip().replaceAll("\\s+", " "); // Replace all is a quick hack for case6
            if (line.length() == 0) continue;
            String[] attackStrings = line.split("\\s");
            attack[0] = Integer.parseInt(attackStrings[0]);
            attack[1] = Integer.parseInt(attackStrings[1]);
            attacks.add(attack);
        }
    }

    private static Scanner getScanner(String filename) {
        try {
            return new Scanner( new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static class Member {
        int value;
        int previousOpponent = -1;

        public Member(int value) {
            this.value = value;
        }

        public String toString() {
            return Integer.toString(value);
        }
    }
}
