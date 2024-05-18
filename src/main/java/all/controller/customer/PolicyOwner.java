package all.controller.customer;

import all.auth.AuthService;
import all.controller.UserSession;
import all.model.customer.Customer;
import all.model.customer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PolicyOwner {

    @FXML private Button logout;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField policyNumberField;
    @FXML
    private VBox mainContent;

    @FXML
    private Button claimsButton;
    @FXML
    private Button beneficiariesButton;
    @FXML
    private Button logsButton;

    private final User currentUser = UserSession.getCurrentUser();

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456");

    }
    @FXML
    public void initialize() {
        // Initialize your fields with data if needed
        loadData(currentUser);
    }



    @FXML
    private void showClaims() {
        loadContent("/policy-owner-view/claims-view.fxml");
    }

    @FXML
    private void showBeneficiaries() {
        loadContent("/policy-owner-view/beneficiaries-view.fxml");
    }

    @FXML
    private void showLogs() {
        loadContent("/policy-owner-view/logs-view.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, handle the error with a dialog or some other way
        }
    }

    public void logout(ActionEvent actionEvent) {
        UserSession.logout();
        loadLoginScreen();
    }

    private void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
