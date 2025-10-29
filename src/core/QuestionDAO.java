package core;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/interview_manager";
    private static final String USER = "root";
    private static final String PASS = "";

    public static List<Question> loadAllQuestions() {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("company"),
                        rs.getString("college"),
                        rs.getString("difficulty"),
                        rs.getString("topic"),
                        rs.getString("createdBy")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.out.println("Error loading questions from DB: " + e.getMessage());
        }
        return questions;
    }

    public static void saveAllQuestions(List<Question> questions) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.createStatement().executeUpdate("DELETE FROM questions"); // clear old
            String sql = "INSERT INTO questions (id, question, company, college, difficulty, topic, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Question q : questions) {
                    ps.setInt(1, q.getId());
                    ps.setString(2, q.getQuestion());
                    ps.setString(3, q.getCompany());
                    ps.setString(4, q.getCollege());
                    ps.setString(5, q.getDifficulty());
                    ps.setString(6, q.getTopic());
                    ps.setString(7, q.createdBy);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            System.out.println("Questions saved to database successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving questions to DB: " + e.getMessage());
        }
    }
}
