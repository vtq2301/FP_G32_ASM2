package all.controller;

import all.auth.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class LoginScreen {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusText;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            statusText.setText("Username and password cannot be empty.");
            return;
        }
        boolean success = authService.authenticateUser(username, password);
        if (success) {
            statusText.setText("Login successful!");
            // Navigate to the main application screen
        } else {
            statusText.setText("Login failed. Please check your username and password.");
        }
    }
}
