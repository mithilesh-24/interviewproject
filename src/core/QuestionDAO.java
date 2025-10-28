package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public static List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        String query = "SELECT * FROM questions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("company"),
                        rs.getString("college"),
                        rs.getString("difficulty"),
                        rs.getString("topic")
                );
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveQuestions(List<Question> questions) {
        String deleteQuery = "DELETE FROM questions"; // clear table
        String insertQuery = "INSERT INTO questions (id, question, company, college, difficulty, topic) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(insertQuery)) {

            conn.setAutoCommit(false);
            stmt.executeUpdate(deleteQuery); // remove old rows

            for (Question q : questions) {
                ps.setInt(1, q.getId());          // use getter
                ps.setString(2, q.getQuestion()); // use getter
                ps.setString(3, q.getCompany());  // use getter
                ps.setString(4, q.getCollege());  // use getter
                ps.setString(5, q.getDifficulty());// use getter
                ps.setString(6, q.getTopic());    // use getter
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
