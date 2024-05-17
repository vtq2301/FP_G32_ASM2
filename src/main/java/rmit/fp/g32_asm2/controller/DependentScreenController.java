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
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfRole;
    @FXML
    private TextField tfPolicyHolderId;
    @FXML
    private TextField tfAddress;

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
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBack;

    private static final dbConnection dbConn = new dbConnection();
    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private void handleAddButtonAction(ActionEvent e){
        handleAddDependents();
    }

    private void handleAddDependents() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Dependent");
        dialog.setHeaderText("Create a New Dependent");
        dialog.setContentText("Please enter the dependent information:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            User dependent = new User(null,tfUsername.getText(),tfPassword.getText(),"PolicyHolder",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText(),tfPolicyHolderId.getText());
            list.addAll(dependent);
            addDependents(dependent);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(dependent.getId(), "Add Dependent", "Added new Dependent" + description,null);
            try {
                loadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addDependents(User dependent) {
        String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, tfUsername.getText());
            ps.setString(3, tfPassword.getText());
            ps.setString(4, "Dependent");
            ps.setString(5, tfFullName.getText());
            ps.setString(6, tfPhoneNumber.getText());
            ps.setString(7, tfAddress.getText());
            ps.setString(8, tfPolicyHolderId.getText());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                dependent.setId(id);
            } else {
                throw new SQLException("Creating dependent failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            alert.setContentText("Please select a dependent to update.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedDependent.getId());
        dialog.setTitle("Update dependent");
        dialog.setHeaderText("Edit the dependent");
        dialog.setContentText("Enter the new information:");

        Optional<String> result = dialog.showAndWait();

        updateDependents(selectedDependent);
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateDependents(User dependent) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, full_name = ?,address = ?, phone_number = ?, policy_holder_id = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tfUsername.getText());
            ps.setString(2, tfPassword.getText());
            ps.setString(3, tfRole.getText());
            ps.setString(4, tfFullName.getText());
            ps.setString(5, tfAddress.getText());
            ps.setString(6, tfPhoneNumber.getText());
            ps.setString(7, tfPolicyHolderId.getText());
            ps.setString(8, tfID.getText());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent e) throws Exception {
        handleDeleteDependents();
    }

    private void handleDeleteDependents() throws Exception {
        User selectedDependent = tvDependent.getSelectionModel().getSelectedItem();
        if (selectedDependent != null) {
            deleteDependents(selectedDependent.getId());
            loadData();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No Dependent Selected");
            alert.setContentText("Please select dependent in the table");
            alert.showAndWait();
        }
    }

    private void deleteDependents(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
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
    int index = -1;
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvDependent.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        tfID.setText(colId.getCellData(index).toString());
        tfUsername.setText(colUsername.getCellData(index).toString());
        tfPassword.setText(colPassword.getCellData(index).toString());
        tfRole.setText(colRole.getCellData(index).toString());
        tfAddress.setText(colAddress.getCellData(index).toString());
        tfFullName.setText(colFullName.getCellData(index).toString());
        tfPolicyHolderId.setText(colPolicyHolderId.getCellData(index).toString());
        tfPhoneNumber.setText(colPhoneNumber.getCellData(index).toString());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }
}
