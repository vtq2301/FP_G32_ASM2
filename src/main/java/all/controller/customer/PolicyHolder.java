package all.controller.customer;

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

public class PolicyHolder {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField policyNumberField;
    @FXML private Button manageClaimButton;
    private String policyHolderId;
    private String userRole;
    @FXML private Button changeDependentInfoButton;
    @FXML private Button updateUserInfoButton;
    @FXML private Button changePasswordButton;

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
    private void handleManageClaim(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimManagement.fxml"));
            Parent root = loader.load();

            ClaimManagementController controller = loader.getController();
            controller.initializeData(policyHolderId, userRole);

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

            Stage stage = (Stage) manageClaimButton.getScene().getWindow();
            stage.setTitle("View Dependents");
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

    @FXML
    private void handleChangeDependentInfo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DependentInfo.fxml"));
            Parent root = loader.load();

            DependentInfoController controller = loader.getController();
            controller.initializeData(policyHolderId);

            Stage stage = (Stage) changeDependentInfoButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dependent information screen.", Alert.AlertType.ERROR);
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
}
