package core;

import java.sql.*;

public class UserDAO {


    public static boolean signUp(String username, String password) {
        if (!isValidUsername(username)) {
            System.out.println("Sign up failed: Username must be 3-20 characters, alphanumeric or underscores.");
            return false;
        }
        if (!isValidPassword(password)) {
            System.out.println("Sign up failed: Password must be at least 6 characters, include letters and numbers.");
            return false;
        }

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Sign up failed: " + e.getMessage());
            return false;
        }
    }


    public static boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Login failed: Username and password cannot be empty.");
            return false;
        }

        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }


    private static boolean isValidUsername(String username) {
        return username != null && username.matches("\\w{3,20}");
    }

    private static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*");
    }
}
