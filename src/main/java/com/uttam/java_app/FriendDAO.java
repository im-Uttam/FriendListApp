package com.uttam.java_app;
import java.sql.*;

public class FriendDAO {

    // Add a new friend
    public void addFriend(int userId, String name, String contact, String email, String date) {
        String sql = "INSERT INTO friends (user_id, friend_name, contact, email, created_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, contact);
            stmt.setString(4, email);
            stmt.setString(5, date);
            stmt.executeUpdate();
            System.out.println("✅ Friend added!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Get all friends
    public void getAllFriends() {
        String sql = "SELECT * FROM friends";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: "      + rs.getInt("id"));
                System.out.println("Name: "    + rs.getString("friend_name"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Email: "   + rs.getString("email"));
                System.out.println("Date: "    + rs.getString("created_date"));
                System.out.println("---");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Delete a friend by ID
    public void deleteFriend(int friendId) {
        String sql = "DELETE FROM friends WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, friendId);
            stmt.executeUpdate();
            System.out.println("✅ Friend deleted!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}