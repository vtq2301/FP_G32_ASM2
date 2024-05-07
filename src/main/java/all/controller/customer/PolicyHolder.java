package all.controller.customer;

import all.model.customer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private String policyHolderId;  // Change type from int to String

    // This method should receive the `User` object and initialize `policyHolderId`
    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456");
        this.policyHolderId = user.getUsername();  // Use the username as is if it's formatted properly

        // Assuming the username is already in the correct format and does not require extraction of numbers
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
            controller.initializeData(policyHolderId);  // Updated to pass String ID

            Stage stage = (Stage) manageClaimButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
