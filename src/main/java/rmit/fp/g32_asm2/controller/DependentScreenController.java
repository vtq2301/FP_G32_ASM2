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
import rmit.fp.g32_asm2.auth.DependentDatabase;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DependentScreenController implements Initializable {
    private final DependentDatabase dbService = new DependentDatabase();
    @FXML
    private TableView<User> tvDependent = new TableView<User>();
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
    private TableColumn<User, String> colPolicyHolderId;

    @FXML
    private Button btnBack;

    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddDependents();
    }

    private void handleAddDependents() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Dependent");
        dialog.setHeaderText("Create new dependent");
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
        TextField tfPolicyHolderId = new TextField();

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
        grid.add(new Label("Policy Holder Id"),0,5);
        grid.add(tfPolicyHolderId, 1, 5);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new User(null,tfUsername.getText(),tfPassword.getText(),"Dependent",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText(),tfPolicyHolderId.getText());
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(dependent ->{
            dbService.addDependents(dependent);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Dependent", "Add new Dependent", null);
            loadData();
        }) ;
    }



    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdateDependents();
    }

    private void handleUpdateDependents() {
        User selectedDependent = tvDependent.getSelectionModel().getSelectedItem();
        if (selectedDependent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Dependent Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a Dependent to update.");
            alert.showAndWait();
            return;
        }

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update Dependent");
        dialog.setHeaderText("Edit the Dependent");
        dialog.setContentText("Enter Dependent information:");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField tfFullName = new TextField(selectedDependent.getFullName());
        TextField tfPhoneNumber = new TextField(selectedDependent.getPhoneNumber());
        TextField tfAddress = new TextField(selectedDependent.getAddress());
        TextField tfPassword = new TextField(selectedDependent.getPassword());
        TextField tfUsername = new TextField(selectedDependent.getUsername());
        TextField tfPolicyHolderId = new TextField(selectedDependent.getPolicyHolderId());
        TextField tfRole = new TextField(selectedDependent.getRole());

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
        grid.add(new Label("Policy Holder ID"), 0, 6);
        grid.add(tfPolicyHolderId, 1, 6);
        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selectedDependent.setUsername(tfUsername.getText());
                selectedDependent.setPassword(tfPassword.getText());
                selectedDependent.setAddress(tfAddress.getText());
                selectedDependent.setFullName(tfFullName.getText());
                selectedDependent.setPhoneNumber(tfPhoneNumber.getText());
                selectedDependent.setPolicyHolderId(tfPolicyHolderId.getText());
                selectedDependent.setRole(tfRole.getText());

                return selectedDependent;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(dependent -> {
            dbService.updateDependents(selectedDependent);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedDependent.getUsername(), "Update Dependent", "Edit Dependent", null);

            loadData();
        });

    }



    @FXML
    private void handleDeleteButtonAction(ActionEvent e) {
        handleDeleteDependents();
    }

    private void handleDeleteDependents() {
        User selectedDependent = tvDependent.getSelectionModel().getSelectedItem();
        if (selectedDependent != null) {
            dbService.deleteDependents(selectedDependent.getId());
            loadData();
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedDependent.getPolicyHolderId(), "Delete Dependent", "Deleted Dependent with ID: " + selectedDependent.getId(), null);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No Dependent Selected");
            alert.setContentText("Please select dependent in the table");
            alert.showAndWait();
        }
    }



    @FXML
    private void handleBackButtonAction(ActionEvent e){
        setBtnBack();
    }

    private void loadData() {
        setCellValueDependents();
        list.setAll(DependentDatabase.getDependentList());
        tvDependent.setItems(list);
    }
    public void setCellValueDependents(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
        colPolicyHolderId.setCellValueFactory(new PropertyValueFactory<User,String>("policyHolderId"));
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
