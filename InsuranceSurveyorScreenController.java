package all.controller;

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
import all.auth.ActionLogger;
import all.auth.InsuranceSurveyorDatabase;
import all.db.dbConnection;
import all.model.customer.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InsuranceSurveyorScreenController implements Initializable {
    @FXML
    private TableView<User> tvInsuranceSurveyor = new TableView<User>();
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
    private final InsuranceSurveyorDatabase dbService = new InsuranceSurveyorDatabase();
    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddInsuranceSurveyors();
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent e){
        handleUpdateInsuranceSurveyors();
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent e) {
        handleDeleteInsuranceSurveyors();
    }
    private void handleAddInsuranceSurveyors() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Insurance Surveyor");
        dialog.setHeaderText("Create new Insurance Surveyor");
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
                String username = tfUsername.getText();
                String password = tfPassword.getText();
                String fullName = tfFullName.getText();
                String phoneNumber = tfPhoneNumber.getText();
                String address = tfAddress.getText();

                return new User(null,username,password,"InsuranceSurveyor",fullName,address,phoneNumber);
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(insuranceSurveyor ->{
            dbService.addInsuranceSurveyors(insuranceSurveyor);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Insurance Surveyor", "Add new Insurance Surveyor", null);
            loadData();
        }) ;
    }




    private void handleUpdateInsuranceSurveyors() {
        User selectedInsuranceSurveyor = tvInsuranceSurveyor.getSelectionModel().getSelectedItem();
        if (selectedInsuranceSurveyor == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No InsuranceSurveyor Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a InsuranceSurveyor to update.");
            alert.showAndWait();
            return;
        }
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update Insurance Surveyor");
        dialog.setHeaderText("Edit the Insurance Surveyor");
        dialog.setContentText("Enter the new information:");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField tfFullName = new TextField(selectedInsuranceSurveyor.getFullName());
        TextField tfPhoneNumber = new TextField(selectedInsuranceSurveyor.getPhoneNumber());
        TextField tfAddress = new TextField(selectedInsuranceSurveyor.getAddress());
        TextField tfPassword = new TextField(selectedInsuranceSurveyor.getPassword());
        TextField tfUsername = new TextField(selectedInsuranceSurveyor.getUsername());
        TextField tfRole = new TextField(selectedInsuranceSurveyor.getRole());

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
                selectedInsuranceSurveyor.setUsername(tfUsername.getText());
                selectedInsuranceSurveyor.setAddress(tfAddress.getText());
                selectedInsuranceSurveyor.setPassword(tfPassword.getText());
                selectedInsuranceSurveyor.setRole(tfRole.getText());
                selectedInsuranceSurveyor.setFullName(tfFullName.getText());
                selectedInsuranceSurveyor.setPhoneNumber(tfPhoneNumber.getText());

                return selectedInsuranceSurveyor;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(insuranceSurveyor -> {
            dbService.updateInsuranceSurveyors(selectedInsuranceSurveyor);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedInsuranceSurveyor.getUsername(), "Update Insurance Surveyor", "Edit Insurance Surveyor", null);

            loadData();
        });
    }



    private void handleDeleteInsuranceSurveyors() {
        User selectedInsuranceSurveyor = tvInsuranceSurveyor.getSelectionModel().getSelectedItem();
        if (selectedInsuranceSurveyor != null) {
            dbService.deleteInsuranceSurveyors(selectedInsuranceSurveyor.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No InsuranceSurveyor Selected");
            alert.setContentText("Please select InsuranceSurveyor in the table");
            alert.showAndWait();
        }
    }



    @FXML
    private void handleBackButtonAction(ActionEvent e){
        setBtnBack();
    }
    public void setCellValueInsuranceSurveyor(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
    }
    private void loadData() {
        setCellValueInsuranceSurveyor();
        list.setAll(InsuranceSurveyorDatabase.getInsuranceSurveyorList());
        tvInsuranceSurveyor.setItems(list);
    }
    private void setBtnBack() {
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
       loadData();
    }
}
