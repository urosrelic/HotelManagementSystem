package com.hotelmanagementsystem.main.java.controller;

import com.hotelmanagementsystem.main.java.objects.Customer;
import com.hotelmanagementsystem.main.java.objects.UserSession;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BillController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnPrintBill;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colBed;

    @FXML
    private TableColumn<Customer, String> colCheckInDate;

    @FXML
    private TableColumn<Customer, String> colCheckOut;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colDaysStaying;

    @FXML
    private TableColumn<Customer, Integer> colID;

    @FXML
    private TableColumn<Customer, String> colMobileNum;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colPaid;

    @FXML
    private TableColumn<Customer, Double> colPrice;

    @FXML
    private TableColumn<Customer, Integer> colRoomNumber;

    @FXML
    private TableColumn<Customer, String> colRoomType;

    @FXML
    private TableColumn<Customer, Double> colTotal;

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
    private TextField textFieldEmail;

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
    private TextField textFieldDaysStaying;

    @FXML
    private TextField textFieldID;

    @FXML
    private AnchorPane billPane;

    private ObservableList<Customer> customerHistoryList = FXCollections.observableArrayList();

    @FXML
    void onMouseClicked(MouseEvent event) {
        try {
            Customer customer = table.getSelectionModel().getSelectedItem();
            textFieldID.setText(String.valueOf(customer.getId()));
            textFieldName.setText(customer.getName());
            textFieldEmail.setText(customer.getEmail());
            textFieldMobileNum.setText(customer.getMobileNum());
            textFieldAddress.setText(customer.getAddress());
            textFieldCheckInDate.setText(customer.getCheckInDate());
            textFieldRoomNumber.setText(String.valueOf(customer.getRoomNum()));
            textFieldRoomType.setText(customer.getRoomType());
            textFieldBed.setText(customer.getBed());
            textFieldPrice.setText(String.valueOf(String.format("%.2f", customer.getPricePerDay())));
            textFieldCheckOutDate.setText(customer.getCheckOutDate());
            textFieldDaysStaying.setText(String.valueOf(customer.getDaysStaying()));
            textFieldTotal.setText(String.valueOf(String.format("%.2f", customer.getTotalAmount())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onButtonPress(ActionEvent event) {
        if(event.getSource() == btnClose) {
            billPane.setVisible(false);
        }

        if(event.getSource() == btnPrintBill) {
            Document doc = new Document();
            try {
                Customer customer = table.getSelectionModel().getSelectedItem();
                String daysStaying = textFieldDaysStaying.getText();
                double total = Double.parseDouble(textFieldTotal.getText());
                String checkOutDate = textFieldCheckOutDate.getText();

                String path = "E:\\HMS\\";
                PdfWriter.getInstance(doc, new FileOutputStream(path + " " + customer.getId() + ".pdf"));
                doc.open();
                Paragraph paragraph1 = new Paragraph("Hotel Management System App");
                doc.add(paragraph1);
                Paragraph paragraph2 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------");
                doc.add(paragraph2);
                Paragraph paragraph3 = new Paragraph("ID:" + customer.getId() +
                        "\nCustomer details:" +
                        "\nName: " + customer.getName() +
                        "\nEmail: " + customer.getEmail() +
                        "\nMobile number: " + customer.getMobileNum());
                doc.add(paragraph3);
                doc.add(paragraph2);
                Paragraph paragraph4 = new Paragraph("Room details:" +
                        "\nRoom number: " + customer.getRoomNum() +
                        "\nRoom type: " + customer.getRoomType() +
                        "\nBed: " + customer.getBed() +
                        "\nPrice: " + customer.getPricePerDay());
                doc.add(paragraph4);
                doc.add(paragraph2);
                PdfPTable table1 = new PdfPTable(5);
                table1.addCell("ID: " + customer.getId());
                table1.addCell("Check in date: " + customer.getCheckInDate());
                table1.addCell("Check out date: " + checkOutDate);
                table1.addCell("Number of days staying: " + daysStaying);
                table1.addCell("Total amount: " + String.format("%.2f", total));
                doc.add(table1);


                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to print the bill?",
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.showAndWait();

                File file = new File(path + " " + customer.getId() + ".pdf");
                if(file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("File does not exist");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        customerHistoryList = getCustomerHistoryList();
        table.setItems(customerHistoryList);
        implementSearch();
    }



    private ObservableList<Customer> getCustomerHistoryList() {
        ObservableList<Customer> tmp = FXCollections.observableArrayList();

        try {
            String currentUser = UserSession.getInstace(textFieldEmail.getText()).getEmail();
            String query = "select * from customers where check_out_date is not null and email='" + currentUser + "'";
            ResultSet rs = DataController.getData(query);

            while (rs.next()) {
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
                        rs.getDouble("price_per_day"),
                        rs.getInt("days_staying"),
                        rs.getDouble("total_amount"), rs.getString("check_out_date"), rs.getString("paid"));
                tmp.add(customer);
            }
         } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    private void implementSearch() {
        FilteredList<Customer> filteredList = new FilteredList<>(customerHistoryList, u -> true);
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
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colDaysStaying.setCellValueFactory(new PropertyValueFactory<>("daysStaying"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("isPaid"));

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


}
