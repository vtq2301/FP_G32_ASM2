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
import javafx.stage.Stage;
import all.auth.AdminDatabase;
import all.db.dbConnection;
import all.model.customer.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminInfoScreenController implements Initializable {
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
    private static final dbConnection dbConn = new dbConnection();
    private final ObservableList<User> list = FXCollections.observableArrayList();
    @FXML
    private Button btnBack;
    @FXML
    private void handleAddButtonAction(ActionEvent e){handleAddAdmins();}

    private void handleAddAdmins() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Admin");
        dialog.setHeaderText("Create a New Admin");
        dialog.setContentText("Please enter the Admin information:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            User admin = new User(null,tfUsername.getText(),"1","Admin",tfFullName.getText(),tfAddress.getText(),tfPhoneNumber.getText());
            list.addAll(admin);
            addAdmin(admin);
            loadData();
        });
    }

    private void loadData() {
        setCellValueAdmin();
        list.setAll(AdminDatabase.getAdminList());
        tvAdmin.setItems(list);
    }

    private void addAdmin(User admin) {
        String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, tfUsername.getText());
            ps.setString(3, tfPassword.getText());
            ps.setString(4, "Admin");
            ps.setString(5, tfFullName.getText());
            ps.setString(6, tfAddress.getText());
            ps.setString(7, tfPhoneNumber.getText());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                admin.setId(id);
            } else {
                throw new SQLException("Creating Admin failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        TextInputDialog dialog = new TextInputDialog(selectedAdmin.getId());
        dialog.setTitle("Update Admin");
        dialog.setHeaderText("Edit the Admin");
        dialog.setContentText("Enter the new information:");

        Optional<String> result = dialog.showAndWait();

        updateAdmin();
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAdmin() {
        String sql = "UPDATE users SET username = ?," +
                " password_hash = ?, role = ?" +
                " full_name = ?," +
                " address = ?," +
                " phone_number = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tfUsername.getText());
            ps.setString(2, tfPassword.getText());
            ps.setString(3, tfRole.getText());
            ps.setString(4, tfFullName.getText());
            ps.setString(5, tfAddress.getText());
            ps.setString(6, tfPhoneNumber.getText());
            ps.setString(7, tfID.getText());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent e){handleDeleteAdmins();}

    private void handleDeleteAdmins() {
        User selectedAdmin = tvAdmin.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            deleteAdmin(selectedAdmin.getId());
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
    private void deleteAdmin(String id){
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

        public void setCellValueAdmin(){
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
    int index = -1;
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = tvAdmin.getSelectionModel().getSelectedIndex();
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }
}
