package com.hotelmanagementsystem.main.java.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private Button btnExit;

    @FXML
    private Button btnForgotPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private AnchorPane signupPane;

    @FXML
    private PasswordField passwordField;

    private final ObservableList<String> options = FXCollections.observableArrayList(
               "What is the name of your first pet?", "What was your first car?",
                    "What elementary school did you attend?",
                    "What is the name of the town where you were born?");
    @FXML
    private ComboBox<String> questionComboBox;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private TextField textFieldAnswer;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldName;

    @FXML
    public void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnSignup) {
            String name = textFieldName.getText();
            String email = textFieldEmail.getText();
            String password = passwordField.getText();
            String secQuestion = questionComboBox.getSelectionModel().getSelectedItem();
            String answer = textFieldAnswer.getText();
            String address = textFieldAddress.getText();

            if(name.equals("") || email.equals("") || password.equals("") || secQuestion.equals("") || answer.equals("")
                    || address.equals("")) {
                new Alert(Alert.AlertType.ERROR, "Fields cannot be null").showAndWait();
            } else {
                String query = "INSERT INTO users(name, email, password, security_question, answer, address, status) " +
                        "VALUES('"
                        + name +"','"
                        + email + "','"
                        + password + "','"
                        + secQuestion + "','"
                        + answer + "','"
                        + address + "','" + 0 + "')";
                DataController.executeQuery(query);
                new Alert(Alert.AlertType.INFORMATION, "User registered").showAndWait();
            }
        }
        if(event.getSource() == btnLogin) {
            SceneController.changeScene("login.fxml", btnLogin);
        }

        if(event.getSource() == btnForgotPassword) {
            SceneController.changeScene("forgotPass.fxml", btnForgotPassword);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionComboBox.setItems(options);
    }



}

