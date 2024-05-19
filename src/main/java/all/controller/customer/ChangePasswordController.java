/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller.customer;

import all.auth.DependencyService;
import all.auth.ActionLogger;
import all.controller.insurance.InsuranceManager;
import all.controller.insurance.InsuranceSurveyor;
import all.model.customer.User;
import all.controller.UserSession;
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            User currentUser = UserSession.getCurrentUser();
            String role = currentUser.getRole();
            String fxmlPath = "";

            switch (role) {
                case "PolicyOwner":
                    fxmlPath = "/PolicyOwnerScreen.fxml";
                    break;
                case "Dependent":
                    fxmlPath = "/DependentScreen.fxml";
                    break;
                case "PolicyHolder":
                    fxmlPath = "/PolicyHolderScreen.fxml";
                    break;
                case "InsuranceSurveyor":
                    fxmlPath = "/InsuranceSurveyorScreen.fxml";
                    break;
                case "InsuranceManager":
                    fxmlPath = "/InsuranceManagerScreen.fxml";
                    break;
                default:
                    System.err.println("Unknown role: " + role);
                    showAlert("Error", "Unknown user role: " + role, Alert.AlertType.ERROR);
                    return;
            }

            fxmlLoader.setLocation(getClass().getResource(fxmlPath));
            Parent parent = fxmlLoader.load();

            switch (role) {
                case "PolicyOwner":
                    PolicyOwner policyOwnerController = fxmlLoader.getController();
                    policyOwnerController.loadData(currentUser);
                    break;
                case "Dependent":
                    Dependent dependentController = fxmlLoader.getController();
                    dependentController.loadData(currentUser);
                    break;
                case "PolicyHolder":
                    PolicyHolder policyHolderController = fxmlLoader.getController();
                    policyHolderController.loadData(currentUser);
                    break;
                case "InsuranceSurveyor":
                    InsuranceSurveyor insuranceSurveyorController = fxmlLoader.getController();
                    insuranceSurveyorController.loadData(currentUser);
                    break;
                case "InsuranceManager":
                    InsuranceManager insuranceManagerController = fxmlLoader.getController();
                    insuranceManagerController.loadData(currentUser);
                    break;
            }

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the previous screen.", Alert.AlertType.ERROR);
        } catch (ClassCastException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to cast the controller.", Alert.AlertType.ERROR);
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
