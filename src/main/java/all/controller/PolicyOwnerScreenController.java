package all.controller;

import all.auth.ActionLogger;
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
import all.auth.PolicyOwnerDatabase;
import all.db.dbConnection;
import all.model.customer.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PolicyOwnerScreenController implements Initializable {
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;

    @FXML
    private TableView<User> tvPolicyOwner = new TableView<User>();
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
    private static final dbConnection dbConn = new dbConnection();
    private final PolicyOwnerDatabase dbService = new PolicyOwnerDatabase();
    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddPolicyOwner();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdatePolicyOwner();    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK)
            {handleDeletePolicyOwner();}
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

    private void loadData() {
        setCellValuePolicyOwners();
        list.setAll(PolicyOwnerDatabase.getUserList());
        tvPolicyOwner.setItems(list);
    }

    public void setCellValuePolicyOwners(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
    }
    private void handleAddPolicyOwner(){
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Policy Owner");
        dialog.setHeaderText("Create new Policy Owner");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tfID = new TextField();
        TextField tfFullName = new TextField();
        TextField tfPhoneNumber = new TextField();
        TextField tfAddress = new TextField();
        TextField tfPassword = new TextField();
        TextField tfUsername = new TextField();
        TextField tfRole = new TextField();
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
                return new User(null,tfUsername.getText(),tfPassword.getText(),"PolicyOwner",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(policyOwner ->{
            dbService.addPolicyOwners(policyOwner);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Policy Owner", "Add new policy owner", null);
            loadData();
        }) ;
    }



    private void handleUpdatePolicyOwner() {
        User selectedPolicyOwner = tvPolicyOwner.getSelectionModel().getSelectedItem();
        if (selectedPolicyOwner == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Policy Owner Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a policy Owner to update.");
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
        TextField tfFullName = new TextField(selectedPolicyOwner.getFullName());
        TextField tfPhoneNumber = new TextField(selectedPolicyOwner.getPhoneNumber());
        TextField tfAddress = new TextField(selectedPolicyOwner.getAddress());
        TextField tfPassword = new TextField(selectedPolicyOwner.getPassword());
        TextField tfUsername = new TextField(selectedPolicyOwner.getUsername());
        TextField tfRole = new TextField(selectedPolicyOwner.getRole());

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
                selectedPolicyOwner.setUsername(tfUsername.getText());
                selectedPolicyOwner.setAddress(tfAddress.getText());
                selectedPolicyOwner.setPassword(tfPassword.getText());
                selectedPolicyOwner.setFullName(tfFullName.getText());
                selectedPolicyOwner.setPhoneNumber(tfPhoneNumber.getText());
                selectedPolicyOwner.setRole(tfRole.getText());
                return selectedPolicyOwner;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(PolicyOwner -> {
            dbService.updatePolicyOwner(selectedPolicyOwner);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedPolicyOwner.getUsername(), "Update Insurance Manager", "Edit Insurance Manager", null);

            loadData();
        });
    }

    private void handleDeletePolicyOwner() {
        User selectedPolicyOwner = tvPolicyOwner.getSelectionModel().getSelectedItem();
        if (selectedPolicyOwner != null) {
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedPolicyOwner.getUsername(), "Delete Policy Owner", "Deleted Policy Owner with ID: " + selectedPolicyOwner.getId(), null);
            dbService.deletePolicyOwner(selectedPolicyOwner.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No Policy Owner Selected");
            alert.setContentText("Please select policy Owner in the table");
            alert.showAndWait();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
