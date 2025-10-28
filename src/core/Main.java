package core;
import dataStructure.AvlTree;
import dataStructure.LinkedList;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("=== Welcome to Interview Manager ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter 1 or 2.");
                continue;
            }

            switch(option) {
                case 1:
                    while (true) {
                        System.out.print("Enter username: ");
                        String newUser = sc.nextLine().trim();
                        System.out.print("Enter password: ");
                        String newPass = sc.nextLine().trim();

                        if(UserDAO.signUp(newUser, newPass)) {
                            System.out.println("Sign up successful! Please login.");
                            break;
                        } else {
                            System.out.println("Try again with valid username and password.");
                        }
                    }
                    break;

                case 2:
                    while (true) {
                        System.out.print("Enter username: ");
                        String username = sc.nextLine().trim();
                        System.out.print("Enter password: ");
                        String password = sc.nextLine().trim();

                        if(UserDAO.login(username, password)) {
                            System.out.println("Login successful! Welcome " + username);
                            loggedIn = true;
                            break;
                        } else {
                            System.out.println("Invalid username or password. Try again.");
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option. Enter 1 or 2.");
            }
        }
        AvlTree avlTree = new AvlTree();
        LinkedList linkedList = new LinkedList();
        preloadQuestions(avlTree,linkedList);
        int id;
        String question;
        String company;
        String college;
        String diff;
        String topic;
        while (true) {
            System.out.println("\n=== Interview Question Manager ===");
            System.out.println("1. Add Question");
            System.out.println("2. View Question");
            System.out.println("3. Search Question");
            System.out.println("4. Delete Question");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;

            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    while (true) {
                        System.out.print("Enter Question ID (positive integer): ");
                        String input = sc.nextLine();
                        try {
                            id = Integer.parseInt(input);
                            if (id <= 0) {
                                System.out.println("ID must be a positive number.");
                            } else if (avlTree.search(id) != null) {
                                System.out.println("A question with this ID already exists. Please enter a different ID.");
                            } else {
                                break; // valid and unique ID
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid integer ID.");
                        }
                    }

                    do {
                        System.out.print("Enter Question: ");
                        question = sc.nextLine().trim();
                        if (question.isEmpty())
                            System.out.println("Question cannot be empty.");
                    } while (question.isEmpty());

                    do {
                        System.out.print("Enter Company Name: ");
                        company = sc.nextLine().trim();
                        if (company.isEmpty())
                            System.out.println("Company name cannot be empty.");
                    } while (company.isEmpty());

                    do {
                        System.out.print("Enter College Name: ");
                        college = sc.nextLine().trim();
                        if (college.isEmpty())
                            System.out.println("College name cannot be empty.");
                    } while (college.isEmpty());

                    do {
                        System.out.print("Enter Difficulty Level (Easy, Medium, Hard): ");
                        diff = sc.nextLine().trim();
                        if (!validDifficulty(diff)) {
                            System.out.println("Invalid difficulty level. Please enter Easy, Medium, or Hard.");
                            diff = "";
                        }
                    } while (diff.isEmpty());

                    do {
                        System.out.print("Enter Topic: ");
                        topic = sc.nextLine().trim();
                        if (topic.isEmpty())
                            System.out.println("Topic cannot be empty.");
                    } while (topic.isEmpty());

                    Question q = new Question(id, question, company, college, diff, topic);
                    avlTree.insert(q);
                    linkedList.insert(q);
                    System.out.println("Question added successfully!");
                    break;

                case 2:
                    System.out.println("All Questions:");
                    avlTree.inorder();
                    break;

                case 3:
                    System.out.print("Enter Question ID to search: ");
                    try {
                        int searchId = Integer.parseInt(sc.nextLine());

                        long startAVL = System.nanoTime();
                        Question avlFound = avlTree.search(searchId);
                        long endAVL = System.nanoTime();
                        long avlTime = endAVL - startAVL;


                        long startList = System.nanoTime();
                        Question listFound = linkedList.search(searchId);
                        long endList = System.nanoTime();
                        long listTime = endList - startList;


                        if (avlFound != null)
                            System.out.println("Found in AVL Tree:\n" + avlFound);
                        else
                            System.out.println("Question not found in AVL Tree.");

                        if (listFound != null)
                            System.out.println("Found in LinkedList:\n" + listFound);
                        else
                            System.out.println("Question not found in LinkedList.");

                        System.out.println("\nSearch time:");
                        System.out.println("AVL Tree: " + avlTime + " ns");
                        System.out.println("LinkedList: " + listTime + " ns");

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Question ID to delete: ");
                    try {
                        int deleteId = Integer.parseInt(sc.nextLine());
                        avlTree.delete(deleteId);
                        linkedList.delete(deleteId);
                        System.out.println("Deletion done (if ID existed).");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting program...");
                    System.out.println("🙋‍♂️🙋‍♂️🙋Press enter to exit...");
                    sc.nextLine();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
    private static boolean validDifficulty(String difficulty) {
        difficulty=difficulty.toLowerCase();
        if("easy".equals(difficulty)) return true;
        else if("medium".equals(difficulty)) return true;
        else if("hard".equals(difficulty)) return true;
        else return false;
    }

    private static void preloadQuestions(AvlTree avlTree,LinkedList linkedList) {
        String[] companies = {"TCS", "Cognizant", "Wipro", "Infosys", "HCL", "Zoho", "Google", "Microsoft"};
        String[] colleges = {"Anna University", "VIT Vellore", "SRM Institute", "CEG Guindy", "PSG College", "IIT Madras"};
        String[] difficulties = {"Easy", "Medium", "Hard"};
        String[] topics = {"Arrays", "Linked Lists", "Strings", "Math", "Dynamic Programming", "Graphs", "Trees"};
        for (int i = 1; i <= 500; i++) {
            int id = 100 + i;
            String question = "";
            switch(i % 10) {
                case 1: question = "Two Sum"; break;
                case 2: question = "Add Two Numbers (Linked List)"; break;
                case 3: question = "Longest Substring Without Repeating Characters"; break;
                case 4: question = "Median of Two Sorted Arrays"; break;
                case 5: question = "Longest Palindromic Substring"; break;
                case 6: question = "ZigZag Conversion"; break;
                case 7: question = "Reverse Integer"; break;
                case 8: question = "String to Integer (atoi)"; break;
                case 9: question = "Palindrome Number"; break;
                case 0: question = "Container With Most Water"; break;
            }
            String company = companies[i % companies.length];
            String college = colleges[i % colleges.length];
            String diff = difficulties[i % difficulties.length];
            String topic = topics[i % topics.length];

            Question q = new Question(id, question, company, college, diff, topic);
            avlTree.insert(q);
            linkedList.insert(q);
        }
    }
}


