/** Assignment 6
 * @author Sean Collings
 * All in all, this implementation is ok, if I was to change something I would have some other way to track
 * if the page is contained in the frames in each of the implementation. Each uses an ArrayList, so lookup
 * will be O(n). But it didn't impact performance substantially so I didn't worry about it.
*/

import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class Assign6 {
    final static int SIMULATION_COUNT = 1000;
    final static int MAX_PAGE_REFERENCE = 250;
    final static int MAX_FRAMES = 100;

    /** Generates a sequence of length 1000 bounded between 1 and MAX_PAGE_REFERENCE*/
    public static int[] generateSequence() {
        int[] sequence = new int[1000];
        for (int i = 0; i < sequence.length; i++) {
            int randomValue = (int)Math.floor(Math.random() * (MAX_PAGE_REFERENCE) + 1);
            sequence[i] = randomValue;
        }

        return sequence;
    }

    public static void testLRU() {
        int[] sequence1 = {5, 1, 4, 3, 0, 2, 1, 3, 0, 5, 4, 0, 1, 0, 5, 2, 1, 2, 3, 1, 0, 5, 3};
        int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
        int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3

        // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
        // Page Faults should be 9
        (new TaskLRU(sequence1, 3, 5, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[3qq]);

        // // Replacement should be: 2, 1, 3, 1, 2
        // // Page Faults should be 7
        // (new TaskLRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
        // System.out.printf("Page Faults: %d\n", pageFaults[2]);

        // // Replacement should be: 1
        // // Page Faults should be 4
        // (new TaskLRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
        // System.out.printf("Page Faults: %d\n", pageFaults[3]);
    }

    /** Displays the results for the given simulation's page faults
     * @param fifoPageFaults
     * @param lruPageFaults
     * @param mruPageFaults
    */
    public static void displayResults(int[][] fifoPageFaults, int[][] lruPageFaults, int[][] mruPageFaults) {
        int minFifoFaults = 0;
        int minLruFaults  = 0;
        int minMruFaults  = 0;

        for (int i = 0; i < fifoPageFaults.length; i++) {
            for (int j = 0; j < fifoPageFaults[i].length; j++) {
                int fifo = fifoPageFaults[i][j];
                int lru = lruPageFaults[i][j];
                int mru = mruPageFaults[i][j];
                if (fifo <= lru && fifo <= mru) {
                    minFifoFaults++;
                }
                if (lru <= fifo && lru <= mru) {
                    minLruFaults++;
                }
                if (mru <= fifo && mru <= lru) {
                    minMruFaults++;
                }
            }
        }

        System.out.println("FIFO min PF  : " + minFifoFaults);
        System.out.println("LRU min PF   : " + minLruFaults);
        System.out.println("MRU min PF   : " + minMruFaults);

        System.out.println("Belandy's Anomoly Report for FIFO:");
        displayAnomolies(fifoPageFaults);
        System.out.println();

        System.out.println("Belandy's Anomoly Report for LRU:");
        displayAnomolies(lruPageFaults);
        System.out.println();

        System.out.println("Belandy's Anomoly Report for MRU:");
        displayAnomolies(mruPageFaults);
        System.out.println();
    }

    /**  Finds the anomolies and displays them
    @param pageFaults
    */
    public static void displayAnomolies(int[][] pageFaults) {
        ArrayList<BeladyAnomoly> anomolies = BeladyAnomoly.findAnomolies(pageFaults);
        int maxDelta = 0;
        for (BeladyAnomoly anomoly : anomolies) {
            if (anomoly.delta > maxDelta) {
                maxDelta = anomoly.delta;
            }
            System.out.println("\t" + anomoly.toString());
        }
        System.out.println("\t Anomaly detected " + anomolies.size() + " times with a max difference of " + maxDelta);
    }


    public static void main(String[] args) throws InterruptedException {
        testLRU();
    //     ExecutorService service = Executors.newFixedThreadPool(
    //         Runtime.getRuntime().availableProcessors()
    //     );

    //     long startTime = System.currentTimeMillis();

    //     int[][] fifoPageFaults = new int[SIMULATION_COUNT][];
    //     int[][] lruPageFaults  = new int[SIMULATION_COUNT][];
    //     int[][] mruPageFaults  = new int[SIMULATION_COUNT][];

    //     for (int j = 0; j < SIMULATION_COUNT ; j++) {
    //         int[] sequence = generateSequence();
    //         fifoPageFaults[j] = new int[MAX_FRAMES + 1];
    //         lruPageFaults[j]  = new int[MAX_FRAMES + 1];
    //         mruPageFaults[j]  = new int[MAX_FRAMES + 1];


    //         for (int i = 1; i <= MAX_FRAMES; i++) {
    //             service.execute(new TaskFIFO(sequence, i, MAX_PAGE_REFERENCE, fifoPageFaults[j]));
    //             service.execute(new TaskLRU(sequence, i, MAX_PAGE_REFERENCE, lruPageFaults[j]));
    //             service.execute(new TaskMRU(sequence, i, MAX_PAGE_REFERENCE, mruPageFaults[j]));
    //         }
    //     }

    //     service.shutdown();
    //     service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    //     long endTime = System.currentTimeMillis();

    //     System.out.println("Execution took: " + (endTime - startTime) + " ms");
    //     displayResults(fifoPageFaults, lruPageFaults, mruPageFaults);
    }
}

/** Stores an instance of an Anomoly for later use*/
class BeladyAnomoly {
    int previous;
    int current;
    int delta;

    public BeladyAnomoly(int current, int previous) {
        this.current = current;
        this.previous = previous;
        this.delta = current - previous;
    }

    public String toString() {
        return "detected - Previous " + previous + " : current " + current + " (" + delta + ")";
    }

    /** Finds the anomiles from the given page faults
     * @param pageFaults 2d array of page faults
    */
    public static ArrayList<BeladyAnomoly> findAnomolies(int[][] pageFaults) {
        ArrayList<BeladyAnomoly> anomolies = new ArrayList<BeladyAnomoly>();

        for (int i = 0; i < pageFaults.length; i++) {
            // Start j at 2 because the pageFaults[i][0] will be a dummy 0
            // Because we start at one frame
            for (int j = 2; j < pageFaults[i].length; j++) {
                int current = pageFaults[i][j];
                int previous = pageFaults[i][j - 1];
                if (current > previous) {
                    anomolies.add(new BeladyAnomoly(current, previous));
                }
            }
        }
        return anomolies;
    }
}