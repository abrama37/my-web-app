package com.myapp.repository;

import com.myapp.db.DatabaseManager;
import com.myapp.model.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    private Meal mapRow(ResultSet rs) throws SQLException {
        return new Meal(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getDouble("rating"),
            rs.getString("created_at")
        );
    }

    public List<Meal> getAllMeals() throws Exception {
        List<Meal> meals = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM meals")) {
            while (rs.next()) meals.add(mapRow(rs));
        }
        return meals;
    }

    public Meal getRandomMeal() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM meals ORDER BY RANDOM() LIMIT 1")) {
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public void addMeal(String name, String category, String description) throws Exception {
        String sql = "INSERT INTO meals (name, category, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, description);
            ps.executeUpdate();
        }
    }

    public void deleteMeal(int id) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM meals WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void updateRating(int id, double rating) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE meals SET rating = ? WHERE id = ?")) {
            ps.setDouble(1, rating);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void updateDescription(int id, String description) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE meals SET description = ? WHERE id = ?")) {
            ps.setString(1, description);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
