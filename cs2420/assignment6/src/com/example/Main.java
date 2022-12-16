package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    /** Wrapper around creating a scanner to handle the
     * error catching
     * @param fileName name of the file to return a handle to
     * @return a scanner object
     */
    static Scanner getReader(String fileName) {
        try {
            return  new Scanner( new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner salaries = getReader("data/Salaries.txt");
        PriorityQueue<Integer> queue = new PriorityQueue<>();

        int numberInserted = 0;
        salaries.nextLine(); // to get rid of the columns line
        DecimalFormat ft = new DecimalFormat("$###,###,###");

        while (salaries.hasNextLine()) {
            int salary = Integer.parseInt(salaries.nextLine().split("\t")[0]);
            queue.insert(salary);
            numberInserted++;

            if (numberInserted % 25 == 0) {
                System.out.println("After " + numberInserted + " salaries, median is: " + ft.format(queue.getCurrMedian()));
            }
        }
        System.out.println("After all salaries, median is: " + ft.format(queue.getCurrMedian()));
    }
}
