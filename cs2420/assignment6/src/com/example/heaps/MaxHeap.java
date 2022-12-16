package com.example.heaps;

/**
 * Max Heap to store values less than the median
 * Implemented as a leftist heap
 */
public class MaxHeap<T extends Comparable<T>> extends Heap<T> {

    public MaxHeap(T rootValue) {
        super(rootValue);
    }

    public MaxHeap() {}

    /**
     * Deletes the largest thing in the heap (the root)
     * @return the largest element
     */
    public T deleteMax() {
        HeapNode<T> max = root;
        if (max == null) return null;

        root = merge(max.left, max.right);
        size--;
        return max.value;
    }

    /**
     * Recursive merge algorithm for a leftist heap
     * It merges along it's right subtree, if the right
     * tree is bigger, it swaps it's children
     * @param node1 - first node to merge
     * @param node2 - second node to merge
     * @return new root node of the subtree
     */
    @Override
    protected HeapNode<T> merge(HeapNode<T> node1, HeapNode<T> node2) {
        // Base cases
        if (node1 == null) { return node2;}
        if (node2 == null) { return node1; }

        // Choose the larger node
        if (node1.compareTo(node2) < 0) {
            HeapNode<T> temp = node1;
            node1 = node2;
            node2 = temp;
        }

        node1.right = merge(node1.right, node2);

        node1.updateNpl();
        if (!node1.isLeftHeavy())
            node1.swapChildren();

        return node1;
    }

    public static void main(String[] args) {
        MaxHeap<Integer> heap = new MaxHeap<Integer>();
        heap.insert(1);
        heap.insert(10);
        heap.insert(50);
        heap.insert(20);
        heap.insert(150);
        heap.insert(120);
        heap.insert(100);
        System.out.println(heap.deleteMax());
        System.out.println(heap.deleteMax());
        System.out.println(heap.deleteMax());
        System.out.println(heap.deleteMax());
    }
}
