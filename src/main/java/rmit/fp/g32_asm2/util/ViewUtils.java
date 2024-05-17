package rmit.fp.g32_asm2.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.controller.common.UserInfoController;
import rmit.fp.g32_asm2.model.User;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public class ViewUtils {
    private static ViewUtils instance;

    private final Stack<Pane> panels = new Stack<>();
    private final Stack<Pane> removedPanels = new Stack<>();
    private Stage primaryStage;

    private ViewUtils() {}

    public static synchronized ViewUtils getInstance() {
        if (instance == null) {
            instance = new ViewUtils();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void renderView(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane pane = loader.load();
            primaryStage.setTitle(title);
            panels.push(pane);
            removedPanels.clear(); // Clear forward history when a new view is rendered
            updateStage(pane);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void back() {
        if (panels.size() > 1) {
            Pane paneToRemove = panels.pop();
            removedPanels.push(paneToRemove);
            updateStage(panels.peek());
        }
    }

    public void forward() {
        if (!removedPanels.isEmpty()) {
            Pane paneToAdd = removedPanels.pop();
            panels.push(paneToAdd);
            updateStage(paneToAdd);
        }
    }

    public void clearHistory() {
        panels.clear();
        removedPanels.clear();
    }

    private void updateStage(Pane pane) {
        if (primaryStage != null) {
            primaryStage.setScene(new Scene(pane));
            primaryStage.show();
        } else {
            System.err.println("Primary stage is not set.");
        }
    }

    public boolean showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Re-enter your credentials to confirm deletion");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        alert.getDialogPane().setContent(passwordField);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String enteredPassword = passwordField.getText();
            User user = AuthContext.getCurrentUser();
            if (Objects.equals(user.getHashPassword(), HashUtils.hashPassword(enteredPassword))) {
                System.out.println(user + " can delete");
                return true;
            }
        }
        return false;
    }

    public static void loadUserInfoView(Class<?> clazz, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(clazz.getResource("/view/common/user-info-view.fxml")));
            Parent root = loader.load();
            UserInfoController controller = loader.getController();
            if (user != null)
                controller.setUser(user);


            Stage stage = new Stage();
            stage.setTitle("User Info");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
