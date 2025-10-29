package core;
import dataStructure.AvlTree;
import dataStructure.LinkedList;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;
        String loggedUser = null;
        AvlTree avlTree = new AvlTree();
        LinkedList linkedList = new LinkedList();
        preloadQuestions(avlTree,linkedList);
        while (true) {
            while (!loggedIn) {
                System.out.println("=== Welcome to Interview Manager ===");
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int option;
                try {
                    option = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter 1, 2, or 3.");
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
                                loggedUser = username;
                                break;
                            } else {
                                System.out.println("Invalid username or password. Try again.");
                            }
                        }
                        break;

                    case 3:
                        System.out.println("Exiting program... Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Enter 1, 2, or 3.");
                }
            }


            int id;
            String question;
            String company;
            String college;
            String diff;
            String topic;

            while (loggedIn) {
                System.out.println("\n=== Interview Question Manager ===");
                System.out.println("1. Add Question");
                System.out.println("2. View Question");
                System.out.println("3. Search Question");
                System.out.println("4. Delete Question");
                System.out.println("5. Logout");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Enter a valid number.");
                    continue;
                }

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
                                    break;
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

                        Question q = new Question(id, question, company, college, diff, topic, loggedUser);
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
                            Question qToDelete = avlTree.search(deleteId);

                            if (qToDelete == null) {
                                System.out.println("No question found with that ID.");
                            } else if (!qToDelete.createdBy.equals(loggedUser)) {
                                System.out.println("You can only delete questions you added.");
                            } else {
                                avlTree.delete(deleteId);
                                linkedList.delete(deleteId);
                                System.out.println("Question deleted successfully.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format.");
                        }
                        break;

                    case 5:
                        System.out.println("Saving questions to MySQL...");
                        QuestionDAO.saveAllQuestions(linkedList.toList());
                        System.out.println("Logging out...");
                        loggedIn = false;
                        loggedUser = null;
                        break;

                    case 6:
                        System.out.println("Saving questions to MySQL...");
                        QuestionDAO.saveAllQuestions(linkedList.toList());
                        System.out.println("Exiting program...");
                        System.out.println("🙋‍♂️🙋‍♂️🙋Press enter to exit...");
                        sc.nextLine();
                        return;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        }
    }

    private static boolean validDifficulty(String difficulty) {
        difficulty = difficulty.toLowerCase();
        return "easy".equals(difficulty) || "medium".equals(difficulty) || "hard".equals(difficulty);
    }

    private static void preloadQuestions(AvlTree avlTree, LinkedList linkedList) {
        var questions = QuestionDAO.loadAllQuestions();
        for (Question q : questions) {
            avlTree.insert(q);
            linkedList.insert(q);
        }
    }
}
