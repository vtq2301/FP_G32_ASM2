package all.controller.customer;

import all.controller.UserSession;
import all.model.customer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PolicyHolder {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField policyNumberField;
    @FXML private Button manageClaimButton;
    private String policyHolderId;
    private String userRole; // Store user role

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456");
        this.policyHolderId = user.getId();
        this.userRole = user.getRole(); // Store user role

        if (this.policyHolderId == null || this.policyHolderId.isEmpty()) {
            System.err.println("Error: PolicyHolder ID is invalid or not set.");
        }
    }

        @FXML
        private void initialize() {
            if (!UserSession.isLoggedIn()) {
                System.out.println("No user is logged in.");
                // Redirect to login screen or show error
            } else {
                // Load user-specific data
            }
        }



    @FXML
    private void handleManageClaim(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimManagement.fxml"));
            Parent root = loader.load();

            ClaimManagementController controller = loader.getController();
            controller.initializeData(policyHolderId, userRole); // Pass both ID and role

            Stage stage = (Stage) manageClaimButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewDependents(ActionEvent event) {
        if (!UserSession.isLoggedIn()) {
            showAlert("Error", "No user logged in. Please login first.", Alert.AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewDependentInfo.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Dependents");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
