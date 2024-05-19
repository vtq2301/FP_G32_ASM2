package all.controller.customer;

import all.auth.DependencyService;
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

public class UpdateUserInfoController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private Button backButton;

    private User currentUser;

    public void loadUserData(User user) {
        currentUser = user;
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
    }

    @FXML
    private void handleSaveUserInfo() {
        currentUser.setFullName(fullNameField.getText());
        currentUser.setAddress(emailField.getText());
        currentUser.setPhoneNumber(contactNumberField.getText());

        DependencyService dbService = new DependencyService();
        dbService.updatePolicyHolder(currentUser);

        ActionLogger actionLogger = new ActionLogger();
        actionLogger.logAction(currentUser.getId(), "Update User Info", "Updated user information for: " + currentUser.getFullName(), null);

        showAlert("Success", "User information updated successfully.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            User currentUser = UserSession.getCurrentUser();
            String role = currentUser.getRole();
            switch (role) {
                case "PolicyOwner":
                    fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyOwnerScreen.fxml"));
                    break;
                case "Dependent":
                    fxmlLoader = new FXMLLoader(getClass().getResource("/DependentScreen.fxml"));
                    break;
                case "PolicyHolder":
                    fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyHolderScreen.fxml"));
                    break;
                default:
                    // Handle unknown roles if necessary
                    break;
            }
            Parent parent = fxmlLoader.load();
            if (role.equals("PolicyOwner") || role.equals("PolicyHolder")) {
                PolicyOwner controller = fxmlLoader.getController();
                controller.loadData(currentUser);
            } else if (role.equals("Dependent")) {
                Dependent controller = fxmlLoader.getController();
                controller.loadData(currentUser);
            }

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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