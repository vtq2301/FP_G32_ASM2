/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
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
import all.auth.PolicyHolderDatabase;
import all.db.dbConnection;
import all.model.customer.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PolicyHolderScreenController implements Initializable {
    @FXML
    private TableView<User> tvPolicyHolder = new TableView<User>();
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
    private final ObservableList<User> list = FXCollections.observableArrayList();
    private final PolicyHolderDatabase dbService = new PolicyHolderDatabase();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddPolicyHolder();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdatePolicyHolder();    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK)
            {handleDeletePolicyHolder();}
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
        setCellValuePolicyHolders();
        list.setAll(PolicyHolderDatabase.getUserList());
        tvPolicyHolder.setItems(list);
    }

    public void setCellValuePolicyHolders(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
    }
    private void handleAddPolicyHolder(){
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Policy Holder");
        dialog.setHeaderText("Create new Policy Holder");
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
                return new User(null,tfUsername.getText(),tfPassword.getText(),"PolicyHolder",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(policyHolder ->{

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Policy Holder", "Add new policy holder", null);
            dbService.addPolicyHolders(policyHolder);
            loadData();
        }) ;
    }


    private void handleUpdatePolicyHolder() {
        User selectedPolicyHolder = tvPolicyHolder.getSelectionModel().getSelectedItem();
        if (selectedPolicyHolder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Policy Holder Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a policy holder to update.");
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
        TextField tfFullName = new TextField(selectedPolicyHolder.getFullName());
        TextField tfPhoneNumber = new TextField(selectedPolicyHolder.getPhoneNumber());
        TextField tfAddress = new TextField(selectedPolicyHolder.getAddress());
        TextField tfPassword = new TextField(selectedPolicyHolder.getPassword());
        TextField tfUsername = new TextField(selectedPolicyHolder.getUsername());
        TextField tfRole = new TextField(selectedPolicyHolder.getRole());

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
                selectedPolicyHolder.setUsername(tfUsername.getText());
                selectedPolicyHolder.setAddress(tfAddress.getText());
                selectedPolicyHolder.setPassword(tfPassword.getText());
                selectedPolicyHolder.setFullName(tfFullName.getText());
                selectedPolicyHolder.setPhoneNumber(tfPhoneNumber.getText());
                selectedPolicyHolder.setRole(tfRole.getText());

                return selectedPolicyHolder;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(PolicyHolder -> {
            dbService.updatePolicyHolder(selectedPolicyHolder);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedPolicyHolder.getUsername(), "Update Insurance Manager", "Edit Insurance Manager", null);

            loadData();
        });
    }

    private void handleDeletePolicyHolder() {
        User selectedPolicyHolder = tvPolicyHolder.getSelectionModel().getSelectedItem();
        if (selectedPolicyHolder != null) {
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedPolicyHolder.getUsername(), "Delete Policy Holder", "Deleted Policy Holder with ID: " + selectedPolicyHolder.getId(), null);
            dbService.deletePolicyHolder(selectedPolicyHolder.getId());
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
