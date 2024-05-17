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
import rmit.fp.g32_asm2.auth.PolicyHolderDatabase;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.User;

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
    private TextField tfFullName;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfRole;

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
    private static final dbConnection dbConn = new dbConnection();
    private final ObservableList<User> list = FXCollections.observableArrayList();
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Policy Holder");
        dialog.setHeaderText("Create a New Policy Holder");
        dialog.setContentText("Please enter the policy holder information:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            User policyHolder = new User(null,tfUsername.getText(),"1","PolicyHolder",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            list.addAll(policyHolder);
            addPolicyHolders(policyHolder);
            try {
                loadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void addPolicyHolders(User policyHolder){
        String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, tfUsername.getText());
            ps.setString(3, "1");
            ps.setString(4, "PolicyHolder");
            ps.setString(5, tfFullName.getText());
            ps.setString(6, tfAddress.getText());
            ps.setString(7, tfPhoneNumber.getText());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                policyHolder.setId(id);
            } else {
                throw new SQLException("Creating policy holder failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePolicyHolder(User policyHolder) {
        String sql = "UPDATE users SET username = ?," +
                    " password_hash = ?," +
                    " full_name = ?," +
                    " address = ?," +
                    " phone_number = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tfUsername.getText());
            ps.setString(2, tfPassword.getText());
            ps.setString(3, tfFullName.getText());
            ps.setString(4, tfAddress.getText());
            ps.setString(5, tfPhoneNumber.getText());
            ps.setString(6, tfID.getText());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void handleDeletePolicyHolder() throws Exception {
        User selectedPolicyHolder = tvPolicyHolder.getSelectionModel().getSelectedItem();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    int index = -1;
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvPolicyHolder.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        tfID.setText(colId.getCellData(index).toString());
        tfUsername.setText(colUsername.getCellData(index).toString());
        tfPassword.setText(colPassword.getCellData(index).toString());
        tfRole.setText(colRole.getCellData(index).toString());
        tfAddress.setText(colAddress.getCellData(index).toString());
        tfFullName.setText(colFullName.getCellData(index).toString());
        tfPhoneNumber.setText(colPhoneNumber.getCellData(index).toString());
    }
}
