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

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());  // Assuming address stores email or update accordingly
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456"); // Load actual policy data if available
    }
    @FXML
    private void handleManageClaim(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimManagement.fxml")); // Make sure path is correct
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Claim Management");
            stage.setScene(new Scene(root));
            stage.show();
            // Optionally, close the current window
            ((Stage) manageClaimButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (maybe show a dialog with the error)
        }
    }

}
