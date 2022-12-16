import java.util.Hashtable;

/** Thread Pool implementation.
 * The task that it performs is static
*/
public class ThreadPool {
    private int threadCount;
    private Hashtable<Integer, Thread> threads;
    private TaskQueue<Integer> queue;
    private ResultTable<Integer, Integer> results;

    public ThreadPool(int threadCount, TaskQueue<Integer> queue, ResultTable<Integer, Integer> results) {
        this.threadCount = threadCount;
        this.queue = queue;
        this.results = results;
        this.threads = new Hashtable<Integer, Thread>();
    }

    /** Intializes and starts `threadCount` number of threads*/
    public void start() {
        for (int i = 0; i < threadCount; i++) {
            Thread thread = createThread(i);
            threads.put(i, thread);
            thread.start();
        }
    }

    /** Blocks until all the threads in the thread
     * pool hav exited
    */
    public void join() {
        // Because Hashtable is synchronized,
        // the thread running join might never
        // give up the lock for long enough to allow the
        // worker threads to access and delete their entry
        // which means that the main thread will hang forever.
        // So each iteration, we pause this thread for 1
        // millisecond. This small amount of time gives the
        // other threads a chance to access and delete their entry
        while (threads.size() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Creates a new thread with the purpose of calculating a digit of PI
     * In an actual implementation of a ThreadPool, you'd want the runnable to be passed in
     * at instatiation, but for this assignment, assigning it statically is fine.s
     * @param id value places into the threads Hashtable to keep track of threads
     * @return the newly created Thread
     * */
    private Thread createThread(int id) {
        Bpp bpp = new Bpp();
        return new Thread(() -> {
            while (!queue.isEmpty()) {
                int digit = queue.dequeue();
                long piDigit = bpp.getDecimal(digit);
                // Since the results will always be 9 long, we can extract the uppermost
                // value by modding and dividing by 10000000
                int extractedDigit = (int)((piDigit - piDigit % 10000000) / 100000000);
                results.put(digit, extractedDigit);
                System.out.print(".");
                System.out.flush();
            }
            threads.remove(id);
        });
    }
}
