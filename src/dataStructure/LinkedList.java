package dataStructure;

import core.Question;

import java.util.ArrayList;
import java.util.List;

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


    public String display() {
        if (head == null) {
            return "No questions available.";
        }

        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data.toString()).append("\n---------------------\n");
            current = current.next;
        }
        return sb.toString();
    }
    public List<Question> toList() {
        List<Question> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
    public boolean displayByUser(String username, StringBuilder sb) {
        if (head == null) {
            return false;
        }

        Node current = head;
        boolean found = false;

        while (current != null) {
            if (current.data.createdBy.equals(username)) {
                sb.append(current.data.toString()).append("\n----------------------\n");
                found = true;
            }
            current = current.next;
        }

        return found;
    }


}
