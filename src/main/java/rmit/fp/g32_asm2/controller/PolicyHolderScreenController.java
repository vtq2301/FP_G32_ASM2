package rmit.fp.g32_asm2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.customer.PolicyHolder;

import java.io.IOException;
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
        if(e.getSource() == btnAdd){
            addPolicyHolders();
        } else if (e.getSource() == btnUpdate) {
            updatePolicyHolders();
        } else if (e.getSource() == btnDelete) {
            deletePolicyHolders();
        }
    }
    public Connection getConnection(){
        Connection conn;
        try {
            conn = DriverManager.getConnection("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
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
    public void addPolicyHolders(){
        String query = "INSERT INTO policyholders VALUES (" + tfID.getText() + ",'" +tfName.getText() + "','"
                + tfPhone.getText() + "'," + tfAddress.getText() + "," + tfEmail.getText() +")";
        executeQuery(query);
        showPolicyHolders();
    }
    public void updatePolicyHolders(){
        String query = "UPDATE policyholders SET name = '" + tfName.getText() + "', phone = '" + tfPhone.getText()
                + "', address = '" + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
        executeQuery(query);
        showPolicyHolders();
    }
    @FXML
    private void deletePolicyHolders(){
        String query = "DELETE FROM policyholders WHERE id = " + tfID.getText()+ "";
        executeQuery(query);
        showPolicyHolders();
    }
    private void executeQuery(String query){
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void loadAdminScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) btnBack.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Admin System");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }

}
