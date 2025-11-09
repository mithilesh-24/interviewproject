package core;

import java.sql.*;

public class UserDAO {

    // ---------------- SIGN UP ----------------
    public static String signUp(String username, String password) {
        if (!isValidUsername(username)) {
            return "Username must be 3–20 characters, alphanumeric or underscores.";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long and contain letters and numbers.";
        }

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate")) {
                return "Username already exists. Choose another.";
            }
            return "Database error: " + e.getMessage();
        }
    }

    // ---------------- LOGIN ----------------
    public static String login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "Username and password cannot be empty.";
        }

        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "SUCCESS";
            } else {
                return "Invalid username or password.";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    // ---------------- USERNAME/PASSWORD VALIDATION ----------------
    private static boolean isValidUsername(String username) {
        return username != null && username.matches("\\w{3,20}");
    }

    private static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*");
    }
}
