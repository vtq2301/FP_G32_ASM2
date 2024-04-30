package all.controller;

import all.auth.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreen {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusText; // Correct variable name used here

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
            loadClaimsScreen();  // Load the next screen upon successful login
        } else {
            statusText.setText("Login failed. Please check your username and password.");
        }
    }

    @FXML
    protected void handleShowSignUp(ActionEvent event) {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/SignUpScreen.fxml"));
            Scene scene = new Scene(signUpRoot);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusText.setText("Failed to load the Sign Up screen.");
        }
    }

    private void loadClaimsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Claims Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusText.setText("Failed to load the claims screen: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            statusText.setText("An error occurred: " + e.getMessage());
        }
    }


}
