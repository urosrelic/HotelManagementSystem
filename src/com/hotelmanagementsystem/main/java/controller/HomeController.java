package com.hotelmanagementsystem.main.java.controller;
import com.hotelmanagementsystem.main.java.objects.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class HomeController extends Controller implements Initializable {
    @FXML
    private Button btnBill;

    @FXML
    private Button btnCheckIn;

    @FXML
    private Button btnCheckOut;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnManageAccount;

    @FXML
    private AnchorPane content;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private Label labelWelcome;

    public TextField getTextFieldEmail() {
        return textFieldEmail;
    }

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnManageAccount) {
            SceneController.changeToNewScene(content, "manageAccount.fxml");
        }

        if(event.getSource() == btnCheckIn) {
            SceneController.changeToNewScene(content, "checkIn.fxml");

        }

        if(event.getSource() == btnCheckOut) {
            SceneController.changeToNewScene(content, "checkOut.fxml");
        }

        if(event.getSource() == btnBill) {
            SceneController.changeToNewScene(content, "bill.fxml");
        }

        if(event.getSource() == btnLogout) {
            SceneController.changeScene("login.fxml", btnLogout);
            UserSession.getInstace(getTextFieldEmail().getText()).cleanUserSession();
        }
    }

    @Override
    void setLoginInfo(String email) {
        textFieldEmail.setText(UserSession.getInstace(email).getEmail());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLoginInfo(textFieldEmail.getText());
    }
}
