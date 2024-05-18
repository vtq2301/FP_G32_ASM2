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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.auth.ActionLogger;
import rmit.fp.g32_asm2.auth.InsuranceManagerDatabase;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InsuranceManagerScreenController implements Initializable {
    @FXML
    private TableView<User> tvInsuranceManager = new TableView<User>();
    @FXML
    private TableColumn<User,String> colId;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colPassword;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, String> colFullName;
    @FXML
    private TableColumn<User, String> colPhoneNumber;
    @FXML
    private TableColumn<User, String> colAddress;

    @FXML
    private Button btnBack;
    private final InsuranceManagerDatabase dbService = new InsuranceManagerDatabase();

    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddInsuranceManagers();
    }

    private void handleAddInsuranceManagers() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Insurance Manager");
        dialog.setHeaderText("Create new Insurance Manager");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField tfFullName = new TextField();
        TextField tfPhoneNumber = new TextField();
        TextField tfAddress = new TextField();
        TextField tfPassword = new TextField();
        TextField tfUsername = new TextField();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(tfUsername, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(tfPassword, 1, 1);
        grid.add(new Label("Full name:"), 0, 2);
        grid.add(tfFullName, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(tfAddress, 1, 3);
        grid.add(new Label("Phone Number:"), 0, 4);
        grid.add(tfPhoneNumber, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new User(null,tfUsername.getText(),tfPassword.getText(),"InsuranceManager",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(insuranceManager ->{
            dbService.addInsuranceManagers(insuranceManager);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Insurance Manager", "Add new Insurance Manager", null);
            loadData();
        }) ;
    }



    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdateInsuranceManagers();
    }

    private void handleUpdateInsuranceManagers() {
        User selectedInsuranceManager = tvInsuranceManager.getSelectionModel().getSelectedItem();
        if (selectedInsuranceManager == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No InsuranceManager Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a InsuranceManager to update.");
            alert.showAndWait();
            return;
        }

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update Insurance Manager");
        dialog.setHeaderText("Edit the Insurance Manager");
        dialog.setContentText("Enter the new information:");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField tfFullName = new TextField(selectedInsuranceManager.getFullName());
        TextField tfPhoneNumber = new TextField(selectedInsuranceManager.getPhoneNumber());
        TextField tfAddress = new TextField(selectedInsuranceManager.getAddress());
        TextField tfPassword = new TextField(selectedInsuranceManager.getPassword());
        TextField tfUsername = new TextField(selectedInsuranceManager.getUsername());
        TextField tfRole = new TextField(selectedInsuranceManager.getRole());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(tfUsername, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(tfPassword, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(tfRole, 1, 2);
        grid.add(new Label("Full name:"), 0, 3);
        grid.add(tfFullName, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(tfAddress, 1, 4);
        grid.add(new Label("Phone Number:"), 0, 5);
        grid.add(tfPhoneNumber, 1, 5);

        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selectedInsuranceManager.setUsername(tfUsername.getText());
                selectedInsuranceManager.setAddress(tfAddress.getText());
                selectedInsuranceManager.setPassword(tfPassword.getText());
                selectedInsuranceManager.setRole(tfRole.getText());
                selectedInsuranceManager.setFullName(tfFullName.getText());
                selectedInsuranceManager.setPhoneNumber(tfPhoneNumber.getText());

                return selectedInsuranceManager;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(insuranceManager -> {
            dbService.updateInsuranceManagers(selectedInsuranceManager);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedInsuranceManager.getUsername(), "Update Insurance Manager", "Edit Insurance Manager", null);

            loadData();
        });

    }



    @FXML
    private void handleDeleteButtonAction(ActionEvent e) {
        handleDeleteInsuranceManagers();
    }

    private void handleDeleteInsuranceManagers() {
        User selectedInsuranceManager = tvInsuranceManager.getSelectionModel().getSelectedItem();
        if (selectedInsuranceManager != null) {
            dbService.deleteInsuranceManager(selectedInsuranceManager.getId());
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



    @FXML
    private void handleBackButtonAction(ActionEvent e){
        setBtnBack();
    }

    private void loadData() {
        setCellValueInsuranceManagers();
        list.setAll(InsuranceManagerDatabase.getInsuranceManagerList());
        tvInsuranceManager.setItems(list);
    }
    public void setCellValueInsuranceManagers(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }
}
