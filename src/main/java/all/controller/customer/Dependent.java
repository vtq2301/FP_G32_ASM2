/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
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

public class Dependent {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Button manageClaimButton;
    @FXML private Button updateUserInfoButton;
    @FXML private Button changePasswordButton;
    private String policyHolderId;
    private String userRole;

    public void loadData(User user) {
        nameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        phoneField.setText(user.getPhoneNumber());
        this.policyHolderId = user.getId();
        this.userRole = user.getRole();

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
            controller.initializeData(policyHolderId, userRole);

            Stage stage = (Stage) manageClaimButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
            Stage stage = (Stage) nameField.getScene().getWindow();
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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
