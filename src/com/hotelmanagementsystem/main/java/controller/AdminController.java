package com.hotelmanagementsystem.main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AdminController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnManageRoom;

    @FXML
    private Button btnManageUser;

    @FXML
    private Pane contentPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnManageRoom) {
            SceneController.changeToNewScene(contentPane, "manageRoom.fxml");
        }

        if(event.getSource() == btnManageUser) {
            SceneController.changeToNewScene(contentPane, "userConfig.fxml");
        }

        if(event.getSource() == btnLogout) {
            SceneController.changeScene("login.fxml", btnLogout);
        }

    }

}
