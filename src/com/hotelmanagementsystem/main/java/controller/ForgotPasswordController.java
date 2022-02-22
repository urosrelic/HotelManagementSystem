package  com.hotelmanagementsystem.main.java.controller;

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

public class ForgotPasswordController implements Initializable {

    @FXML
    private AnchorPane forgotPassPane;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnUpdate;

    private final ObservableList<String> options = FXCollections.observableArrayList(
            "What is the name of your first pet?", "What was your first car?",
            "What elementary school did you attend?",
            "What is the name of the town where you were born?");

    @FXML
    private ComboBox<String> questionComboBox;

    @FXML
    private TextField textFieldAnswer;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private PasswordField textFieldNewPass;

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnUpdate) {
            int check = 0;

            String email = textFieldEmail.getText();
            String comboBox = questionComboBox.getSelectionModel().getSelectedItem();
            String answer = textFieldAnswer.getText();
            String newPass = textFieldNewPass.getText();
            if(answer.equals("") || newPass.equals("") || comboBox.equals("")) {
                check = 1;
                new Alert(Alert.AlertType.ERROR, "Fields cannot be null").showAndWait();
            } else {
                String query = "select * from users where email='" + email
                        + "' and security_question='" + comboBox + "' and answer='" + answer + "'";
                ResultSet rs = DataController.getData(query);
                try {
                    String updateQuery = "update users set password='" + newPass + "' where email='" + email + "'";

                    if(rs.next()) {
                        check = 1;
                        DataController.executeQuery(updateQuery);
                        new Alert(Alert.AlertType.INFORMATION, "Successfully changed the password").showAndWait();
                        SceneController.changeScene("login.fxml", btnLogin);
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
                }
            }

            if(check == 0) {
                new Alert(Alert.AlertType.ERROR, "Wrong information, check your security question or email")
                        .showAndWait();
            }
        }

        if(event.getSource() == btnLogin) {
            SceneController.changeScene("login.fxml", btnLogin);
        }

        if(event.getSource() == btnSignup) {
            SceneController.changeScene("signup.fxml", btnSignup);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionComboBox.setItems(options);
    }

}
