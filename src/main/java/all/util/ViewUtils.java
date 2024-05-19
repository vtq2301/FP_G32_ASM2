package all.util;

import all.controller.UserSession;
import all.model.customer.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;

import java.util.Objects;
import java.util.Optional;

public class ViewUtils {

    /**
     * Displays an error message in a dialog box.
     *
     * @param message the error message to display
     */
    public static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Re-enter your credentials to confirm deletion");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        alert.getDialogPane().setContent(passwordField);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String enteredPassword = passwordField.getText();
            User user = UserSession.getCurrentUser();
            if (Objects.equals(user.getPassword(), enteredPassword)) {
                System.out.println(user + " can delete");
                return true;
            }
        }
        return false;
    }
}
