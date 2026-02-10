package com.revshop.model;

import java.sql.Timestamp;

public class Favorite {

    private int favoriteId;
    private int userId;
    private int productId;
    private Timestamp addedAt;
    private String productName;

    // Default constructor
    public Favorite() {
    }

    // Constructor for creating new favorite (without ID and timestamp)
    public Favorite(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // Getters and Setters
    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Override
    public String toString() {
        return "Favorite{" +
                "favoriteId=" + favoriteId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", addedAt=" + addedAt +
                ", productName=" + productName+
                '}';
    }
}