package com.hotelmanagementsystem.main.java.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionController {

    public static Connection getConnection() {
        try {
            // If you are using XAMPP, the connection is usually localhost:3306
            return DriverManager.getConnection("jdbc:mysql://yourconnection/hotel",
                    "your username", "your password");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
