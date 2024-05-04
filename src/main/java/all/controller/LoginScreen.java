package all.controller;

import all.auth.AuthService;
import all.controller.customer.PolicyHolder;
import all.model.customer.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import all.controller.customer.PolicyOwner;
import all.controller.customer.Dependent;

import java.io.IOException;

public class LoginScreen {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusText;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            statusText.setText("Username and password cannot be empty.");
            return;
        }
        User user = authService.authenticateUser(username, password);
        if (user != null) {
            statusText.setText("Login successful!");
            loadClaimsScreen(user);
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

    private void loadClaimsScreen(User user) {
        try {
            String fxmlFile = "/" + user.getRole() + "Screen.fxml"; // Ensure this matches the actual location of your FXML files
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            switch (user.getRole()) {
                case "PolicyOwner":
                    ((PolicyOwner) controller).loadData(user);
                    break;
                case "Dependent":
                    ((Dependent) controller).loadData(user);
                    break;
                case "PolicyHolder":
                    ((PolicyHolder) controller).loadData(user);
                    break;
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusText.setText("Failed to load the screen: " + e.getMessage());
        }
    }
}
