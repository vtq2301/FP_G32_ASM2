package all.controller.customer;

import all.auth.ActionLogger;
import all.model.customer.User;
import all.controller.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PolicyOwner {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField policyNumberField;
    @FXML private Button manageClaimButton;
    @FXML private Button manageBeneficiariesButton;
    @FXML private Button updateUserInfoButton;
    @FXML private Button changePasswordButton;
    @FXML private Button logoutButton;
    @FXML private Button manageBeneficiaryClaimsButton;

    private String policyHolderId;
    private String userRole;

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456");
        this.policyHolderId = user.getId();
        this.userRole = user.getRole();

        if (this.policyHolderId == null || this.policyHolderId.isEmpty()) {
            System.err.println("Error: PolicyHolder ID is invalid or not set.");
        }
    }

    @FXML
    private void initialize() {
        if (!UserSession.isLoggedIn()) {
            System.out.println("No user is logged in.");
        }
    }

    @FXML
    private void handleManageBeneficiaryClaims(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimManagement.fxml"));
            Parent root = loader.load();

            ClaimManagementController controller = loader.getController();
            controller.initializeDataForBeneficiaries(UserSession.getCurrentUser().getId()); // Assuming UserSession holds the current user

            Stage stage = (Stage) manageBeneficiaryClaimsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleManageBeneficiaries(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewBeneficiaries.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) manageBeneficiariesButton.getScene().getWindow();
            stage.setTitle("Manage Beneficiaries");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateUserInfo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUserInfo.fxml"));
            Parent root = loader.load();

            UpdateUserInfoController controller = loader.getController();
            controller.loadUserData(UserSession.getCurrentUser());

            Stage stage = (Stage) updateUserInfoButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load update user information screen.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load change password screen.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
