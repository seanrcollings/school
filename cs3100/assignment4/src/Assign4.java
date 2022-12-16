/**
* Calcualtes the value of Pi to RANGE (1000)
* in parallel using the Bailey-Borwein-Plouffe formula
* @author Sean Collings
*/

public class Assign4 {
    static final int RANGE = 1000;

    public static void main(String[] args) {
        long duration = System.currentTimeMillis();

        ResultTable<Integer, Integer> table = new ResultTable<>();
        TaskQueue<Integer> queue = new TaskQueue<>();

        for (int i = 1; i <= RANGE; i++) {
            queue.add(i);
        }

        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Executing on: " + Integer.toString(numberOfProcessors) + " threads");

        ThreadPool pool = new ThreadPool(numberOfProcessors, queue, table);
        pool.start();
        pool.join();

        System.out.println("\nComputed Value of pi: ");
        System.out.print("3.");
        for (int i = 1; i <= RANGE; i++) {
            System.out.print(table.get(i));
        }

		duration = System.currentTimeMillis() - duration;
        System.out.println("\n> " + duration + " ms");
    }
}
