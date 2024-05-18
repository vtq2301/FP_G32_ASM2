package all.controller.customer;

import all.auth.DependencyService;
import all.auth.ActionLogger;
import all.controller.UserSession;
import all.model.customer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePasswordController {

    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button backButton;

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

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(currentUserId, "Change Password", "Password changed for user: " + currentUserId, null);

            showAlert("Success", "Password updated successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Old Password is incorrect.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader;
            User currentUser = UserSession.getCurrentUser();
            if ("dependent".equalsIgnoreCase(currentUser.getRole())) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/DependentScreen.fxml"));
                Parent parent = fxmlLoader.load();
                Dependent controller = fxmlLoader.getController();
                controller.loadData(currentUser);
                Scene scene = new Scene(parent);
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyHolderScreen.fxml"));
                Parent parent = fxmlLoader.load();
                PolicyHolder controller = fxmlLoader.getController();
                controller.loadData(currentUser);
                Scene scene = new Scene(parent);
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the previous screen.", Alert.AlertType.ERROR);
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
