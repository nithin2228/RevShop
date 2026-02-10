package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(User user) {

        // Validate password strength
        if (!isValidPassword(user.getPasswordHash())) {
            System.out.println(
                "Password must be at least 8 characters long and contain:\n" +
                "- One uppercase letter\n" +
                "- One lowercase letter\n" +
                "- One digit\n" +
                "- One special character"
            );
            return false;
        }

        // Hash password before saving
        String hashedPassword = hashPassword(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        return userDAO.registerUser(user);
    }


    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            String hashedPassword = hashPassword(password);
            if (hashedPassword.equals(user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    public boolean resetPassword(String email, String securityAnswer, String newPassword) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getSecurityAnswer().equalsIgnoreCase(securityAnswer)) {
            String newHashedPassword = hashPassword(newPassword);
            return userDAO.updatePassword(email, newHashedPassword);
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
           // e.printStackTrace();
        	System.out.println("Password hashing algorithm not available.");
            return password; // Fallback (should not happen)
        }
    }
    
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
    
    //pasword atlest 8 charcters
    private boolean isValidPassword(String password) {

        String passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        return password.matches(passwordRegex);
    }

}
