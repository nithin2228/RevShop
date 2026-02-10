package com.revshop.ui;

import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.model.Order;
import com.revshop.model.CartItem;
import com.revshop.model.Favorite;
import com.revshop.model.Notification;
import com.revshop.model.Review;
import com.revshop.model.SoldItem;
import com.revshop.service.AuthService;
import com.revshop.service.FavoriteService;
import com.revshop.service.NotificationService;
import com.revshop.service.OrderService;
import com.revshop.service.ProductService;
import com.revshop.service.ReviewService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private AuthService authService;
    private ProductService productService;
    private OrderService orderService;
    private ReviewService reviewService;
    private User currentUser;
    private User loggedInUser;

    private FavoriteService favoriteService = new FavoriteService();

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
        this.productService = new ProductService();
        this.orderService = new OrderService();
        this.reviewService = new ReviewService();
       
    }

    public void start() {
        System.out.println("Welcome to RevShop - Your Trusted E-Commerce Platform");
        
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                if ("SELLER".equalsIgnoreCase(currentUser.getRole())) {
                    showSellerMenu();
                } else {
                    showBuyerMenu();
                }
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Forgot Password");
        System.out.println("4. Exit");
        System.out.print("Enter choice: ");
        
        int choice = readInt();
        switch (choice) {
            case 1: register(); break;
            case 2: login(); break;
            case 3: forgotPassword(); break;
            case 4: 
                System.out.println("Goodbye!"); 
                System.exit(0);
                break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void register() {
        System.out.println("\n--- Register ---");

        String email;
        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();

            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }

        //  Password validation loop
        String password;
        while (true) {
            System.out.print("Enter Password: ");
            password = scanner.nextLine();

            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println(
                    "Invalid password.\n" +
                    "Password must contain:\n" +
                    "- At least 8 characters\n" +
                    "- One uppercase letter\n" +
                    "- One lowercase letter\n" +
                    "- One digit\n" +
                    "- One special character"
                );
            }
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Role (BUYER/SELLER): ");
        String role = scanner.nextLine().toUpperCase();

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        System.out.print("Security Question: ");
        String question = scanner.nextLine();

        System.out.print("Security Answer: ");
        String answer = scanner.nextLine();

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setName(name);
        user.setRole(role);
        user.setPhone(phone);
        user.setAddress(address);
        user.setSecurityQuestion(question);
        user.setSecurityAnswer(answer);

        if (authService.registerUser(user)) {
            System.out.println("Registration Successful! Please login.");
        } else {
            System.out.println("Registration Failed. Email might already exist.");
        }
    }


    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = authService.login(email, password);
        if (user != null) {
            currentUser = user;
            loggedInUser = user;
            System.out.println("Login Successful! Welcome, " + user.getName());
        } else {
            System.out.println("Invalid Credentials.");
        }
    }
    
    private void forgotPassword() {

        System.out.println("\n--- Forgot Password ---");

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        User user = authService.getUserByEmail(email);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Security Question: " + user.getSecurityQuestion());
        System.out.print("Enter Answer: ");
        String answer = scanner.nextLine();

        String newPassword;
        String confirmPassword;

        while (true) {

            //  New password
            System.out.print("Enter New Password: ");
            newPassword = scanner.nextLine();

            if (!isValidPassword(newPassword)) {
                System.out.println(
                    "Invalid password.\n" +
                    "Password must contain:\n" +
                    "- At least 8 characters\n" +
                    "- One uppercase letter\n" +
                    "- One lowercase letter\n" +
                    "- One digit\n" +
                    "- One special character"
                );
                continue;
            }

            //  Confirm password
            System.out.print("Confirm New Password: ");
            confirmPassword = scanner.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }

            break; //  valid & matched
        }

        if (authService.resetPassword(email, answer, newPassword)) {
            System.out.println("Password reset successfully. Please login.");
        } else {
            System.out.println("Security answer incorrect. Password reset failed.");
        }
    }


    private void showSellerMenu() {
        System.out.println("\n--- Seller Dashboard ---");
        System.out.println("1. Add Product");
        System.out.println("2. View My Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. View Orders (Sold Items) "); 
        System.out.println("6. Accept Order Item");
        System.out.println("7. View Notifications");

        System.out.println("8. Logout");
        System.out.print("Enter choice: ");
        
        int choice = readInt();
        switch (choice) {
            case 1: addProduct(); break;
            case 2: viewMyProducts(); break;
            case 3: updateProduct(); break;
            case 4: deleteProduct(); break;
            case 5: viewSoldItems(); break; // Can implement using OrderDAO querying order items by seller products
            case 6: acceptOrderItem(); break;
            case 7: viewNotifications(); break;
            case 8: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void addProduct() {
        System.out.println("\n--- Add Product ---");
        System.out.print("Category ID (1:Electronics, 2:Fashion, 3:Books, 4:Home): ");
        int catId = readInt();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Price: ");
        double price = readDouble();
        System.out.print("MRP: ");
        double mrp = readDouble();
        System.out.print("Stock Quantity: ");
        int stock = readInt();
        
        Product p = new Product();
        p.setSellerId(currentUser.getUserId());
        p.setCategoryId(catId);
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setMrp(mrp);
        p.setStockQuantity(stock);
        p.setImageUrl("N/A");
        p.setActive(true);
        
        if (productService.addProduct(p)) {
            System.out.println("Product Added Successfully!");
        } else {
            System.out.println("Failed to add product.");
        }
    }
    
    private void viewMyProducts() {
        System.out.println("\n--- My Products ---");
        List<Product> products = productService.getProductsBySeller(currentUser.getUserId());
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product p : products) {
                System.out.printf("ID: %d | Name: %s | Price: %.2f | Stock: %d\n", 
                        p.getProductId(), p.getName(), p.getPrice(), p.getStockQuantity());
            }
        }
    }
    
    private void updateProduct() {

        viewMyProducts();

        System.out.print("Enter Product ID to update: ");
        int productId = readInt();

        System.out.print("New Name: ");
        String name = scanner.nextLine();

        System.out.print("New Description: ");
        String desc = scanner.nextLine();

        System.out.print("New Price: ");
        double price = readDouble();

        System.out.print("New MRP: ");
        double mrp = readDouble();

        System.out.print("New Stock Quantity: ");
        int stock = readInt();

        Product p = new Product();
        p.setProductId(productId);
        p.setSellerId(currentUser.getUserId());
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setMrp(mrp);
        p.setStockQuantity(stock);

        if (productService.updateProduct(p)) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Update failed. Check product ID.");
        }
    }

    private void deleteProduct() {

        viewMyProducts();

        System.out.print("Enter Product ID to delete: ");
        int productId = readInt();

        boolean success =
            productService.deleteProduct(productId, currentUser.getUserId());

        if (success) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Delete failed. Check product ID.");
        }
    }

    
    private void viewSoldItems() {

        int sellerId = loggedInUser.getUserId();

        List<SoldItem> list = orderService.getSoldItems(sellerId);

        if (list.isEmpty()) {
            System.out.println("No orders found for your products.");
            return;
        }

        System.out.println("\n--- Sold Items ---");

        for (SoldItem s : list) {
        	System.out.println(
        		    "OrderItemID: " + s.getOrderItemId()
        		    + " | OrderID: " + s.getOrderId()
        		    + " | ProductID: " + s.getProductId()
        		    + " | Product: " + s.getProductName()
        		    + " | Qty: " + s.getQuantity()
        		    + " | Price: " + s.getPrice()
        		    + " | Status: " + s.getStatus()
        		);

        }
    }
    
    
    private void acceptOrderItem() {

        System.out.print("Enter Order Item ID: ");
        int orderItemId = readInt();

        boolean success = orderService.acceptOrderItem(orderItemId);

        if (success) {
            System.out.println("Order item accepted successfully.");
        } else {
            System.out.println("Failed to accept order item.");
        }
    }




    private void showBuyerMenu() {
        while (true) {   //  add loop so it returns to menu after each action
            System.out.println("\n--- Buyer Dashboard ---");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Products");
            System.out.println("3. View Cart");
            System.out.println("4. View Order History");
            System.out.println("5. Add Product to Favorites");
            System.out.println("6. Remove Product from Favorites");
            System.out.println("7. View My Favorites");
            System.out.println("8. View Notifications");
            System.out.println("9. Logout");

            System.out.print("Enter choice: ");

            int choice = readInt();   // assuming this is your safe integer input method

            switch (choice) {
                case 1:
                    browseProducts();
                    break;
                case 2:
                    searchProducts();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    viewOrderHistory();
                    break;
                case 5:
                    addToFavorites();
                    break;
                case 6:
                    removeFromFavorites();
                    break;
                case 7:
                    viewFavorites();
                    break;
                case 8: 
                	viewNotifications(); 
                	break;

                case 9:
                    logout();
                    return;           //  exit the loop and menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void browseProducts() {
        List<Product> products = productService.getAllProducts();
        displayProductList(products);
    }
    
    private void searchProducts() {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        List<Product> products = productService.searchProducts(keyword);
        displayProductList(products);
    }
    
    private void displayProductList(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("\n--- Products ---");
        for (Product p : products) {
            System.out.printf("ID: %d | %s | %.2f | Stock: %d\n", 
                    p.getProductId(), p.getName(), p.getPrice(), p.getStockQuantity());
        }
        System.out.println("----------------");
        System.out.println("Enter Product ID to view details/add to cart, or 0 to go back:");
        int pid = readInt();
        if (pid > 0) {
            Product p = productService.getProductById(pid);
            if (p != null) {
                showProductDetails(p);
            }
        }
    }
    
    private void showProductDetails(Product p) {
        System.out.println("\n--- Product Details ---");
        System.out.println("Name: " + p.getName());
        System.out.println("Description: " + p.getDescription());
        System.out.println("Price: " + p.getPrice());
        System.out.println("MRP: " + p.getMrp());
        System.out.println("Stock: " + p.getStockQuantity());
        
        // Show Reviews
        List<Review> reviews = reviewService.getReviewsForProduct(p.getProductId());
        if (!reviews.isEmpty()) {
            System.out.println("Reviews:");
            for (Review r : reviews) {
                System.out.println("- " + r.getRating() + "/5: " + r.getReviewText());
            }
        }
        
        System.out.println("\n1. Add to Cart");
        System.out.println("2. Write Review"); // Only if purchased? For now allow any buyer
        System.out.println("3. Back");
        int subChoice = readInt();
        if (subChoice == 1) {
            System.out.print("Enter Quantity: ");
            int qty = readInt();
            if (orderService.addToCart(currentUser.getUserId(), p.getProductId(), qty)) {
                System.out.println("Added to cart!");
            } else {
                System.out.println("Failed to add to cart (Check stock).");
            }
        } else if (subChoice == 2) {
             System.out.print("Rating (1-5): ");
             int rating = readInt();
             System.out.print("Review: ");
             String text = scanner.nextLine();
             Review r = new Review();
             r.setBuyerId(currentUser.getUserId());
             r.setProductId(p.getProductId());
             r.setRating(rating);
             r.setReviewText(text);
             if (reviewService.addReview(r)) {
                 System.out.println("Review added!");
             }
        }
    }
    
    private void viewCart() {
        System.out.println("\n--- Your Cart ---");
        List<CartItem> items = orderService.getCartItems(currentUser.getUserId());
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = 0;
        for (CartItem item : items) {
            Product p = productService.getProductById(item.getProductId());
            double subtotal = p.getPrice() * item.getQuantity();
            total += subtotal;
            System.out.printf("[%d] %s x %d = %.2f\n", 
                    p.getProductId(), p.getName(), item.getQuantity(), subtotal);
        }
        System.out.printf("Total: %.2f\n", total);
        
        System.out.println("1. Checkout");
        System.out.println("2. Go Back");
        int choice = readInt();
        if (choice == 1) {
            System.out.print("Enter Shipping Address: ");
            String address = scanner.nextLine();
            System.out.print("Payment Method (Card/UPI/COD): ");
            String payment = scanner.nextLine();
            
            if (orderService.placeOrder(currentUser.getUserId(), address, payment)) {
                System.out.println("Order Placed Successfully!");
            } else {
                System.out.println("Order failed. Stock might have run out.");
            }
        }
    }
    
    private void viewOrderHistory() {
         System.out.println("\n--- Order History ---");
         List<Order> orders = orderService.getOrderHistory(currentUser.getUserId());
         if (orders.isEmpty()) {
             System.out.println("No orders found.");
         } else {
             for (Order o : orders) {
                 System.out.printf("Order #%d | Date: %s | Status: %s | Total: %.2f\n", 
                         o.getOrderId(), o.getOrderDate(), o.getOrderStatus(), o.getTotalAmount());
             }
         }
    }
    
    private void addToFavorites() {
        System.out.print("Enter Product ID to add to favorites: ");
        int productId = readInt();  // your safe int input method

        if (productId <= 0) {
            System.out.println("Invalid Product ID.");
            return;
        }

        int userId = currentUser.getUserId();
        // Optional debug (remove later)
        // System.out.println("DEBUG: Trying to add favorite for user " + userId + ", product " + productId);

        // Step 1: Check if already exists (fast & clean)
        if (favoriteService.isProductInFavorites(userId, productId)) {
            System.out.println("This product is already in your favorites.");
            return;
        }

        

        // Step 2: Attempt to add
        boolean added = favoriteService.addToFavorites(userId, productId);

        if (added) {
            System.out.println("Product added to your favorites successfully!");
        } else {
            System.out.println("Failed to add product. Please try again (check if product exists).");
        }
    }

    private void removeFromFavorites() {
        System.out.print("Enter Product ID to remove from favorites: ");
        int productId = readInt();

        if (productId <= 0) {
            System.out.println("Invalid product ID.");
            return;
        }

        boolean success = favoriteService.removeFromFavorites(currentUser.getUserId(), productId);

        if (success) {
            System.out.println("Product removed from your favorites.");
        } else {
            System.out.println("Product was not found in your favorites.");
        }
    }

    private void viewFavorites() {
        List<Favorite> favorites = favoriteService.getUserFavorites(currentUser.getUserId());

        if (favorites.isEmpty()) {
            System.out.println("You have no items in your favorites yet.");
            return;
        }

        System.out.println("\n--- My Favorites ---");
            System.out.println("-----------------------------------------------------");

        for (Favorite fav : favorites) {
            // If you added product name to Favorite model or joined in DAO, use it
            // For now we show basic info – improve later
            System.out.printf("%-12d %-10d %-20s %-15s%n",
                fav.getFavoriteId(),
                fav.getProductId(),
                "(name not loaded yet)",   //  improve this later
                fav.getAddedAt() != null ? fav.getAddedAt().toString().substring(0, 19) : "N/A");
        }
    }
    
    private void viewNotifications() {

        NotificationService notificationService =
            new NotificationService();

        List<Notification> list =
            notificationService.getNotifications(currentUser.getUserId());

        if (list.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        System.out.println("\n--- Notifications ---");

        for (Notification n : list) {
            System.out.println(
                n.getCreatedAt() + " - " + n.getMessage()
            );
        }
    }


    private void logout() {
        currentUser = null;
        loggedInUser = null;
        System.out.println("Logged out successfully.");
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    
    private double readDouble() {
        try {
            double d = Double.parseDouble(scanner.nextLine());
            return d;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    //valid email
    private boolean isValidEmail(String email) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email != null && email.matches(emailRegex);
    }
    
    private boolean isValidPassword(String password) {
        String passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }


}

