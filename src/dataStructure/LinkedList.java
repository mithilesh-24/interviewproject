package dataStructure;

import core.Question;

public class LinkedList {
    private static class Node {
        Question data;
        Node next;

        Node(Question data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public void insert(Question q) {
        Node newNode = new Node(q);
        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }


    public Question search(int id) {
        Node current = head;
        while (current != null) {
            if (current.data.id == id)
                return current.data;
            current = current.next;
        }
        return null;
    }


    public void delete(int id) {
        if (head == null) return;

        if (head.data.id == id) {
            head = head.next;
            return;
        }

        Node current = head;
        Node prev = null;

        while (current != null && current.data.id != id) {
            prev = current;
            current = current.next;
        }

        if (current == null) return;

        prev.next = current.next;
    }


    public void display() {
        if (head == null) {
            System.out.println("No questions available.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
