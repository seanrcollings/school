package src;


import java.util.Iterator;

public class LinkedList<E> implements Iterable<E> {
    private ListNode<E> head;
    public int length = 0;

    public void addFirst(E data) {
        ListNode<E> node = new ListNode<>(data);

        if (head != null) node.next = head;
        this.head = node;
        this.length++;
    }


    public void traverse() {
        ListNode<E> current = this.head;

        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private ListNode<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                ListNode<E> old = this.current;
                this.current = old.next;
                return old.data;
            }
        };
    }

    private static class ListNode<T> {
        public T data;
        public ListNode<T> next;
        public ListNode() {}
        public ListNode(T data) { this.data = data; }
    }
}