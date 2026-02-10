package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.dao.OrderDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.model.Product;
import com.revshop.model.SoldItem;

import java.util.List;

public class OrderService {

    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private NotificationService notificationService = new NotificationService();


    public OrderService() {
        this.cartDAO = new CartDAO();
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
    }

    public boolean addToCart(int buyerId, int productId, int quantity) {
        Product product = productDAO.getProductById(productId);
        if (product == null || product.getStockQuantity() < quantity) {
            return false;
        }
        CartItem item = new CartItem();
        item.setBuyerId(buyerId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return cartDAO.addToCart(item);
    }

    public List<CartItem> getCartItems(int buyerId) {
        return cartDAO.getCartItems(buyerId);
    }
    
    public boolean removeFromCart(int buyerId, int productId) {
        return cartDAO.removeFromCart(buyerId, productId);
    }

    public boolean placeOrder(int buyerId, String shippingAddress, String paymentMethod) {

        // 1. Get cart items
        List<CartItem> cartItems = cartDAO.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return false;
        }

        // 2. Calculate total amount & validate stock
        double totalAmount = 0;

        for (CartItem item : cartItems) {
            Product p = productDAO.getProductById(item.getProductId());

            if (p.getStockQuantity() < item.getQuantity()) {
                System.out.println("Insufficient stock for product: " + p.getName());
                return false;
            }

            totalAmount += p.getPrice() * item.getQuantity();
        }

        // 3. Create Order
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);

        int orderId = orderDAO.createOrder(order);

        if (orderId == -1) {
            System.out.println("Failed to create order.");
            return false;
        }

        // 4. Create Order Items, Update Stock, Notify Sellers
        for (CartItem item : cartItems) {

            Product p = productDAO.getProductById(item.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPricePerUnit(p.getPrice());

            // create order item
            orderDAO.createOrderItem(orderItem);

            // update stock
            productDAO.updateStock(p.getProductId(), item.getQuantity());

            //  Notify Seller
            notificationService.notifyUser(
                p.getSellerId(),
                "New order received for your product: " + p.getName()
            );
        }

        // 5. Clear cart
        cartDAO.clearCart(buyerId);

        //  Notify Buyer
        notificationService.notifyUser(
            buyerId,
            "Your order has been placed successfully. Order ID: " + orderId
        );

        return true;
    }


    public List<Order> getOrderHistory(int buyerId) {
        return orderDAO.getOrdersByBuyer(buyerId);
    }
    
    public List<SoldItem> getSoldItems(int sellerId) {
        return orderDAO.getSoldItemsBySeller(sellerId);
    }
    
    public boolean acceptOrderItem(int orderItemId) {

        // 1. Update order_items status using order_item_id
        boolean updated =
            orderDAO.updateOrderItemStatus(orderItemId, "SUCCESS");

        if (!updated) {
            return false;
        }

        // 2. Get order_id for this order_item
        int orderId =
            orderDAO.getOrderIdByOrderItemId(orderItemId);

        // 3. Check if all order_items of this order are SUCCESS
        boolean allSuccess =
            orderDAO.areAllOrderItemsSuccess(orderId);

        // 4. If yes, update orders table
        if (allSuccess) {
            orderDAO.markOrderAsSuccess(orderId);
        }

        return true;
    }




}
