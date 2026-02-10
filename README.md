# RevShop – Console Based E-Commerce Application

RevShop is a secure, console-based e-commerce application developed using Core Java and JDBC with Oracle 11g XE as the backend database.  
It supports both **Buyer** and **Seller** workflows and follows a layered, modular architecture to simulate real-world e-commerce operations.

---

## 📌 Project Overview

RevShop allows users to register and log in as either a buyer or a seller.  
Buyers can browse products, manage carts, place and cancel orders, write reviews, and receive notifications.  
Sellers can manage products, monitor stock levels, and track orders related to their products.

The application focuses on clean separation of concerns using service, DAO, and UI layers.

---

## 👤 Buyer Features

- Register and Login  
- Forgot Password using Security Question and Answer  
- View all available products  
- Search products by keyword  
- Add products to cart  
- Remove products from cart  
- Checkout with simulated payment methods  
  - Cash on Delivery (COD)  
  - UPI  
  - Card  
- View order history  
- Cancel orders  
- Add product reviews and ratings  
- Add products to favourites  
- View favourite products  
- Receive in-app notifications  

---

## 🏪 Seller Features

- Register and Login as Seller  
- Add new products  
- Update product price and stock  
- Delete products  
- Set MRP and discounted price  
- View orders for their own products  
- Low stock alerts using threshold values  
- View product reviews and ratings  

---

## 🛠️ Technologies Used

- Language: Java (Core Java)
- Database: Oracle 11g XE
- Connectivity: JDBC
- IDE: Eclipse
- Architecture: Layered / Modular Architecture
- Application Type: Console-based application

---

## 🧱 Project Architecture

The project follows a layered architecture:

- UI Layer – Handles console input and output
- Service Layer – Contains business logic
- DAO Layer – Handles database operations using JDBC
- Model Layer – Represents application entities

---

## 🗄️ Database

- Oracle 11g XE is used as the backend database.
- All application data such as users, products, carts, orders, reviews and notifications are stored in relational tables.

---

## ▶️ How to Run the Project

1. Open the project in Eclipse.
2. Configure the Oracle JDBC driver in your project.
3. Update database connection details in your JDBC utility / connection class.
4. Make sure Oracle 11g XE is running.
5. Run the main console UI class to start the application.

---

## 🔐 Authentication & Security

- Role-based login (Buyer / Seller)
- Forgot password feature using security question and answer
- Basic input validations for user data

---


