package rmit.fp.g32_asm2.controller.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.auth.AuthService;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.util.ViewUtils;
import rmit.fp.g32_asm2.view.Routes;

public class LoginController {
    @FXML private Label messageLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;


    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        messageLabel.setText("Logging in...");
        boolean loggedIn = AuthService.login(username, password);
        if (!loggedIn) {
            messageLabel.setText("Invalid username or password. Please try again!");
            return;
        }
        User currentUser = AuthContext.getCurrentUser();
        messageLabel.setText("Logged in as " + currentUser.getClass().getSimpleName());
        String path = Routes.getInstance().getRoute(currentUser.getClass().getSimpleName(), "Dashboard");
        ViewUtils.getInstance().renderView(path, "Dashboard");
    }
    private void navigateTo(int roleId) {

    }

}
