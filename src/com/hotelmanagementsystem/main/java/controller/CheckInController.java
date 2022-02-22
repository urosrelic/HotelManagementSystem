package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.Room;
import com.hotelmanagementsystem.main.java.objects.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CheckInController extends Controller implements Initializable {

    @FXML
    private Button btnCheckIn;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnClearFirst;

    @FXML
    private Button btnClearSecond;

    @FXML
    private TextField textFieldDate;

    @FXML
    private AnchorPane checkInPane;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldNumber;

    @FXML
    private TextField textFieldRoomBed;

    @FXML
    private TextField textFieldRoomNum;

    @FXML
    private TextField textFieldRoomPrice;

    @FXML
    private TextField textFieldRoomType;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableColumn<Room, String> colBed;

    @FXML
    private TableColumn<Room, Integer> colPrice;

    @FXML
    private TableColumn<Room, Integer> colRoomNumber;

    @FXML
    private TableColumn<Room, String> colRoomType;

    @FXML
    private TableView<Room> table;

    private final ObservableList<Room> roomsList = FXCollections.observableArrayList();


    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnClose) {
            checkInPane.setVisible(false);
        }

        if(event.getSource() == btnCheckIn) {
            String name = textFieldName.getText();
            String email = textFieldEmail.getText();
            String mobileNum = textFieldNumber.getText();
            String address = textFieldAddress.getText();
            String roomNum = textFieldRoomNum.getText();
            String roomType = textFieldRoomType.getText();
            String bed = textFieldRoomBed.getText();
            String price = textFieldRoomPrice.getText();
            String date = textFieldDate.getText();

            if(name.equals("")
                    || email.equals("")
                    || mobileNum.equals("")
                    || address.equals("")
                    || roomType.equals("") || roomNum.equals("") || bed.equals("") || price.equals("") || date.equals("")){
                new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").showAndWait();
            } else {
                try {
                    String updateQuery = "update rooms set status='" + 1 + "' where room_num='" + roomNum + "'";
                    DataController.executeQuery(updateQuery);
                    String insertQuery = "insert into customers(name, email, " +
                            "mobile_number, address, " +
                            "check_in_date, room_number, " +
                            "room_type ,bed, price_per_day)" +
                            "values('" +
                            name + "','" +
                            email + "','" +
                            mobileNum + "','" +
                            address + "','" +
                            date + "','" +
                            roomNum + "','" +
                            roomType + "','" +
                            bed + "','" +
                            price + "')";
                    DataController.executeQuery(insertQuery);
                    new Alert(Alert.AlertType.INFORMATION, "Successfully booked.").showAndWait();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage());
                } finally {
                    refreshTable();
                }
            }
        }

        if(event.getSource() == btnClearFirst) {
            textFieldName.setText("");
            textFieldNumber.setText("");
            textFieldEmail.setText("");
            textFieldAddress.setText("");
        }

        if(event.getSource() == btnClearSecond) {
            textFieldRoomNum.setText("");
            textFieldRoomBed.setText("");
            textFieldRoomPrice.setText("");
            textFieldRoomType.setText("");
        }
    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        try {
            Room room = table.getSelectionModel().getSelectedItem();

            int roomNum = room.getRoomNum();
            String roomType = room.getRoomType();
            String bed = room.getBed();
            double price = room.getPrice();

            textFieldRoomNum.setText(String.valueOf(roomNum));
            textFieldRoomPrice.setText(String.valueOf(price));
            textFieldRoomBed.setText(bed);
            textFieldRoomType.setText(roomType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCellValues() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colBed.setCellValueFactory(new PropertyValueFactory<>("bed"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void implementSearch() {
        FilteredList<Room> filteredData = new FilteredList<>(roomsList, r -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(room -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(room.getRoomNum()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getRoomType()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getBed()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getStatus()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        SortedList<Room> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void refreshTable() {
        roomsList.clear();
        try {
            String query = "select * from hotel.rooms where status=0";
            ResultSet rs = DataController.getData(query);

            while(rs.next()) {
                Room room = new Room(rs.getInt("room_num"),
                        rs.getString("room_type"),
                        rs.getString("bed"),
                        rs.getInt("price"),
                        rs.getInt("status"));
                roomsList.add(room);
                table.setItems(roomsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        refreshTable();
        implementSearch();
        setLoginInfo(textFieldEmail.getText());



        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        textFieldDate.setText(format.format(calendar.getTime()));
        textFieldDate.setEditable(false);
        textFieldEmail.setEditable(false);
        textFieldName.setEditable(false);
    }


    @Override
    void setLoginInfo(String email) {
        String name = "";
        textFieldEmail.setText(UserSession.getInstace(email).getEmail());

        try {
            String query = "select * from users where email='" + textFieldEmail.getText() + "'";
            ResultSet rs = DataController.getData(query);

            while(rs.next()) {
                name = rs.getString("name");
            }
            textFieldName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
