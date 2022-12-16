package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class Util {

    /**
     * Try / Catch handler around getting a Scanner
     * @param filename name of file to read
     * @return a Scanner of the provided file
     */
    public static Scanner getScanner(String filename) {
        try {
            return  new Scanner( new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
