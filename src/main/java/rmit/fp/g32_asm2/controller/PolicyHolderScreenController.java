package rmit.fp.g32_asm2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.customer.PolicyHolder;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PolicyHolderScreenController implements Initializable {
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
    private TableView<PolicyHolder> tvPolicyHolder;
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
    private static final dbConnection dbConn = new dbConnection();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        addPolicyHolders();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        updatePolicyHolders();
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e){
        deletePolicyHolders();
    }
    @FXML
    private void handleBackButtonAction(ActionEvent e){
        loadAdminScreen();
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
    public static ObservableList<PolicyHolder> getPolicyHolderList(){
        ObservableList<PolicyHolder> policyHoldersList = FXCollections.observableArrayList();
        Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
        String query = "SELECT * FROM policy_holders";
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

        colId.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<PolicyHolder, String>("address"));
        ObservableList<PolicyHolder> list = getPolicyHolderList();
        tvPolicyHolder.setItems(list);
    }
    public void addPolicyHolders(){
        String query = "INSERT INTO policy_holders VALUES (" + tfID.getText() + "," +tfName.getText() + ","
                + tfPhone.getText() + "," + tfAddress.getText() + "," + tfEmail.getText() +")";
        executeQuery(query);
        showPolicyHolders();
    }
    public void updatePolicyHolders(){
        String query = "UPDATE policy_holders SET name = " + tfName.getText() + ", phone = " + tfPhone.getText()
                + ", address = " + tfAddress.getText() + ", email = " + tfEmail.getText() + "WHERE id = " + tfID.getText() + "";
        executeQuery(query);
        showPolicyHolders();
    }

    private void deletePolicyHolders(){
        String query = "DELETE FROM policy_holders WHERE id = " + tfID.getText()+ "";
        executeQuery(query);
        showPolicyHolders();
    }
    private void executeQuery(String query){
        Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
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
    int index = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPolicyHolders();
    }
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvPolicyHolder.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        tfID.setText(colId.getCellData(index).toString());
        tfName.setText(colName.getCellData(index));
        tfPhone.setText(colPhone.getCellData(index).toString());
        tfEmail.setText(colEmail.getCellData(index).toString());
        tfAddress.setText(colAddress.getCellData(index).toString());
    }
}
