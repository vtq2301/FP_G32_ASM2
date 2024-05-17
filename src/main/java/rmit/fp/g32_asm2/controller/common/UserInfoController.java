package rmit.fp.g32_asm2.controller.common;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.model.User;

public class UserInfoController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField roleField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField isActiveField;

    private User currentUser = AuthContext.getCurrentUser();

    public void initialize() {
        usernameField.setText(AuthContext.getCurrentUser().getUsername());
        nameField.setText(AuthContext.getCurrentUser().getName());
        roleField.setText(currentUser.getClass().getSimpleName());
        phoneField.setText(currentUser.getPhoneNumber());
        emailField.setText(currentUser.getEmail());
        addressField.setText(currentUser.getAddress());
        isActiveField.setText(String.valueOf(currentUser.getIsActive()));
    }
    @FXML
    private void saveUserInfo() {
        // Logic to save user information
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel() {
        // Logic to cancel the operation
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
