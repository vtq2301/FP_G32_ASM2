package rmit.fp.g32_asm2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rmit.fp.g32_asm2.model.customer.PolicyHolder;

import java.sql.*;

public class PolicyHolderScreenController {
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
    private TableView<PolicyHolder> tvCustomer;
    @FXML
    private TableColumn<PolicyHolder,String> colId;
    @FXML
    private TableColumn<PolicyHolder, String> colName;
    @FXML
    private TableColumn<PolicyHolder, String> colPhone;
    @FXML
    private TableColumn<PolicyHolder, String> colAddress;
    @FXML
    private TableColumn<PolicyHolder, String> colEmail;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBack;
    @FXML
    private void handleButtonAction(ActionEvent e){

    }
    public Connection getConnection(){
        Connection conn;
        try {
            conn = DriverManager.getConnection();
            return conn;
        }
        catch (Exception e){
            System.out.println("Error"+ e.getMessage());
            return null;
        }
    }
    public ObservableList<PolicyHolder> getPolicyHolderList(){
        ObservableList<PolicyHolder> policyHoldersList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM policyholders";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            PolicyHolder policyHolder;
            while (rs.next()){
                policyHolder = new PolicyHolder(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"), rs.getString("email"));
                policyHoldersList.add(policyHolder);
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return policyHoldersList;
    }
    public void showPolicyHolders(){
        ObservableList<PolicyHolder> list = getPolicyHolderList();

        colId.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("address"));
    }

}
