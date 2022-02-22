package com.hotelmanagementsystem.main.java.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;

public class SceneController {
    private static final String PATH = "com/hotelmanagementsystem/main/resources/fxml/";

    public static void changeScene(String fileName, Button button) {
        try {
            URL url = SceneController.class.getClassLoader()
                    .getResource(PATH + fileName);
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    public static void changeToNewScene(Pane pane, String fileName) {
        try {
            Pane newPane = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getClassLoader().
                    getResource(PATH + fileName)));

            pane.getChildren().setAll(newPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

}
