package all.controller.customer;

import all.auth.DependencyService;
import all.controller.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

public class ChangePasswordController {

    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleSavePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New Password and Confirm Password do not match.", Alert.AlertType.ERROR);
            return;
        }

        DependencyService dbService = new DependencyService();
        String currentUserId = UserSession.getCurrentUser().getId();

        if (dbService.verifyOldPassword(currentUserId, oldPassword)) {
            dbService.updatePassword(currentUserId, newPassword);
            showAlert("Success", "Password updated successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Old Password is incorrect.", Alert.AlertType.ERROR);
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
