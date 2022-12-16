import java.util.LinkedList;

/** FIFO Task Queue implementation for theads to consume.
 * Uses the synchronized keyword to insure that acess to the queue is
 * threadsafe
 */
class TaskQueue<T> {
    private LinkedList<T> queue;

    public TaskQueue() {
        queue = new LinkedList<T>();
    }

    /** Adds an item to the queue
    * @param value to be added */
    public void add(T value) {
        queue.add(value);
    }

    /** Removes the first item in the queue
     * Method is synchonized, to prevent multiple threads
     * from dequeueing at the same time
     * @return the item removed*/
    public synchronized T dequeue() {
        return queue.removeFirst();
    }

     /** Asserts wether the queue is empty*/
    public synchronized boolean isEmpty() {
        return queue.peekFirst() == null;
    }
}