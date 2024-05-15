package all.controller.customer;

import all.auth.DependencyService;
import all.auth.ActionLogger;
import all.model.customer.User;
import all.controller.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class DependentInfoController {
    @FXML private TableView<User> dependentsTable;
    @FXML private Button updateButton;

    private ObservableList<User> dependents = FXCollections.observableArrayList();
    private DependencyService dbService = new DependencyService();

    public void initializeData(String policyHolderId) {
        dependents.setAll(dbService.fetchSelectedDependents(policyHolderId));
        dependentsTable.setItems(dependents);
    }

    @FXML
    private void handleUpdateDependent() {
        User selectedDependent = dependentsTable.getSelectionModel().getSelectedItem();
        if (selectedDependent == null) {
            showAlert("No Dependent Selected", "Please select a dependent to update.", Alert.AlertType.WARNING);
            return;
        }

        Dialog<User> dialog = createUpdateDialog(selectedDependent);
        Optional<User> result = dialog.showAndWait();

        result.ifPresent(user -> {
            dbService.updateDependent(user);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Update Dependent", "Updated dependent information for: " + user.getFullName(), user.getId());
            loadData();
        });
    }

    private Dialog<User> createUpdateDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update Dependent");
        dialog.setHeaderText("Edit Dependent Information");

        // Setup the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField fullNameField = new TextField(user.getFullName());
        TextField addressField = new TextField(user.getAddress());
        TextField phoneNumberField = new TextField(user.getPhoneNumber());

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Address:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new Label("Phone Number:"), 0, 2);
        grid.add(phoneNumberField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new User(user.getId(), user.getRole(), fullNameField.getText(), addressField.getText(), phoneNumberField.getText());
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadData() {
        String policyHolderId = UserSession.getCurrentUser().getId();
        dependents.setAll(dbService.fetchSelectedDependents(policyHolderId));
        dependentsTable.setItems(dependents);
    }
}
