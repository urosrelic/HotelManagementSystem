package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class UserConfigController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<User, String> colAddress;

    @FXML
    private TableColumn<User, String> colAnswer;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, Integer> colID;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colPassword;

    @FXML
    private TableColumn<User, String> colSecurityQuestion;

    @FXML
    private TableColumn<User, Integer> colStatus;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<User> table;

    @FXML
    private AnchorPane userConfig;

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnClose) {
            userConfig.setVisible(false);
        }

    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        User user = table.getSelectionModel().getSelectedItem();
        int status = user.getStatusCode();
        String email = user.getEmail();

        if(status == 0) {
            status = 1;
        } else {
            status = 0;
        }

        try {
            String query = "update users set status='" + status + "' where email='" + email + "'";
            DataController.executeQuery(query);
            table.setItems(getAllUsers());
            new Alert(Alert.AlertType.INFORMATION, "Changed Successfully.").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        table.setItems(getAllUsers());
        setTextFieldMaxLength();
        implementSearch();

    }

    private void setTextFieldMaxLength() {
        int max = 50;
        UnaryOperator<TextFormatter.Change> rejectChange = c -> {
            // check if the change might effect the validating predicate
            if (c.isContentChange()) {
                // check if change is valid
                if (c.getControlNewText().length() > max) {
                    // invalid change
                    // sugar: show a context menu with error message
                    final ContextMenu menu = new ContextMenu();
                    menu.getItems().add(new MenuItem("This field takes\n"+max+" characters only."));
                    menu.show(c.getControl(), Side.BOTTOM, 0, 0);
                    // return null to reject the change
                    return null;
                }
            }
            // valid change: accept the change by returning it
            return c;
        };

        textFieldSearch.setTextFormatter(new TextFormatter<String>(rejectChange));
    }

    private void implementSearch() {
        FilteredList<User> filteredList = new FilteredList<>(getAllUsers(), u -> true);
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                // If filter is empty display all values
                if(newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter
                String searchKeyword = newValue.toLowerCase();

                if(user.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // Filter matches name
                } else if(user.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // Filter matches email
                } else if(String.valueOf(user.getStatusCode()).indexOf(searchKeyword) > -1){
                    return true;
                } else if(String.valueOf(user.getAddress()).indexOf(searchKeyword) > -1){
                    return true;
                } else if(String.valueOf(user.getSecurity_question()).indexOf(searchKeyword) > -1){
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<User> sortedUsers = new SortedList<>(filteredList);
        sortedUsers.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedUsers);
    }

    private void setCellValues() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colSecurityQuestion.setCellValueFactory(new PropertyValueFactory<>("security_question"));
        colAnswer.setCellValueFactory(new PropertyValueFactory<>("answer"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusCode"));
    }

    private ObservableList<User> getAllUsers() {
        ObservableList<User> tmp = FXCollections.observableArrayList();
        String query = "SELECT * FROM users";

        try {
            ResultSet rs = DataController.getData(query);

            while(rs.next()) {
                User user = new User(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("security_question"),
                        rs.getString("answer"),
                        rs.getString("address"),
                        rs.getInt("status"));
                tmp.add(user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tmp;
    }


}
