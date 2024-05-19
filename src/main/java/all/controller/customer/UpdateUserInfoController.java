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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateUserInfoController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private Button backButton;

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

        System.out.println("Updating user: " + currentUser);

        DependencyService dbService = new DependencyService();
        if (dbService.updatePolicyHolder(currentUser)) {
            System.out.println("User updated successfully in the database.");
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(currentUser.getId(), "Update User Info", "Updated user information for: " + currentUser.getFullName(), null);

            showAlert("Success", "User information updated successfully.", Alert.AlertType.INFORMATION);
        } else {
            System.err.println("Failed to update user in the database.");
            showAlert("Error", "Failed to update user information.", Alert.AlertType.ERROR);
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
