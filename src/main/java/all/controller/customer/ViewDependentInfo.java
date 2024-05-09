package all.controller.customer;

import all.auth.DependencyService;
import all.controller.UserSession;
import all.model.customer.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ViewDependentInfo {
    @FXML private TableView<User> availableDependentsTable;
    @FXML private TableView<User> selectedDependentsTable;
    @FXML private TextField policyHolderTextField;

    private ObservableList<User> availableDependents = FXCollections.observableArrayList();
    private ObservableList<User> selectedDependents = FXCollections.observableArrayList();
    private DependencyService dbService = new DependencyService();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (UserSession.isLoggedIn()) {
                String policyHolderId = UserSession.getCurrentUser().getId();
                loadDependents(policyHolderId);
            } else {
                showAlert("Login Error", "No user is logged in.", Alert.AlertType.ERROR);
                closeWindow();
            }
        });
    }

    private void loadDependents(String policyHolderId) {
        availableDependents.setAll(dbService.fetchAvailableDependents(policyHolderId));
        selectedDependents.setAll(dbService.fetchSelectedDependents(policyHolderId));
        availableDependentsTable.setItems(availableDependents);
        selectedDependentsTable.setItems(selectedDependents);
    }

    @FXML
    private void handleAddDependent() {
        User selected = availableDependentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedDependents.add(selected);
            availableDependents.remove(selected);
        }
    }

    @FXML
    private void handleRemoveDependent() {
        User selected = selectedDependentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableDependents.add(selected);
            selectedDependents.remove(selected);
        }
    }

    @FXML
    private void handleSave() {
        List<User> dependentsToSave = new ArrayList<>(selectedDependentsTable.getItems());
        String policyHolderId = UserSession.getCurrentUser().getId(); // Fetch from the session
        boolean success = dbService.saveDependents(dependentsToSave, policyHolderId);
        if (success) {
            showAlert("Success", "Dependents saved successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to save dependents.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        if (policyHolderTextField != null && policyHolderTextField.getScene() != null) {
            Stage stage = (Stage) policyHolderTextField.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } else {
            System.err.println("UI components are not initialized.");
        }
    }
}
