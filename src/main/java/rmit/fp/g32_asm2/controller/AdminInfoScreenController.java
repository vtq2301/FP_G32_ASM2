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
import rmit.fp.g32_asm2.model.admin.SystemAdmin;


import java.sql.*;

public class AdminInfoScreenController {
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
        private TableView<SystemAdmin> tvSystemAdmin;
        @FXML
        private TableColumn<SystemAdmin,String> colId;
        @FXML
        private TableColumn<SystemAdmin, String> colName;
        @FXML
        private TableColumn<SystemAdmin, String> colPhone;
        @FXML
        private TableColumn<SystemAdmin, String> colAddress;
        @FXML
        private TableColumn<SystemAdmin, String> colEmail;
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
            addSystemAdmins();
        }
        @FXML
        private void handleUpdateButtonAction(ActionEvent e){
            updateSystemAdmins();
        }
        @FXML
        private void handleDeleteButtonAction(ActionEvent e){
            deleteSystemAdmins();
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
        public ObservableList<SystemAdmin> getSystemAdminsList(){
            ObservableList<SystemAdmin> systemAdminsList = FXCollections.observableArrayList();
            Connection conn = getConnection();
            String query = "SELECT * FROM system_admins";
            Statement st;
            ResultSet rs;
            try{
                st = conn.createStatement();
                rs = st.executeQuery(query);
                SystemAdmin systemAdmin;
                while (rs.next()){
                    systemAdmin = new SystemAdmin(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"), rs.getString("email"));
                    systemAdminsList.add(systemAdmin);
                };
            }catch (Exception e){
                e.printStackTrace();
            }
            return systemAdminsList;
        }
        public void showSystemAdmins(){
            ObservableList<SystemAdmin> list = getSystemAdminsList();

            colId.setCellValueFactory(new PropertyValueFactory<SystemAdmin, String>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<SystemAdmin, String>("name"));
            colPhone.setCellValueFactory(new PropertyValueFactory<SystemAdmin, String>("phone"));
            colEmail.setCellValueFactory(new PropertyValueFactory<SystemAdmin, String>("email"));
            colAddress.setCellValueFactory(new PropertyValueFactory<SystemAdmin, String>("address"));
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
        public void addSystemAdmins(){
            String query = "INSERT INTO system_admins VALUES (" + tfID.getText() + ",'" +tfName.getText() + "','"
                    + tfPhone.getText() + "'," + tfAddress.getText() + "," + tfEmail.getText() +")";
            executeQuery(query);
            showSystemAdmins();
        }



        private void updateSystemAdmins() {
            String query = "UPDATE system_admins SET name = '" + tfName.getText() + "', phone = '" + tfPhone.getText()
                    + "', address = '" + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
            executeQuery(query);
            showSystemAdmins();
        }
        private void deleteSystemAdmins() {
            String query = "DELETE FROM system_admins WHERE id = " + tfID.getText()+ "";
            executeQuery(query);
            showSystemAdmins();
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
