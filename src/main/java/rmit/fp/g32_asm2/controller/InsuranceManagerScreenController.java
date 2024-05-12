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

import rmit.fp.g32_asm2.model.provider.InsuranceManager;

import java.sql.*;

public class InsuranceManagerScreenController {
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
    private TableView<InsuranceManager> tvInsuranceManager;
    @FXML
    private TableColumn<InsuranceManager,String> colId;
    @FXML
    private TableColumn<InsuranceManager, String> colName;
    @FXML
    private TableColumn<InsuranceManager, String> colPhone;
    @FXML
    private TableColumn<InsuranceManager, String> colAddress;
    @FXML
    private TableColumn<InsuranceManager, String> colEmail;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBack;

    @FXML
    private void handleAddButtonAction(ActionEvent e){
        addInsuranceManagers();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        updateInsuranceManagers();
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e){
        deleteInsuranceManagers();
    }
    @FXML
    private void handleBackButtonAction(ActionEvent e){
        setBtnBack();
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
    public ObservableList<InsuranceManager> getInsuranceManagersList(){
        ObservableList<InsuranceManager> insuranceManagersList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM insurance_managers";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            InsuranceManager insuranceManager;
            while (rs.next()){
                insuranceManager = new InsuranceManager(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"), rs.getString("email"));
                insuranceManagersList.add(insuranceManager);
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return insuranceManagersList;
    }
    public void showInsuranceManager(){
        ObservableList<InsuranceManager> list = getInsuranceManagersList();

        colId.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("address"));
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
    public void addInsuranceManagers(){
        String query = "INSERT INTO insurance_managers VALUES (" + tfID.getText() + ",'" +tfName.getText() + "','"
                + tfPhone.getText() + "'," + tfAddress.getText() + "," + tfEmail.getText() +")";
        executeQuery(query);
        showInsuranceManager();
    }



    private void updateInsuranceManagers() {
        String query = "UPDATE insurance_managers SET name = '" + tfName.getText() + "', phone = '" + tfPhone.getText()
                + "', address = '" + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
        executeQuery(query);
        showInsuranceManager();
    }
    private void deleteInsuranceManagers() {
        String query = "DELETE FROM insurance_managers WHERE id = " + tfID.getText()+ "";
        executeQuery(query);
        showInsuranceManager();
    }

    private void setBtnBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/AdminScreen.fxml"));
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
