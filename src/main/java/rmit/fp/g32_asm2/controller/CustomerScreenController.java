package rmit.fp.g32_asm2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rmit.fp.g32_asm2.model.customer.Customer;

public class CustomerScreenController {
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfRole;
    @FXML
    private TableView<Customer> tvCustomer;
    @FXML
    private TableColumn<Customer,String> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colRole;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBack;

}
