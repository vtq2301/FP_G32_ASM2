/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller.customer;

import all.auth.ActionLogger;
import all.auth.DependencyService;
import all.controller.UserSession;
import all.model.customer.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewDependentInfo {
    @FXML private TableView<User> availableDependentsTable;
    @FXML private TableView<User> selectedDependentsTable;
    @FXML private TextField policyHolderTextField;
    @FXML private Button backButton;

    private ObservableList<User> availableDependents = FXCollections.observableArrayList();
    private ObservableList<User> selectedDependents = FXCollections.observableArrayList();
    private DependencyService dbService = new DependencyService();

    private Set<User> dependentsToAdd = new HashSet<>();
    private Set<User> dependentsToRemove = new HashSet<>();

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
            dependentsToAdd.add(selected);
            dependentsToRemove.remove(selected);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Add Dependent", "Added dependent: " + selected.getFullName(), null);
        }
    }

    @FXML
    private void handleRemoveDependent() {
        User selected = selectedDependentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableDependents.add(selected);
            selectedDependents.remove(selected);
            dependentsToRemove.add(selected);
            dependentsToAdd.remove(selected);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Remove Dependent", "Removed dependent: " + selected.getFullName(), null);
        }
    }

    @FXML
    private void handleSave() {
        List<User> dependentsToSave = new ArrayList<>(selectedDependents);
        List<User> dependentsToRemoveList = new ArrayList<>(dependentsToRemove);

        String policyHolderId = UserSession.getCurrentUser().getId();
        boolean success = dbService.saveAndRemoveDependents(dependentsToSave, dependentsToRemoveList, policyHolderId);

        if (success) {
            ActionLogger actionLogger = new ActionLogger();

            for (User user : dependentsToAdd) {
                actionLogger.logAction(policyHolderId, "Add Dependent", "Added dependent: " + user.getFullName(), null);
            }

            for (User user : dependentsToRemove) {
                actionLogger.logAction(policyHolderId, "Remove Dependent", "Removed dependent: " + user.getFullName(), null);
            }

            dependentsToAdd.clear();
            dependentsToRemove.clear();

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

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyHolderScreen.fxml"));
            Parent parent = fxmlLoader.load();
            PolicyHolder controller = fxmlLoader.getController();

            User currentUser = UserSession.getCurrentUser();
            controller.loadData(currentUser);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
