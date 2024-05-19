/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller.insurance;

import all.controller.UserSession;
import all.controller.customer.UpdateUserInfoController;
import all.model.customer.User;
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

public class InsuranceSurveyor {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField surveyorIdField;
    @FXML private Button manageInsuranceButton;
    @FXML private Button logoutButton;
    @FXML private Button updateUserInfoButton;
    @FXML private Button changePasswordButton;
    @FXML
    public void initialize() {
    }

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        surveyorIdField.setText("SURV123456"); // Example Surveyor ID
    }

    @FXML
    private void handleManageInsurance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InsuranceManagementSurveyor.fxml"));
            Parent root = loader.load();

            InsuranceManagementSurveyor controller = loader.getController();
            controller.loadClaims();

            Stage stage = (Stage) manageInsuranceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
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
