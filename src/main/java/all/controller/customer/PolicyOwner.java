package all.controller.customer;

import all.model.customer.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PolicyOwner {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField policyNumberField;

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());  // Assuming address stores email or update accordingly
        contactNumberField.setText(user.getPhoneNumber());
        policyNumberField.setText("POLICY123456"); // Load actual policy data if available
    }
}
