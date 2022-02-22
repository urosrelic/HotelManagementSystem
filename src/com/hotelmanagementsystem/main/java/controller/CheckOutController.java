package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.Customer;
import com.hotelmanagementsystem.main.java.objects.UserSession;
import com.itextpdf.text.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class CheckOutController extends Controller implements Initializable {

    @FXML
    private Button btnCheckOut;

    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colBed;

    @FXML
    private TableColumn<Customer, String> colCheckInDate;

    @FXML
    private TableColumn<Customer, Integer> colMobileNum;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colGender;

    @FXML
    private TableColumn<Customer, Integer> colID;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, Double> colPrice;

    @FXML
    private TableColumn<Customer, Integer> colRoomNumber;

    @FXML
    private TableColumn<Customer, String> colRoomType;

    @FXML
    private TableView<Customer> table;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private TextField textFieldBed;

    @FXML
    private TextField textFieldCheckInDate;

    @FXML
    private TextField textFieldCheckOutDate;

    @FXML
    private TextField textFieldDaysStaying;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldGender;

    @FXML
    private TextField textFieldMobileNum;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPrice;

    @FXML
    private TextField textFieldRoomNumber;

    @FXML
    private TextField textFieldRoomType;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TextField textFieldTotal;

    @FXML
    private AnchorPane checkOutPane;

    private ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    @Override
    void setLoginInfo(String email) {
        textFieldEmail.setText(UserSession.getInstace(email).getEmail());

        String name = "";
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

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnClose) {
            checkOutPane.setVisible(false);
        }

        if(event.getSource() == btnCheckOut) {
            Document doc = new Document();
            try {
                String roomNum = textFieldRoomNumber.getText();
                String daysStaying = textFieldDaysStaying.getText();
                double total = Double.parseDouble(textFieldTotal.getText());
                String checkOutDate = textFieldCheckOutDate.getText();

                String updateCustomersQuery = "update hotel.customers set days_staying='"
                        + daysStaying + "',total_amount='"
                        + String.format("%.2f", total) + "',check_out_date='"
                        + checkOutDate + "',paid='Not paid' where room_number='"
                        + roomNum + "'";
                DataController.executeQuery(updateCustomersQuery);

                String updateRoomsQuery = "update hotel.rooms set status='" + 0 + "' where room_num='" + roomNum + "'";
                DataController.executeQuery(updateRoomsQuery);

                new Alert(Alert.AlertType.INFORMATION, "Successfully checked out.").showAndWait();
                table.setItems(getCustomerInfo());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
                e.printStackTrace();
            }
            doc.close();
        }
    }


    @FXML
    public void onMouseClicked(MouseEvent mouseEvent) {
        try {
            Customer customer = table.getSelectionModel().getSelectedItem();
            textFieldMobileNum.setText(customer.getMobileNum());
            textFieldAddress.setText(customer.getAddress());
            textFieldCheckInDate.setText(customer.getCheckInDate());
            textFieldRoomNumber.setText(String.valueOf(customer.getRoomNum()));
            textFieldRoomType.setText(customer.getRoomType());
            textFieldBed.setText(customer.getBed());
            textFieldPrice.setText(String.valueOf(String.format("%.2f", customer.getPricePerDay())));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String checkInDateString = textFieldCheckInDate.getText();
            Date checkInDate = simpleDateFormat.parse(checkInDateString);

            String checkOutDateString = textFieldCheckOutDate.getText();
            Date checkOutDate = simpleDateFormat.parse(checkOutDateString);

            long differenceInTime = checkOutDate.getTime() - checkInDate.getTime();
            long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24) % 365);

            if(differenceInDays == 0) {
                differenceInDays = 1;
            }

            textFieldDaysStaying.setText(String.valueOf(differenceInDays));

            double price = Double.parseDouble(textFieldPrice.getText());
            textFieldTotal.setText(String.format("%.2f", differenceInDays * price));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValues();
        customerObservableList = getCustomerInfo();
        table.setItems(customerObservableList);
        setLoginInfo(textFieldEmail.getText());


        implementSearch();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        textFieldCheckOutDate.setText(simpleDateFormat.format(calendar.getTime()));

        disableTextFields();
    }

    private void disableTextFields() {
        textFieldName.setEditable(false);
        textFieldEmail.setEditable(false);
        textFieldMobileNum.setEditable(false);
        textFieldGender.setEditable(false);
        textFieldCheckInDate.setEditable(false);
        textFieldRoomNumber.setEditable(false);
        textFieldRoomType.setEditable(false);
        textFieldBed.setEditable(false);
        textFieldPrice.setEditable(false);
        textFieldName.setEditable(false);
        textFieldDaysStaying.setEditable(false);
        textFieldTotal.setEditable(false);
        textFieldCheckOutDate.setEditable(false);
    }

    private void setCellValues() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMobileNum.setCellValueFactory(new PropertyValueFactory<>("mobileNum"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCheckInDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colBed.setCellValueFactory(new PropertyValueFactory<>("bed"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

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

    public ObservableList<Customer> getCustomerInfo() {
        ObservableList<Customer> tmp = FXCollections.observableArrayList();
        try {
            String currentUser = UserSession.getInstace(textFieldEmail.getText()).getEmail();
            String query = "select * from customers where email='" + currentUser + "' and check_out_date is NULL";
            ResultSet rs = DataController.getData(query);

            while(rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mobile_number"),
                        rs.getString("address"),
                        rs.getString("check_in_date"),
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        rs.getString("bed"),
                        rs.getDouble("price_per_day"), 0, 0, null, null);
                tmp.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    private void implementSearch() {
        FilteredList<Customer> filteredList = new FilteredList<>(getCustomerInfo(), u -> true);
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                // If filter is empty display all values
                if(newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter
                String searchKeyword = newValue.toLowerCase();

                if(customer.getRoomType().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if(String.valueOf(String.format("%.2f", customer.getPricePerDay())).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if(String.valueOf(customer.getRoomNum()).indexOf(searchKeyword) > -1){
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Customer> sortedCustomerInfoList = new SortedList<>(filteredList);
        sortedCustomerInfoList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedCustomerInfoList);
    }
}
