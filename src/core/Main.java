package core;

import dataStructure.AvlTree;
import dataStructure.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static JFrame frame;
    private static String loggedUser = null;
    private static boolean isAdmin = false;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private static AvlTree avlTree = new AvlTree();
    private static LinkedList linkedList = new LinkedList();

    public static void main(String[] args) {
        preloadQuestions(avlTree, linkedList);
        SwingUtilities.invokeLater(Main::showHomeScreen);
    }


    private static void showHomeScreen() {
        frame = new JFrame("Interview Manager");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // FULL SCREEN
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JButton loginBtn = new JButton("User Login");
        JButton signupBtn = new JButton("Sign Up");
        JButton adminBtn = new JButton("Admin Login");

        Font btnFont = new Font("Arial", Font.BOLD, 28);
        loginBtn.setFont(btnFont);
        signupBtn.setFont(btnFont);
        adminBtn.setFont(btnFont);

        Dimension btnSize = new Dimension(300, 80);
        loginBtn.setPreferredSize(btnSize);
        signupBtn.setPreferredSize(btnSize);
        adminBtn.setPreferredSize(btnSize);

        loginBtn.addActionListener(e -> showLoginDialog(false));
        signupBtn.addActionListener(e -> showSignupDialog());
        adminBtn.addActionListener(e -> showLoginDialog(true));

        gbc.gridy = 0; panel.add(loginBtn, gbc);
        gbc.gridy = 1; panel.add(signupBtn, gbc);
        gbc.gridy = 2; panel.add(adminBtn, gbc);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }


    private static void showLoginDialog(boolean isAdminLogin) {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] fields = {"Username:", usernameField, "Password:", passwordField};

        int result = JOptionPane.showConfirmDialog(frame, fields,
                isAdminLogin ? "Admin Login" : "User Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!");
                return;
            }

            if (isAdminLogin) {
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    loggedUser = username;
                    isAdmin = true;
                    showMainDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid admin credentials!");
                }
            } else {
                String msg = UserDAO.login(username, password);
                if ("SUCCESS".equals(msg)) {
                    loggedUser = username;
                    isAdmin = false;
                    showMainDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, msg);
                }
            }
        }
    }

    private static void showSignupDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] fields = {"New Username:", usernameField, "New Password:", passwordField};

        int result = JOptionPane.showConfirmDialog(frame, fields, "User Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            String msg = UserDAO.signUp(username, password);
            if ("SUCCESS".equals(msg)) {
                JOptionPane.showMessageDialog(frame, "Sign up successful! Please log in.");
            } else {
                JOptionPane.showMessageDialog(frame, msg);
            }
        }
    }


    private static void showMainDashboard() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridLayout(9, 1, 15, 15));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel welcome = new JLabel("Welcome, " + loggedUser + (isAdmin ? " (Admin)" : ""), SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 26));

        JButton addBtn = new JButton("Add Question");
        JButton viewBtn = new JButton("View All Questions");
        JButton viewTreeBtn = new JButton("View AVL Tree");
        JButton searchBtn = new JButton("Search Question");
        JButton deleteBtn = new JButton("Delete Question");
        JButton modifyBtn = new JButton("Modify Question");
        JButton myQBtn = new JButton("View My Questions");
        JButton logoutBtn = new JButton("Logout");
        JButton exitBtn = new JButton("Exit");

        Font btnFont = new Font("Arial", Font.BOLD, 22);
        for (JButton b : new JButton[]{addBtn, viewBtn, viewTreeBtn, searchBtn, deleteBtn, modifyBtn, myQBtn, logoutBtn, exitBtn})
            b.setFont(btnFont);

        panel.add(welcome);
        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(viewTreeBtn);
        panel.add(searchBtn);
        panel.add(deleteBtn);
        panel.add(modifyBtn);
        panel.add(myQBtn);
        panel.add(logoutBtn);
        panel.add(exitBtn);

        addBtn.addActionListener(e -> showAddQuestionDialog());
        viewBtn.addActionListener(e -> showScrollableQuestions("All Questions", linkedList.display()));
        viewTreeBtn.addActionListener(e -> viewAVLTree());
        searchBtn.addActionListener(e -> searchQuestion());
        deleteBtn.addActionListener(e -> deleteQuestion());
        modifyBtn.addActionListener(e -> modifyQuestion());
        myQBtn.addActionListener(e -> showMyQuestions());
        logoutBtn.addActionListener(e -> {
            QuestionDAO.saveAllQuestions(linkedList.toList());
            loggedUser = null;
            isAdmin = false;
            showHomeScreen();
        });
        exitBtn.addActionListener(e -> {
            QuestionDAO.saveAllQuestions(linkedList.toList());
            System.exit(0);
        });

        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }


    private static void showAddQuestionDialog() {
        int id = -1;
        while (true) {
            String idStr = JOptionPane.showInputDialog(frame, "Enter Question ID:");
            if (idStr == null) return;
            try {
                id = Integer.parseInt(idStr);
                if (id <= 0) JOptionPane.showMessageDialog(frame, "ID must be a positive integer.");
                else if (avlTree.search(id) != null) JOptionPane.showMessageDialog(frame, "ID already exists!");
                else break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "ID must be an integer!");
            }
        }

        String questionText = "";
        while (true) {
            questionText = JOptionPane.showInputDialog(frame, "Enter Question:");
            if (questionText == null) return;
            if (questionText.trim().isEmpty()) JOptionPane.showMessageDialog(frame, "Question cannot be empty!");
            else break;
        }

        String company = "";
        while (true) {
            company = JOptionPane.showInputDialog(frame, "Enter Company:");
            if (company == null) return;
            if (company.trim().isEmpty()) JOptionPane.showMessageDialog(frame, "Company cannot be empty!");
            else break;
        }

        String college = "";
        while (true) {
            college = JOptionPane.showInputDialog(frame, "Enter College:");
            if (college == null) return;
            if (college.trim().isEmpty()) JOptionPane.showMessageDialog(frame, "College cannot be empty!");
            else break;
        }

        String difficulty = "";
        while (true) {
            difficulty = JOptionPane.showInputDialog(frame, "Enter Difficulty (easy, medium, hard):");
            if (difficulty == null) return;
            difficulty = difficulty.trim().toLowerCase();
            if (difficulty.isEmpty()) JOptionPane.showMessageDialog(frame, "Difficulty cannot be empty!");
            else if (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard"))
                JOptionPane.showMessageDialog(frame, "Invalid difficulty! Enter only easy, medium, or hard.");
            else break;
        }

        String topic = "";
        while (true) {
            topic = JOptionPane.showInputDialog(frame, "Enter Topic:");
            if (topic == null) return;
            if (topic.trim().isEmpty()) JOptionPane.showMessageDialog(frame, "Topic cannot be empty!");
            else break;
        }
        String solution = JOptionPane.showInputDialog(frame, "Enter Solution Link (e.g., https://en.wikipedia.org/...):");
        if (solution == null) solution = "";


        Question q = new Question(id, questionText, company, college, difficulty, topic, loggedUser, solution);

        avlTree.insert(q);
        linkedList.insert(q);
        JOptionPane.showMessageDialog(frame, "Question added successfully!");
    }


    private static void viewAVLTree() {
        JFrame treeFrame = new JFrame("AVL Tree");
        treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        treeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        TreePanel panel = new TreePanel(avlTree);
        JScrollPane scrollPane = new JScrollPane(panel);
        treeFrame.add(scrollPane);
        treeFrame.setVisible(true);
    }


    private static void showScrollableQuestions(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        JOptionPane.showMessageDialog(frame, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }


    private static void showMyQuestions() {
        StringBuilder sb = new StringBuilder();
        boolean has = linkedList.displayByUser(loggedUser, sb);
        if (!has) JOptionPane.showMessageDialog(frame, "No questions created by you.");
        else showScrollableQuestions("My Questions", sb.toString());
    }


    private static void searchQuestion() {
        String idStr = JOptionPane.showInputDialog("Enter Question ID to search:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);

            long startAVL = System.nanoTime();
            Question avlFound = avlTree.search(id);
            long endAVL = System.nanoTime();

            long startList = System.nanoTime();
            Question listFound = linkedList.search(id);
            long endList = System.nanoTime();

            StringBuilder result = new StringBuilder("=== Search Results ===\n");
            if (avlFound != null) result.append("Found in AVL Tree:\n").append(avlFound).append("\n");
            else result.append("Not found in AVL Tree.\n");

            if (listFound != null) result.append("\nFound in LinkedList:\n").append(listFound).append("\n");
            else result.append("\nNot found in LinkedList.\n");

            result.append("\nSearch Times:\n")
                    .append("AVL Tree: ").append(endAVL - startAVL).append(" ns\n")
                    .append("LinkedList: ").append(endList - startList).append(" ns");

            showScrollableQuestions("Search Result", result.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID!");
        }
    }

    private static void deleteQuestion() {
        String idStr = JOptionPane.showInputDialog("Enter Question ID to delete:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Question q = avlTree.search(id);
            if (q == null) JOptionPane.showMessageDialog(frame, "No question found!");
            else if (!isAdmin && !q.createdBy.equals(loggedUser)) JOptionPane.showMessageDialog(frame, "You can only delete your own questions.");
            else {
                avlTree.delete(id);
                linkedList.delete(id);
                JOptionPane.showMessageDialog(frame, "Question deleted successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID!");
        }
    }

    private static void modifyQuestion() {
        String idStr = JOptionPane.showInputDialog("Enter Question ID to modify:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Question q = avlTree.search(id);
            if (q == null) { JOptionPane.showMessageDialog(frame, "No question found!"); return; }
            if (!isAdmin && !q.createdBy.equals(loggedUser)) { JOptionPane.showMessageDialog(frame, "You can only modify your own questions."); return; }

            JTextField qField = new JTextField(q.getQuestion());
            JTextField cField = new JTextField(q.getCompany());
            JTextField colField = new JTextField(q.getCollege());
            JTextField dField = new JTextField(q.getDifficulty());
            JTextField tField = new JTextField(q.getTopic());

            Object[] fields = {"Question:", qField, "Company:", cField, "College:", colField, "Difficulty:", dField, "Topic:", tField};
            int res = JOptionPane.showConfirmDialog(frame, fields, "Modify Question", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                Question updated = new Question(id, qField.getText(), cField.getText(), colField.getText(),
                        dField.getText(), tField.getText(), q.createdBy);
                avlTree.delete(id);
                linkedList.delete(id);
                avlTree.insert(updated);
                linkedList.insert(updated);
                JOptionPane.showMessageDialog(frame, "Question updated successfully!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID!");
        }
    }


    private static void preloadQuestions(AvlTree avlTree, LinkedList linkedList) {
        var questions = QuestionDAO.loadAllQuestions();
        for (Question q : questions) {
            avlTree.insert(q);
            linkedList.insert(q);
        }
    }


    static class TreePanel extends JPanel {
        private AvlTree tree;
        private Map<Rectangle, Question> nodePositions = new HashMap<>();

        public TreePanel(AvlTree tree) {
            this.tree = tree;
            setPreferredSize(new Dimension(1200, 800));

            // Click to show question details
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Rectangle rect : nodePositions.keySet()) {
                        if (rect.contains(e.getPoint())) {
                            Question q = nodePositions.get(rect);
                            int res = JOptionPane.showConfirmDialog(TreePanel.this,
                                    q.toString() + (q.getSolution().isEmpty() ? "" : "\n\nOpen solution link?"),
                                    "Question ID: " + q.getId(),
                                    q.getSolution().isEmpty() ? JOptionPane.DEFAULT_OPTION : JOptionPane.YES_NO_OPTION);

                            if (!q.getSolution().isEmpty() && res == JOptionPane.YES_OPTION) {
                                try {
                                    Desktop.getDesktop().browse(new java.net.URI(q.getSolution()));
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(TreePanel.this, "Unable to open link!");
                                }
                            }
                            break;
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            nodePositions.clear();
            if (tree.getRoot() != null) {
                int initialX = getWidth() / 2;
                int initialY = 30;
                int initialOffset = getWidth() / 4; // starting horizontal spacing
                drawNode(g, tree.getRoot(), initialX, initialY, initialOffset);
            }
        }

        private void drawNode(Graphics g, AvlTree.Node node, int x, int y, int xOffset) {
            int radius = 20;
            int levelSpacing = 75;
            int minOffset = 50;   

            // Draw node circle
            g.setColor(Color.BLACK);
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

            // Draw node text
            g.setColor(Color.WHITE);
            String text = String.valueOf(node.getData().getId());
            g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y + 5);

            // Save position for click events
            nodePositions.put(new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius), node.getData());

            // Draw left child
            if (node.getLeft() != null) {
                int childX = x - Math.max(xOffset, minOffset);
                int childY = y + levelSpacing;
                g.setColor(Color.BLACK);
                g.drawLine(x, y + radius, childX, childY - radius);
                drawNode(g, node.getLeft(), childX, childY, xOffset / 2);
            }

            // Draw right child
            if (node.getRight() != null) {
                int childX = x + Math.max(xOffset, minOffset);
                int childY = y + levelSpacing;
                g.setColor(Color.BLACK);
                g.drawLine(x, y + radius, childX, childY - radius);
                drawNode(g, node.getRight(), childX, childY, xOffset / 2);
            }
        }
    }

}
