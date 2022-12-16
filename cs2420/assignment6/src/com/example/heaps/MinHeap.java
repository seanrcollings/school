package com.example.heaps;

/**
 * Min heap for things larger than the median
 * implemented as a skew heap
 */
public class MinHeap<T extends Comparable<T>> extends Heap<T> {

    public MinHeap(T rootValue) {
        super(rootValue);
    }

    public MinHeap() {}

    /**
     * Deletes the smallest thing in the heap (the root)
     * @return the smallest element
     */
    public T deleteMin() {
        HeapNode<T> min = root;
        if (min == null) return null;

        root = merge(min.left, min.right);
        size--;
        return min.value;
    }

    /**
     * Skew heap merge implementation
     * @param node1 the first tree to be merged
     * @param node2 the second tree to be merged
     * @return the new root node
     */
    @Override
    protected HeapNode<T> merge(HeapNode<T> node1, HeapNode<T> node2) {
        // Base cases
        if (node1 == null) { return node2;}
        if (node2 == null) { return node1; }

        if (node1.compareTo(node2) > 0) {
            HeapNode<T> temp = node1;
            node1 = node2;
            node2 = temp;
        }

        node1.right = merge(node1.right, node2);
        node1.swapChildren();
        return node1;
    }

    public static void main(String[] args) {
        MinHeap<Integer> heap = new MinHeap<Integer>();
        heap.insert(10);
        heap.insert(50);
        heap.insert(20);
        heap.insert(1);
        heap.insert(150);
        heap.insert(120);
        heap.insert(100);
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
    }
}
