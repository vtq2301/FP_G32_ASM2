package rmit.fp.g32_asm2.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import rmit.fp.g32_asm2.model.User;

public class DashboardController {

    @FXML private TextField filterField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> addressColumn;
    @FXML private ImageView userIcon;

    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        users.add(new User.Builder()
                .withUsername("test")
                .withName("test")
                .withPhoneNumber("00000000")
                .withEmail("emailTest")
                .withAddress("addresssss")
                .build());
        userTable.setItems(users);
    }

    @FXML
    private void showClaims() {
        // Show Claims view
    }

    @FXML
    private void showCustomers() {
        // Show Customers view
    }

    @FXML
    private void showProviders() {
        // Show Providers view
    }

    @FXML
    private void addUser() {
        // Add User logic
    }

    @FXML
    private void editUser() {
        // Edit User logic
    }

    @FXML
    private void deleteUser() {
        // Delete User logic
    }

    @FXML
    private void goToUserInfoView() {
        // Navigate to User Info view
    }
}
