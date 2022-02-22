package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.Room;
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

public class ManageRoomController implements Initializable {

    @FXML
    private Button btnAddRoom;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDeleteRoom;

    @FXML
    private Button btnEditRoom;

    @FXML
    private TableColumn<Room, String> colBed;

    @FXML
    private TableColumn<Room, Double> colPrice;

    @FXML
    private TableColumn<Room, Integer> colRoomNumber;

    @FXML
    private TableColumn<Room, String> colRoomType;

    @FXML
    private TableColumn<Room, Integer> colStatus;

    @FXML
    private TableView<Room> roomTable;

    private final ObservableList<String> roomOptions = FXCollections.observableArrayList(
            "Single room","Double room", "Triple room", "Executive Suite", "Mini Suite", "Apartment", "Villa");

    private final ObservableList<String> bedOptions = FXCollections.observableArrayList(
            "Single bed","King bed","Queen bed","Sofa bed");

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private ComboBox<String> bedComboBox;

    @FXML
    private TextField textFieldPrice;

    @FXML
    private TextField textFieldRoomNum;

    @FXML
    private AnchorPane manageRoomPane;

    @FXML
    private TextField textFieldSearch;

    private ObservableList<Room> rooms = FXCollections.observableArrayList();

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnClose) {
            manageRoomPane.setVisible(false);
        }

        if(event.getSource() == btnAddRoom) {
            String room_num = textFieldRoomNum.getText();
            String room_type = roomTypeComboBox.getSelectionModel().getSelectedItem();
            String bed = bedComboBox.getSelectionModel().getSelectedItem();
            double price = Double.parseDouble(textFieldPrice.getText());
            if(room_num.equals("") || roomTypeComboBox.getSelectionModel().getSelectedItem() == null || bedComboBox.getSelectionModel().getSelectedItem() == null || String.valueOf(price).equals("")) {
                new Alert(Alert.AlertType.ERROR, "Fields cannot be null").showAndWait();
            } else {
                String query = "insert into rooms values('" + room_num + "','"
                        + room_type +"','" + bed + "','" + String.format("%.2f", price) + "','0')";
                DataController.executeQuery(query);
                setCellValues();
                new Alert(Alert.AlertType.INFORMATION, "Room added").showAndWait();
            }
        }

        if(event.getSource() == btnDeleteRoom) {
            String roomNum = textFieldRoomNum.getText();
            try {
                String query = "delete from hotel.rooms where room_num='" + roomNum + "'";
                DataController.executeQuery(query);
                new Alert(Alert.AlertType.INFORMATION, "Successfully deleted.").showAndWait();
                setCellValues();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }

        if(event.getSource() == btnEditRoom) {
            String roomNum = textFieldRoomNum.getText();
            String roomType = roomTypeComboBox.getSelectionModel().getSelectedItem();
            String bed = bedComboBox.getSelectionModel().getSelectedItem();
            double price = Double.parseDouble(textFieldPrice.getText());


            try {
                String query = "update hotel.rooms set room_type='" +
                        roomType + "',bed='" + bed + "',price='" + String.format("%.2f", price) + "' where room_num='" + roomNum + "'";
                DataController.executeQuery(query);
                setCellValues();
                new Alert(Alert.AlertType.INFORMATION, "Successfully updated.").showAndWait();

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error:" + e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        try {
            Room room = roomTable.getSelectionModel().getSelectedItem();

            int roomNum = room.getRoomNum();

            String roomType = room.getRoomType();
            String bed = room.getBed();
            double price = room.getPrice();

            textFieldRoomNum.setText(String.valueOf(roomNum));
            bedComboBox.setValue(bed);
            roomTypeComboBox.setValue(roomType);
            textFieldPrice.setText(String.valueOf(price));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        rooms = getRoomsList();
        roomTable.setItems(rooms);

        implementSearch();
        setTextFieldMaxLength();


        roomTypeComboBox.setItems(roomOptions);
        bedComboBox.setItems(bedOptions);

    }

    private ObservableList<Room> getRoomsList() {
        ObservableList<Room> tmp = FXCollections.observableArrayList();
        String query = "SELECT * FROM rooms";

        try {
            ResultSet rs = DataController.getData(query);

            while(rs.next()) {
                Room room = new Room(rs.getInt("room_num"),
                        rs.getString("room_type"),
                        rs.getString("bed"),
                        rs.getDouble("price"),
                        rs.getInt("status"));
                tmp.add(room);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tmp;
    }

    private void setCellValues() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colBed.setCellValueFactory(new PropertyValueFactory<>("bed"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colPrice.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Double balance, boolean empty) {
                super.updateItem(balance, empty);
                if (balance == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", balance));
                }
            }
        });
    }

    private void setTextFieldMaxLength() {
        int max = 15;
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
        FilteredList<Room> filteredList = new FilteredList<>(rooms, u -> true);
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(room -> {
                if(newValue.isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(String.valueOf(room.getRoomNum()).indexOf(searchKeyword) > -1) {
                    return true;
                } else if(String.valueOf(String.format("%.2f", room.getPrice())).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if(room.getRoomType().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Room> sortedRoomsList = new SortedList<>(filteredList);
        sortedRoomsList.comparatorProperty().bind(roomTable.comparatorProperty());
        roomTable.setItems(sortedRoomsList);
    }


}
