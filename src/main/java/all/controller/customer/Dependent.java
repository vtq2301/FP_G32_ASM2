package all.controller.customer;

import all.model.customer.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class Dependent {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    public void loadData(User user) {
        nameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        addressField.setText(user.getAddress());
        phoneField.setText(user.getPhoneNumber());
    }
}
