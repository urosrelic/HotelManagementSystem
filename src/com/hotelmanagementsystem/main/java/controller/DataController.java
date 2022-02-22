package com.hotelmanagementsystem.main.java.controller;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataController {
    public static void executeQuery(String query) {
        try {
            Connection conn = ConnectionController.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
        }
    }

    public static ResultSet getData(String query) {
        try {
            Connection conn = ConnectionController.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
            return null;
        }
    }
}
