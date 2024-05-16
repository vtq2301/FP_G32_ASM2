package rmit.fp.g32_asm2.controller.common;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserInfoController {

    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML
    private void initialize() {
        // Initialize fields with user info
    }

    @FXML
    private void updateUserInfo() {
        // Update user info logic
    }
}
