package com.revshop.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revshop.model.Notification;
import com.revshop.util.DBConnection;

public class NotificationDAO {

    public void addNotification(int userId, String message) {

    	String sql =
    		    "INSERT INTO notifications (notification_id, user_id, message) " +
    		    "VALUES (notifications_seq.NEXTVAL, ?, ?)";


        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to add notification: " + e.getMessage());
        }
    }

    public List<Notification> getUserNotifications(int userId) {

        List<Notification> list = new ArrayList<>();

        String sql =
            "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(rs.getDate("created_at"));
                n.setIsRead(rs.getString("is_read"));
                list.add(n);
            }

        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Failed to fetch notifications: " + e.getMessage());
        }
        return list;
    }
}
