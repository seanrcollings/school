import java.util.Arrays;

/**
 * Assignment 4 for CS 1410
 * This program evaluates the linear and binary searching, along
 * with comparing performance difference between the selection sort
 * and the built-in java.util.Arrays.sort.
 *
 */
public class EvaluationDriver {
    static final int MAX_VALUE = 1_000_000;
    static final int MAX_ARRAY_SIZE = 100_000;
    static final int ARRAY_INCREMENT = 20_000;
    static final int NUMBER_SEARCHES = 50_000;

    public static void main(String[] args) {
        demoLinearSearchUnsorted(MAX_VALUE, MAX_ARRAY_SIZE, ARRAY_INCREMENT, NUMBER_SEARCHES);
        demoLinearSearchSorted(MAX_VALUE, MAX_ARRAY_SIZE, ARRAY_INCREMENT, NUMBER_SEARCHES);
        demoBinarySearchSelectionSort(MAX_VALUE, MAX_ARRAY_SIZE, ARRAY_INCREMENT, NUMBER_SEARCHES);
        demoBinarySearchFastSort(MAX_VALUE, MAX_ARRAY_SIZE, ARRAY_INCREMENT, NUMBER_SEARCHES);
    }

    public static void demoLinearSearchUnsorted(final int MAX_VALUE, final int MAX_ARRAY_SIZE, final int ARRAY_INCREMENT, final int NUMBER_SEARCHES) {
        System.out.println("--- Linear Search Timing (unsorted) ---");
        for (int i = ARRAY_INCREMENT; i <= MAX_ARRAY_SIZE; i += ARRAY_INCREMENT) {
            int numberFound = 0;
            long startTime = System.currentTimeMillis();

            int[] data = generateNumbers(i, MAX_VALUE);
            for (int j = 0; j < NUMBER_SEARCHES; j++) {
                int search = (int)(Math.random() * MAX_VALUE);
                numberFound += linearSearch(data, search) ? 1 : 0;
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            reportData(i, numberFound, elapsedTime);
        }
    }

    public static void demoLinearSearchSorted(final int MAX_VALUE, final int MAX_ARRAY_SIZE, final int ARRAY_INCREMENT, final int NUMBER_SEARCHES) {
        System.out.println("--- Linear Search Timing (Selection Sort) ---");
        for (int i = ARRAY_INCREMENT; i <= MAX_ARRAY_SIZE; i += ARRAY_INCREMENT) {
            int numberFound = 0;
            long startTime = System.currentTimeMillis();

            int[] data = generateNumbers(i, MAX_VALUE);
            selectionSort(data);
            for (int j = 0; j < NUMBER_SEARCHES; j++) {
                int search = (int)(Math.random() * MAX_VALUE);
                numberFound += linearSearch(data, search) ? 1 : 0;
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            reportData(i, numberFound, elapsedTime);
        }
    }

    public static void demoBinarySearchSelectionSort(final int MAX_VALUE, final int MAX_ARRAY_SIZE, final int ARRAY_INCREMENT, final int NUMBER_SEARCHES) {
        System.out.println("--- Binary Search Timing (Selection Sort) ---");
        for (int i = ARRAY_INCREMENT; i <= MAX_ARRAY_SIZE; i += ARRAY_INCREMENT) {
            int numberFound = 0;
            long startTime = System.currentTimeMillis();

            int[] data = generateNumbers(i, MAX_VALUE);
            selectionSort(data);
            for (int j = 0; j < NUMBER_SEARCHES; j++) {
                int search = (int)(Math.random() * MAX_VALUE);
                numberFound += binarySearch(data, search) ? 1 : 0;
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            reportData(i, numberFound, elapsedTime);
        }
    }

    public static void demoBinarySearchFastSort(final int MAX_VALUE, final int MAX_ARRAY_SIZE, final int ARRAY_INCREMENT, final int NUMBER_SEARCHES) {
        System.out.println("--- Binary Search Timing (Fast Sort) ---");
        for (int i = ARRAY_INCREMENT; i <= MAX_ARRAY_SIZE; i += ARRAY_INCREMENT) {
            int numberFound = 0;
            long startTime = System.currentTimeMillis();

            int[] data = generateNumbers(i, MAX_VALUE);
            Arrays.sort(data);
            for (int j = 0; j < NUMBER_SEARCHES; j++) {
                int search = (int)(Math.random() * MAX_VALUE);
                numberFound += binarySearch(data, search) ? 1 : 0;
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            reportData(i, numberFound, elapsedTime);
        }
    }


    public static void reportData(int items, int numberFound, long elapsedTime) {
        System.out.printf("Number of Items       : %d\n", items);
        System.out.printf("Times value was found : %d\n", numberFound);
        System.out.printf("Total search time     : %d ms\n\n", elapsedTime);
    }

    public static int[] generateNumbers(int howMany, int maxValue) {
        if (howMany > 0) {
            int[] data = new int[howMany];
            for (int i = 0; i < data.length; i++) {
                data[i] = (int)(Math.random() * maxValue);
            }
            return data;
        } else {
            return null;
        }
    }

    public static boolean linearSearch(int[] data, int search) {
        for (int value : data) {
            if (value == search) {
                return true;
            }
        }
        return false;
    }

    public static boolean binarySearch(int[] data, int search) {
        int bottom = 0;
        int top = data.length - 1;

        while (top >= bottom) {
            int middle = bottom + (top - bottom) / 2;
            if (search == data[middle]) {
                return true;
            } else if (search > data[middle]) {
                bottom = middle + 1;
            } else if (search < data[middle]) {
                top = middle - 1;
            }
        }
        return false;
    }

    public static void selectionSort(int[] data) {
        int n = data.length;

        for (int i = 0; i < n-1; i++){
            int minIndex = i;
            for (int j = i+1; j < n; j++)
                if (data[j] < data[minIndex])
                    minIndex = j;

            int temp = data[minIndex];
            data[minIndex] = data[i];
            data[i] = temp;
        }
    }
}
