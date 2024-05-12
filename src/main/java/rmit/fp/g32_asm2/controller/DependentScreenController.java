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
import rmit.fp.g32_asm2.model.customer.Dependent;

import java.sql.*;

public class DependentScreenController {
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
    private TableView<Dependent> tvDependent;
    @FXML
    private TableColumn<Dependent,String> colId;
    @FXML
    private TableColumn<Dependent, String> colName;
    @FXML
    private TableColumn<Dependent, String> colPhone;
    @FXML
    private TableColumn<Dependent, String> colAddress;
    @FXML
    private TableColumn<Dependent, String> colEmail;
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
        addDependents();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        updateDependents();
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e){
        deleteDependents();
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
    public ObservableList<Dependent> getDependentsList(){
        ObservableList<Dependent> dependentsList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM dependents";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Dependent dependent;
            while (rs.next()){
                dependent = new Dependent(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"), rs.getString("email"));
                dependentsList.add(dependent);
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return dependentsList;
    }
    public void showDependents(){
        ObservableList<Dependent> list = getDependentsList();

        colId.setCellValueFactory(new PropertyValueFactory<Dependent, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Dependent, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Dependent, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Dependent, String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Dependent, String>("address"));
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
    public void addDependents(){
        String query = "INSERT INTO dependents VALUES (" + tfID.getText() + ",'" +tfName.getText() + "','"
                + tfPhone.getText() + "'," + tfAddress.getText() + "," + tfEmail.getText() +")";
        executeQuery(query);
        showDependents();
    }



    private void updateDependents() {
        String query = "UPDATE dependents SET name = '" + tfName.getText() + "', phone = '" + tfPhone.getText()
                + "', address = '" + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
        executeQuery(query);
        showDependents();
    }
    private void deleteDependents() {
        String query = "DELETE FROM dependents WHERE id = " + tfID.getText()+ "";
        executeQuery(query);
        showDependents();
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