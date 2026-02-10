package com.revshop.service;

import java.util.List;

import com.revshop.dao.NotificationDAO;
import com.revshop.model.Notification;

public class NotificationService {

    private NotificationDAO dao = new NotificationDAO();

    public void notifyUser(int userId, String message) {
        dao.addNotification(userId, message);
    }

    public List<Notification> getNotifications(int userId) {
        return dao.getUserNotifications(userId);
    }
}
