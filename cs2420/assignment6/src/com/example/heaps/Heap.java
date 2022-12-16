package com.example.heaps;

public abstract class Heap<T extends Comparable<T>> {
    protected HeapNode<T> root;
    protected int size = 0;

    public Heap() {}
    public Heap(T rootValue) {
        this.root = new HeapNode<T>(rootValue);
        this.size = 1;
    }

    // Public methods
    public void insert(T value) {
        HeapNode<T> newNode = new HeapNode<T>(value);
        root = merge(root, newNode);
        size++;
    }

    public String toString() {
        return toString(root, 0);
    }

    public int getSize() { return size; }

    // Protected methods
    abstract protected HeapNode<T> merge(HeapNode<T> node1, HeapNode<T> node2);

    // Private methods

    private String toString(HeapNode<T> t, int level) {
        StringBuilder sb = new StringBuilder();

        if (t != null) {
            ++level;
            sb.append(toString(t.right, level));

            sb.append("\t".repeat(level)).append(t.value).append("\n");
            sb.append(toString(t.left, level));
        }
        return sb.toString();
    }

    protected static class HeapNode<E extends Comparable<E>> implements Comparable<HeapNode<E>> {
        E value;
        int npl; // used for leftist heap only
        HeapNode<E> left;
        HeapNode<E> right;

        public HeapNode(E value) {
            this.value = value;
        }

        public void swapChildren() {
            HeapNode<E> temp = left;
            left = right;
            right = temp;
        }

        public void updateNpl() {
            if (left == null || right == null) {
                npl = 0;
            } else if (left.npl > right.npl) {
                npl = right.npl + 1;
            } else {
                npl = left.npl + 1;
            }
        }

        public boolean isLeftHeavy() {
            if (left == null && right != null) return false;
            if (right == null) return true;
            return left.npl >= right.npl;
        }

        @Override
        public int compareTo(HeapNode<E> o) {
            return value.compareTo(o.value);
        }
    }
}
