package all.controller.customer;

import all.model.customer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Dependent {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Button manageClaimButton;
    private String policyHolderId;
    private String userRole; // Added to keep track of the user's role

    public void loadData(User user) {
        nameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        phoneField.setText(user.getPhoneNumber());
        this.policyHolderId = user.getId();
        this.userRole = user.getRole(); // Store user role

        if (this.policyHolderId == null || this.policyHolderId.isEmpty()) {
            System.err.println("Error: PolicyHolder ID is invalid or not set.");
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
}
