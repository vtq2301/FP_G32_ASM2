package all.controller;

import all.auth.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class SignUpScreen {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusText;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            statusText.setText("Username and password cannot be empty.");
            return;
        }
        boolean success = authService.registerUser(username, password);
        if (success) {
            statusText.setText("Registration successful!");
        } else {
            statusText.setText("Registration failed. User may already exist.");
        }
    }
}
