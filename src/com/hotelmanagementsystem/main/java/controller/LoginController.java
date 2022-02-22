package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;

public class LoginController {

    @FXML
    private Button btnExit;

    @FXML
    private Button btnForgotPass;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private PasswordField textFieldPassword;


    @FXML
    public void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnLogin) {
            int check = -1;
            String email = textFieldEmail.getText();
            String password = textFieldPassword.getText();

            if(email.equals("") || password.equals("")) {
                check = 1;
                new Alert(Alert.AlertType.ERROR, "Fields cannot be null").showAndWait();
            } else if(email.equals("hms") && password.equals("admin")){
                check = 2;
                SceneController.changeScene("adminPanel.fxml", btnSignup);
            } else {
                String query = "select * from users where email='" + email + "' and password = '" + password + "' ";
                ResultSet rs = DataController.getData(query);
                try {
                    if(rs.next()) {
                        check = 1;
                        if(rs.getInt(8) == 1) {
                            new Alert(Alert.AlertType.INFORMATION, "Logged in").showAndWait();
                            UserSession.getInstace(email);
                            SceneController.changeScene("home.fxml", btnLogin);
                        } else {
                            new Alert(Alert.AlertType.INFORMATION, "Wait for admin approval").showAndWait();
                        }
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
                }
            }

            if(check == -1) {
                new Alert(Alert.AlertType.ERROR, "Incorrect email or password").showAndWait();
            }
        }

        if(event.getSource() == btnSignup) {
            SceneController.changeScene("signup.fxml", btnSignup);
        }

        if(event.getSource() == btnForgotPass) {
            SceneController.changeScene("forgotPass.fxml", btnForgotPass);
        }
    }

    private void setEmailToControllers() {

        CheckInController checkInController = new CheckInController();
        CheckOutController checkOutController = new CheckOutController();
    }
}
