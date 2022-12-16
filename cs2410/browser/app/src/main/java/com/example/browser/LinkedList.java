package com.example.browser;

// Generic Doubly Linked List to be extended by History

public class LinkedList<E> {
    protected ListNode<E> head;
    protected ListNode<E> tail;

    public LinkedList(E data) {
        insertStart(data);
    }

    public void insertStart(E data) {
        ListNode<E> newNode = new ListNode<>(data);
        if (head != null) head.prev = newNode;
        newNode.next = head;
        head = newNode;
        if (tail == null) tail = newNode;
    }

    public void insertEnd(E data) {
        ListNode<E> newNode = new ListNode<>(data);
        if (tail != null) tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        if (head == null) head = newNode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode<E> curr = head;
        while (curr != null)  {
            sb.append(curr.getData()).append(" -> ");
            curr = curr.next;
        }
        return sb.toString();
    }

    protected class ListNode<T> {
        private T data;
        protected ListNode prev;
        protected ListNode next;

        public ListNode(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }
}
