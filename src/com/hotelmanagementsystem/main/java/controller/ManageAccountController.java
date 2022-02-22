package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ManageAccountController extends Controller implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> comboBoxSecurityQuestion;

    @FXML
    private AnchorPane manageAccPane;

    @FXML
    private TextField textFieldAnswer;

    @FXML
    private PasswordField textFieldConfirmPass;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private PasswordField textFIeldNewPass;

    @FXML
    private PasswordField textFieldOldPass;

    private final ObservableList<String> options = FXCollections.observableArrayList(
            "What is the name of your first pet?", "What was your first car?",
            "What elementary school did you attend?",
            "What is the name of the town where you were born?");

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnSave) {
            int check = 0;
            String email = textFieldEmail.getText();
            String oldPass = textFieldOldPass.getText();
            String newPass = textFIeldNewPass.getText();
            String confirmPass = textFieldConfirmPass.getText();
            String securityQuestion = comboBoxSecurityQuestion.getSelectionModel().getSelectedItem();
            String answer = textFieldAnswer.getText();

            if(email.equals("") || oldPass.equals("") || newPass.equals("")
                    || confirmPass.equals("") || securityQuestion.equals("") || answer.equals("")) {
                check = 1;
                new Alert(Alert.AlertType.ERROR, "Fields cannot be null").showAndWait();
            } else if(!newPass.equals(confirmPass)) {
                check = 1;
                new Alert(Alert.AlertType.ERROR, "Passwords do not match").showAndWait();
            } else {
                String query = "select * from users where email='" + email
                        + "' and security_question='" + securityQuestion + "' and answer='" + answer + "' and password='" + oldPass + "'";
                ResultSet rs = DataController.getData(query);
                try {
                    String updateQuery = "update users set password='" + newPass + "' where email='" + email + "'";

                    if(rs.next()) {
                        check = 1;
                        DataController.executeQuery(updateQuery);
                        new Alert(Alert.AlertType.INFORMATION, "Successfully changed the password, please login again").showAndWait();
                        SceneController.changeScene("login.fxml", btnSave);
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            }

            if(check == 0) {
                new Alert(Alert.AlertType.ERROR, "Wrong information, check your security question or email")
                        .showAndWait();
            }
        }

        if(event.getSource() == btnClose) {
            manageAccPane.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxSecurityQuestion.setItems(options);
        setLoginInfo(textFieldEmail.getText());
    }


    @Override
    void setLoginInfo(String email) {
        textFieldEmail.setText(UserSession.getInstace(email).getEmail());
    }
}