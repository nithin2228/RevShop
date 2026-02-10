package com.revshop.service;

import com.revshop.dao.FavoriteDAO;
import com.revshop.model.Favorite;
import java.util.List;

public class FavoriteService {

    private FavoriteDAO favoriteDAO = new FavoriteDAO();

    public boolean addToFavorites(int userId, int productId) {
        return favoriteDAO.addToFavorites(userId, productId);
    }

    public boolean removeFromFavorites(int userId, int productId) {
        return favoriteDAO.removeFromFavorites(userId, productId);
    }

    public List<Favorite> getUserFavorites(int userId) {
        return favoriteDAO.getFavoritesByUser(userId);
    }

    public boolean isProductInFavorites(int userId, int productId) {
        return favoriteDAO.isFavorite(userId, productId);
    }
}