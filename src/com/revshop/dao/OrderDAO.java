package com.revshop.dao;

import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.model.SoldItem;
import com.revshop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order) {
        String sql = "INSERT INTO ORDERS (order_id, buyer_id, total_amount, order_status, order_date, shipping_address, payment_method) " +
                     "VALUES (ORDER_SEQ.NEXTVAL, ?, ?, 'PENDING', SYSDATE, ?, ?)";
        // Need to return the generated ID
        String[] generatedColumns = {"order_id"};

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns)) {
            
            pstmt.setInt(1, order.getBuyerId());
            pstmt.setDouble(2, order.getTotalAmount());
            pstmt.setString(3, order.getShippingAddress());
            pstmt.setString(4, order.getPaymentMethod());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        	System.out.println("Failed to create order: " + e.getMessage());
        }
        return -1; // Fail
    }

    public boolean createOrderItem(OrderItem item) {
        String sql = "INSERT INTO ORDER_ITEMS (order_item_id, order_id, product_id, quantity, price_per_unit) " +
                     "VALUES (ORDER_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, item.getOrderId());
            pstmt.setInt(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getPricePerUnit());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
           // e.printStackTrace();
        	 System.out.println("Failed to create order item: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM ORDERS WHERE buyer_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, buyerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setBuyerId(rs.getInt("buyer_id"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                orders.add(order);
            }
        } catch (SQLException e) {
           // e.printStackTrace();
        	 System.out.println("Failed to fetch buyer orders: " + e.getMessage());
        }
        return orders;
    }
    
    
    
    public List<SoldItem> getSoldItemsBySeller(int sellerId) {

        List<SoldItem> list = new ArrayList<>();

        String sql =
        	    "SELECT oi.order_item_id, o.order_id, p.product_id, p.name, " +
        	    "oi.quantity, oi.price_per_unit, o.order_date, oi.status " +
        	    "FROM orders o " +
        	    "JOIN order_items oi ON o.order_id = oi.order_id " +
        	    "JOIN products p ON oi.product_id = p.product_id " +
        	    "WHERE p.seller_id = ? " +
        	    "ORDER BY o.order_date DESC";





        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                SoldItem s = new SoldItem();

                s.setOrderItemId(rs.getInt(1));   // oi.order_item_id
                s.setOrderId(rs.getInt(2));       // o.order_id
                s.setProductId(rs.getInt(3));     // p.product_id
                s.setProductName(rs.getString(4));
                s.setQuantity(rs.getInt(5));
                s.setPrice(rs.getDouble(6));
                s.setOrderDate(rs.getTimestamp(7));
                s.setStatus(rs.getString(8));

                list.add(s);
            }

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to fetch sold items for seller: " + e.getMessage());
        }

        return list;
    }
    
    
    public boolean updateOrderItemStatus(int orderItemId, String status) {

        String sql =
            "UPDATE order_items SET status = ? WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderItemId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to update order item status: " + e.getMessage());
        }
        return false;
    }

    
    public boolean areAllOrderItemsSuccess(int orderId) {

        String sql =
            "SELECT COUNT(*) FROM order_items " +
            "WHERE order_id = ? AND status <> 'SUCCESS'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to verify order item statuses: " + e.getMessage());
        }
        return false;
    }

    
    public void markOrderAsSuccess(int orderId) {

        String sql =
            "UPDATE orders SET order_status = 'SUCCESS' WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.executeUpdate();

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to update order status: " + e.getMessage());
        }
    }
    
    public int getOrderIdByOrderItemId(int orderItemId) {

        String sql =
            "SELECT order_id FROM order_items WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_id");
            }

        } catch (Exception e) {
           // e.printStackTrace();
        	 System.out.println("Failed to fetch order id for order item: " + e.getMessage());
        }

        return -1; // not found
    }




}
