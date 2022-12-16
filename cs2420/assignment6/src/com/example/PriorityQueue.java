package com.example;

import com.example.heaps.*;

public class PriorityQueue<T extends Comparable<T>> {
    private T currMedian;
    private final MaxHeap<T> lessThanMedian; // Max heap for values less than currMedian - Leftist Heap
    private final MinHeap<T> greaterThanMedian; // Min heap for values greater than currMedian - Skew Heap

    public PriorityQueue() {
        lessThanMedian = new MaxHeap<>();
        greaterThanMedian = new MinHeap<>();
    }

    public T getCurrMedian(){ return currMedian; }

    /**
     * Inserts a new value into into the queue
     * @param value - the value to be inserted
     */
    public void insert(T value) {
        if (currMedian == null) {
            currMedian = value;
            return;
        }

        if (currMedian.compareTo(value) > 0) {
            lessThanMedian.insert(value);
        } else {
            greaterThanMedian.insert(value);
        }

        calculateMedian();
    }


    /**
     * Ran each time something is inserted into the queue
     * Calculates what the new median should be
     */
    private void calculateMedian() {
        int diff = lessThanMedian.getSize() - greaterThanMedian.getSize();
        if (Math.abs(diff) > 1) {
            if (diff < 0) {
                lessThanMedian.insert(currMedian);
                currMedian = greaterThanMedian.deleteMin();
            } else {
                greaterThanMedian.insert(currMedian);
                currMedian = lessThanMedian.deleteMax();
            }
        }
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        int[] values = {5, 7, 10, 15, 12, 8, 3, 1, 2, 42, 20};
        for (int value: values) {
            queue.insert(value);
            System.out.println(queue.getCurrMedian());
        }
    }
}