package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public boolean addProduct(Product product) {
        if (product.getPrice() < 0) return false;
        return productDAO.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> getProductsBySeller(int sellerId) {
        return productDAO.getProductsBySeller(sellerId);
    }
    
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> allProducts = productDAO.getAllProducts();
        return allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                             p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
         List<Product> allProducts = productDAO.getAllProducts();
         return allProducts.stream()
                 .filter(p -> p.getCategoryId() == categoryId)
                 .collect(Collectors.toList());
    }
    
    public boolean updateProduct(Product p) {
        return productDAO.updateProduct(p);
    }

    public boolean deleteProduct(int productId, int sellerId) {
        return productDAO.deleteProduct(productId, sellerId);
    }

}
