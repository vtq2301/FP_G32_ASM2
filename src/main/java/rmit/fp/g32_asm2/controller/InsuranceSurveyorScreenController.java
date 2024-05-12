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
import rmit.fp.g32_asm2.model.provider.InsuranceSurveyor;

import java.sql.*;

public class InsuranceSurveyorScreenController {

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
        private TableView<InsuranceSurveyor> tvInsuranceSurveyor;
        @FXML
        private TableColumn<InsuranceSurveyor,String> colId;
        @FXML
        private TableColumn<InsuranceSurveyor, String> colName;
        @FXML
        private TableColumn<InsuranceSurveyor, String> colPhone;
        @FXML
        private TableColumn<InsuranceSurveyor, String> colAddress;
        @FXML
        private TableColumn<InsuranceSurveyor, String> colEmail;
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
            addInsuranceSurveyors();
        }
        @FXML
        private void handleUpdateButtonAction(ActionEvent e){
            updateInsuranceSurveyors();
        }
        @FXML
        private void handleDeleteButtonAction(ActionEvent e){
            deleteInsuranceSurveyors();
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
        public ObservableList<InsuranceSurveyor> getInsuranceSurveyorsList(){
            ObservableList<InsuranceSurveyor> insuranceSurveyorsList = FXCollections.observableArrayList();
            Connection conn = getConnection();
            String query = "SELECT * FROM insurance_surveyors";
            Statement st;
            ResultSet rs;
            try{
                st = conn.createStatement();
                rs = st.executeQuery(query);
                InsuranceSurveyor insuranceSurveyor;
                while (rs.next()){
                    insuranceSurveyor = new InsuranceSurveyor(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"), rs.getString("email"));
                    insuranceSurveyorsList.add(insuranceSurveyor);
                };
            }catch (Exception e){
                e.printStackTrace();
            }
            return insuranceSurveyorsList;
        }
        public void showInsuranceSurveyor(){
            ObservableList<InsuranceSurveyor> list = getInsuranceSurveyorsList();

            colId.setCellValueFactory(new PropertyValueFactory<InsuranceSurveyor, String>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<InsuranceSurveyor, String>("name"));
            colPhone.setCellValueFactory(new PropertyValueFactory<InsuranceSurveyor, String>("phone"));
            colEmail.setCellValueFactory(new PropertyValueFactory<InsuranceSurveyor, String>("email"));
            colAddress.setCellValueFactory(new PropertyValueFactory<InsuranceSurveyor, String>("address"));
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
        public void addInsuranceSurveyors(){
            String query = "INSERT INTO insurance_surveyors VALUES (" + tfID.getText() + ",'" +tfName.getText() + "','"
                    + tfPhone.getText() + "'," + tfAddress.getText() + "," + tfEmail.getText() +")";
            executeQuery(query);
            showInsuranceSurveyor();
        }



        private void updateInsuranceSurveyors() {
            String query = "UPDATE insurance_surveyors SET name = '" + tfName.getText() + "', phone = '" + tfPhone.getText()
                    + "', address = '" + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
            executeQuery(query);
            showInsuranceSurveyor();
        }
        private void deleteInsuranceSurveyors() {
            String query = "DELETE FROM insurance_surveyors WHERE id = " + tfID.getText()+ "";
            executeQuery(query);
            showInsuranceSurveyor();
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
