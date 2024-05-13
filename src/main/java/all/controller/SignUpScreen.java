package all.controller;

import all.auth.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class SignUpScreen {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label statusText;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String role = roleComboBox.getSelectionModel().getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || role == null) {
            statusText.setText("All fields are required.");
            return;
        }
        boolean success = authService.registerUser(username, password, role, fullName, address, phoneNumber);
        if (success) {
            statusText.setText("Registration successful!");
        } else {
            statusText.setText("Registration failed. User may already exist.");
        }
    }

    @FXML
    protected void handleShowLogin(ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
        Scene scene = new Scene(loginRoot);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("PolicyHolder", "PolicyOwner", "Dependent", "InsuranceSurveyor", "InsuranceManager"));
    }
}
