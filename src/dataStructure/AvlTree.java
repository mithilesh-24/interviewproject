package dataStructure;

import core.Question;
import java.util.ArrayList;
import java.util.List;


public class AvlTree {
    private static class Node {
        Question data;
        Node left, right;
        int height;

        public Node(Question data) {
            this.data = data;
            this.height = 0;
        }
    }

    private Node root;

    private int height(Node n) {
        return n == null ? -1 : n.height;
    }

    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rightRotate(Node k2) {
        Node k1 = k2.left;
        Node y = k1.right;


        k1.right = k2;
        k2.left = y;


        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;


        return k1;
    }

    private Node leftRotate(Node k1) {
        Node k2 = k1.right;
        Node y = k2.left;


        k2.left = k1;
        k1.right = y;


        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;


        return k2;
    }


    public void insert(Question q) {
        root = insert(root, q);
    }

    private Node insert(Node node, Question q) {
        if (node == null) return new Node(q);

        if (q.id < node.data.id)
            node.left = insert(node.left, q);
        else if (q.id > node.data.id)
            node.right = insert(node.right, q);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        //LL
        if (balance > 1 && q.id < node.left.data.id)
            return rightRotate(node);

        //RR
        if (balance < -1 && q.id > node.right.data.id)
            return leftRotate(node);

        //LR
        if (balance > 1 && q.id > node.left.data.id) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && q.id < node.right.data.id) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }


    public Question search(int id) {
        Node res = search(root, id);
        return res == null ? null : res.data;
    }

    private Node search(Node node, int id) {
        if (node == null || node.data.id == id)
            return node;
        if (id < node.data.id)
            return search(node.left, id);
        return search(node.right, id);
    }


    public void inorder() {
        inorder(root);
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.println(node.data);
            inorder(node.right);
        }
    }


    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }


    public void delete(int id) {
        root = delete(root, id);
    }

    private Node delete(Node root, int id) {
        if (root == null) return root;

        if (id < root.data.id)
            root.left = delete(root.left, id);
        else if (id > root.data.id)
            root.right = delete(root.right, id);
        else {
            if (root.left == null || root.right == null) {
                Node temp = root.left != null ? root.left : root.right;
                if (temp == null) root = null;
                else root = temp;
            } else {
                Node temp = minValueNode(root.right);
                root.data = temp.data;
                root.right = delete(root.right, temp.data.id);
            }
        }

        if (root == null) return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // Balancing
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }
}

