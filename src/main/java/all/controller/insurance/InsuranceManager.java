package all.controller.insurance;

import all.model.customer.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InsuranceManager {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField managerIdField;
    @FXML private Button manageInsuranceButton;

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        managerIdField.setText("MANAGER123456");
    }

    @FXML
    private void handleManageInsurance() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InsuranceManagement.fxml"));
            Scene scene = new Scene(fxmlLoader.load());


            Stage stage = (Stage) manageInsuranceButton.getScene().getWindow();


            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
