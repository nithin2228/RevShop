package com.revshop.dao;

import com.revshop.model.Favorite;
import com.revshop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    /**
     * Adds a product to user's favorites
     * Returns true if added successfully (or already exists - thanks to UNIQUE constraint)
     */
    public boolean addToFavorites(int userId, int productId) {
        String sql = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
        	 System.out.println("Failed to add product to favorites: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a product from user's favorites
     */
    public boolean removeFromFavorites(int userId, int productId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
           // e.printStackTrace();
        	System.out.println("Failed to remove product from favorites: " + e.getMessage());

            return false;
        }
    }

    /**
     * Get all favorite products for a user
     */
    public List<Favorite> getFavoritesByUser(int userId) {
        List<Favorite> favorites = new ArrayList<>();
        String sql = "SELECT f.*, p.name AS product_name " +
                     "FROM favorites f " +
                     "JOIN products p ON f.product_id = p.product_id " +
                     "WHERE f.user_id = ? " +
                     "ORDER BY f.added_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Favorite fav = new Favorite();
                fav.setFavoriteId(rs.getInt("favorite_id"));
                fav.setUserId(rs.getInt("user_id"));
                fav.setProductId(rs.getInt("product_id"));
                fav.setAddedAt(rs.getTimestamp("added_at"));
                fav.setProductName(rs.getString("product_name"));

                // You can add product_name to model later if needed
                favorites.add(fav);
            }

        } catch (SQLException e) {
           // e.printStackTrace();
        	 System.out.println("Error while fetching favorites: " + e.getMessage());
        }
        return favorites;
    }

    /**
     * Check if product is already in favorites
     */
    public boolean isFavorite(int userId, int productId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            return rs.next();  // true = already exists
        } catch (SQLException e) {
            System.err.println("Error checking favorite: " + e.getMessage());
            return false;  // safe default
        }
    }
}