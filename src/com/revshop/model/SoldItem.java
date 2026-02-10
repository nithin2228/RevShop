package com.revshop.model;

import java.sql.Timestamp;

public class SoldItem {
	
    private int orderId;
    private String productName;
    private int quantity;
    private double price;
    private Timestamp orderDate;
    private String status;
    private int orderItemId;
    private int productId;


    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) 
    {
        this.orderDate = orderDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        
    }
    
    
    public int getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
	@Override
	public String toString() {
		return "SoldItem [orderId=" + orderId + ", productName=" + productName + ", quantity=" + quantity + ", price="
				+ price + ", orderDate=" + orderDate + ", status=" + status + ", orderItemId=" + orderItemId
				+ ", productId=" + productId + "]";
	}


    
    
    

}
