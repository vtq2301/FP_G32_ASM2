package rmit.fp.g32_asm2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.auth.ActionLogger;
import rmit.fp.g32_asm2.auth.PolicyHolderDatabase;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.customer.PolicyHolder;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
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
    private TableView<PolicyHolder> tvPolicyHolder = new TableView<PolicyHolder>();
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
    private Button btnBack;
    private static final dbConnection dbConn = new dbConnection();
    private final ObservableList<PolicyHolder> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddPolicyHolder();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdatePolicyHolder();    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e) throws Exception {
        handleDeletePolicyHolder();
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

    private void loadData() throws Exception {
        setCellValuePolicyHolders();
        list.setAll(PolicyHolderDatabase.getPolicyHolderList());
        tvPolicyHolder.setItems(list);
    }

    public void setCellValuePolicyHolders(){
        colId.setCellValueFactory(new PropertyValueFactory<PolicyHolder,String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<PolicyHolder,String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<PolicyHolder,String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<PolicyHolder,String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<PolicyHolder,String>("address"));
    }
    public void addPolicyHolders(PolicyHolder policyHolder){
        String query = "INSERT INTO policy_holders VALUES (?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, tfName.getText());
            ps.setString(3, tfPhone.getText());
            ps.setString(4, tfAddress.getText());
            ps.setString(5, tfEmail.getText());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                policyHolder.setId(id); // Set the claim ID back on the object
            } else {
                throw new SQLException("Creating policy holder failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePolicyHolder(PolicyHolder policyHolder) {
        String sql = "UPDATE policy_holders SET name = ?, phone = ?, address = ?, email = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tfName.getText());
            ps.setString(2, tfPhone.getText());
            ps.setString(3, tfAddress.getText());
            ps.setString(4, tfEmail.getText());
            ps.setString(5, tfID.getText());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void handleAddPolicyHolder(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Policy Holder");
        dialog.setHeaderText("Create a New Policy Holder");
        dialog.setContentText("Please enter the policy holder information:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            PolicyHolder policyHolder = new PolicyHolder(null,tfName.getText(),tfPhone.getText(),tfAddress.getText(),tfEmail.getText());
            list.addAll(policyHolder);
            addPolicyHolders(policyHolder);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolder.getId(), "Add Policy Holder", "Added new Policy Holder" + description,null);
            try {
                loadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void handleUpdatePolicyHolder() {
        PolicyHolder selectedPolicyHolder = tvPolicyHolder.getSelectionModel().getSelectedItem();
        if (selectedPolicyHolder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Policy Holder Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a policy holder to update.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedPolicyHolder.getId());
        dialog.setTitle("Update Policy Holder");
        dialog.setHeaderText("Edit the Policy Holder");
        dialog.setContentText("Enter the new information:");

        Optional<String> result = dialog.showAndWait();

        updatePolicyHolder(selectedPolicyHolder);
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deletePolicyHolder(String id) {
        String sql = "DELETE FROM policy_holders WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tfID.getText());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deletion failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void handleDeletePolicyHolder() throws Exception {
        PolicyHolder selectedPolicyHolder = tvPolicyHolder.getSelectionModel().getSelectedItem();
        if (selectedPolicyHolder != null) {
            deletePolicyHolder(selectedPolicyHolder.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No Policy Holder Selected");
            alert.setContentText("Please select policy holder in the table");
            alert.showAndWait();
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
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvPolicyHolder.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        tfID.setText(colId.getCellData(index).toString());
        tfName.setText(colName.getCellData(index).toString());
        tfPhone.setText(colPhone.getCellData(index).toString());
        tfEmail.setText(colEmail.getCellData(index).toString());
        tfAddress.setText(colAddress.getCellData(index).toString());
    }
}
