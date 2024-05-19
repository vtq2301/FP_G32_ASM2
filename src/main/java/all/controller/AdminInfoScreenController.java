package all.controller;

import all.auth.ActionLogger;
import all.auth.AdminDatabase;
import all.model.customer.User;
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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminInfoScreenController implements Initializable {

    @FXML
    private TableView<User> tvAdmin = new TableView<User>();

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
    private final ObservableList<User> list = FXCollections.observableArrayList();
    private final AdminDatabase dbService = new AdminDatabase();
    @FXML
    private Button btnBack;
    @FXML
    private void handleAddButtonAction(ActionEvent e){handleAddAdmins();}

    private void handleAddAdmins() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add new Admin");
        dialog.setHeaderText("Create new Admin");
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
                return new User(null,tfUsername.getText(),tfPassword.getText(),"PolicyOwner",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            }
            return null;
        });
        Optional<User> result =dialog.showAndWait();
        result.ifPresent(admin ->{
            dbService.addAdmin(admin);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(tfUsername.getText(), "Add Admin", "Add new Admin", null);
            loadData();
        }) ;
    }

    private void loadData() {
        setCellValueAdmin();
        list.setAll(AdminDatabase.getAdminList());
        tvAdmin.setItems(list);
    }


    @FXML
    private void handleUpdateButtonAction(ActionEvent e){handleUpdateAdmins();}

    private void handleUpdateAdmins() {
        User selectedAdmin = tvAdmin.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Admin Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a Admin to update.");
            alert.showAndWait();
            return;
        }

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update Admin");
        dialog.setHeaderText("Edit the Admin");
        dialog.setContentText("Enter the new information:");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField tfFullName = new TextField(selectedAdmin.getFullName());
        TextField tfPhoneNumber = new TextField(selectedAdmin.getPhoneNumber());
        TextField tfAddress = new TextField(selectedAdmin.getAddress());
        TextField tfPassword = new TextField(selectedAdmin.getPassword());
        TextField tfUsername = new TextField(selectedAdmin.getUsername());
        TextField tfRole = new TextField(selectedAdmin.getRole());
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
                selectedAdmin.setUsername(tfUsername.getText());
                selectedAdmin.setAddress(tfAddress.getText());
                selectedAdmin.setPassword(tfPassword.getText());
                selectedAdmin.setFullName(tfFullName.getText());
                selectedAdmin.setPhoneNumber(tfPhoneNumber.getText());
                selectedAdmin.setRole(tfRole.getText());
                return selectedAdmin;
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(insuranceSurveyor -> {
            dbService.updateAdmin(selectedAdmin);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedAdmin.getUsername(), "Update Admin", "Edit Admin", null);

            loadData();
        });
    }


    @FXML
    private void handleDeleteButtonAction(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK){handleDeleteAdmins();}}

    private void handleDeleteAdmins() {
        User selectedAdmin = tvAdmin.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(selectedAdmin.getUsername(), "Delete Admin", "Deleted Admin with ID: " + selectedAdmin.getId(), null);
            dbService.deleteAdmin(selectedAdmin.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No Admin Selected");
            alert.setContentText("Please select Admin in the table");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleBackButtonAction(ActionEvent e){
        setBtnBack();
    }

    public void setCellValueAdmin(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
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
}
