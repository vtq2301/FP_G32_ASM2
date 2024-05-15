package all.controller.customer;

import all.auth.DependencyService;
import all.model.customer.User;
import all.controller.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UpdateUserInfoController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;

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


        showAlert("Success", "User information updated successfully.", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
