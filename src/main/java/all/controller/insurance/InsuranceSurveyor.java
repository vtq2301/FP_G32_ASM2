package all.controller.insurance;

import all.controller.UserSession;
import all.model.customer.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
}
