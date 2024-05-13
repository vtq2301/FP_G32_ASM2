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
import rmit.fp.g32_asm2.auth.InsuranceManagerDatabase;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.provider.InsuranceManager;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InsuranceManagerScreenController implements Initializable {
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
    private TableView<InsuranceManager> tvInsuranceManager = new TableView<InsuranceManager>();
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

    private static final dbConnection dbConn = new dbConnection();
    private final ObservableList<InsuranceManager> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddInsuranceManagers();
    }

    private void handleAddInsuranceManagers() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Insurance Manager");
        dialog.setHeaderText("Create a New Insurance Manager");
        dialog.setContentText("Please enter the Insurance Manager information:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            InsuranceManager insuranceManager = new InsuranceManager(null,tfName.getText(),tfPhone.getText(),tfAddress.getText(),tfEmail.getText());
            list.addAll(insuranceManager);
            addInsuranceManagers(insuranceManager);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(insuranceManager.getId(), "Add InsuranceManager", "Added new InsuranceManager" + description,null);
            try {
                loadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addInsuranceManagers(InsuranceManager insuranceManager) {
        String query = "INSERT INTO insurance_managers VALUES (?,?,?,?,?)";
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
                InsuranceManager.setId(id);
            } else {
                throw new SQLException("Creating InsuranceManager failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdateInsuranceManagers();
    }

    private void handleUpdateInsuranceManagers() {
        InsuranceManager selectedInsuranceManager = tvInsuranceManager.getSelectionModel().getSelectedItem();
        if (selectedInsuranceManager == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No InsuranceManager Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a InsuranceManager to update.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedInsuranceManager.getId());
        dialog.setTitle("Update Insurance Manager");
        dialog.setHeaderText("Edit the Insurance Manager");
        dialog.setContentText("Enter the new information:");

        Optional<String> result = dialog.showAndWait();

        updateInsuranceManagers(selectedInsuranceManager);
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInsuranceManagers(InsuranceManager insuranceManager) {
        String sql = "UPDATE insurance_managers SET name = ?, phone = ?, address = ?, email = ? WHERE id = ?";
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

    @FXML
    private void handleDeleteButtonAction(ActionEvent e) throws Exception {
        handleDeleteInsuranceManagers();
    }

    private void handleDeleteInsuranceManagers() throws Exception {
        InsuranceManager selectedInsuranceManager = tvInsuranceManager.getSelectionModel().getSelectedItem();
        if (selectedInsuranceManager != null) {
            deleteInsuranceManagers(selectedInsuranceManager.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No InsuranceManager Selected");
            alert.setContentText("Please select InsuranceManager in the table");
            alert.showAndWait();
        }
    }

    private void deleteInsuranceManagers(String id) {
        String sql = "DELETE FROM InsuranceManagers WHERE id = ?";
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
    private void loadData() throws Exception {
        setCellValueInsuranceManagers();
        list.setAll(InsuranceManagerDatabase.getInsuranceManagerList());
        tvInsuranceManager.setItems(list);
    }
    public void setCellValueInsuranceManagers(){
        colId.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<InsuranceManager, String>("address"));
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
    int index = -1;
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvInsuranceManager.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        tfID.setText(colId.getCellData(index).toString());
        tfName.setText(colName.getCellData(index).toString());
        tfPhone.setText(colPhone.getCellData(index).toString());
        tfEmail.setText(colEmail.getCellData(index).toString());
        tfAddress.setText(colAddress.getCellData(index).toString());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
