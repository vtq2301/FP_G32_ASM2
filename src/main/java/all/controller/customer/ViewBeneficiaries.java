package all.controller.customer;

import all.auth.ActionLogger;
import all.auth.BeneficiaryService;
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

public class ViewBeneficiaries {
    @FXML private TableView<User> availableBeneficiariesTable;
    @FXML private TableView<User> selectedBeneficiariesTable;
    @FXML private TextField policyOwnerTextField;
    @FXML private Button backButton;

    private ObservableList<User> availableBeneficiaries = FXCollections.observableArrayList();
    private ObservableList<User> selectedBeneficiaries = FXCollections.observableArrayList();
    private BeneficiaryService dbService = new BeneficiaryService();

    private Set<User> beneficiariesToAdd = new HashSet<>();
    private Set<User> beneficiariesToRemove = new HashSet<>();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (UserSession.isLoggedIn()) {
                String policyOwnerId = UserSession.getCurrentUser().getId();
                loadBeneficiaries(policyOwnerId);
            } else {
                showAlert("Login Error", "No user is logged in.", Alert.AlertType.ERROR);
                closeWindow();
            }
        });
    }

    private void loadBeneficiaries(String policyOwnerId) {
        availableBeneficiaries.setAll(dbService.fetchAvailableBeneficiaries(policyOwnerId));
        selectedBeneficiaries.setAll(dbService.fetchSelectedBeneficiaries(policyOwnerId));
        availableBeneficiariesTable.setItems(availableBeneficiaries);
        selectedBeneficiariesTable.setItems(selectedBeneficiaries);
    }

    @FXML
    private void handleAddBeneficiary() {
        User selected = availableBeneficiariesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedBeneficiaries.add(selected);
            availableBeneficiaries.remove(selected);
            beneficiariesToAdd.add(selected);
            beneficiariesToRemove.remove(selected);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Add Beneficiary", "Added beneficiary: " + selected.getFullName(), null);
        }
    }

    @FXML
    private void handleRemoveBeneficiary() {
        User selected = selectedBeneficiariesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableBeneficiaries.add(selected);
            selectedBeneficiaries.remove(selected);
            beneficiariesToRemove.add(selected);
            beneficiariesToAdd.remove(selected);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Remove Beneficiary", "Removed beneficiary: " + selected.getFullName(), null);
        }
    }

    @FXML
    private void handleSave() {
        List<User> beneficiariesToSave = new ArrayList<>(selectedBeneficiaries);
        List<User> beneficiariesToRemoveList = new ArrayList<>(beneficiariesToRemove);

        String policyOwnerId = UserSession.getCurrentUser().getId();
        boolean success = dbService.saveAndRemoveBeneficiaries(beneficiariesToSave, beneficiariesToRemoveList, policyOwnerId);

        if (success) {
            ActionLogger actionLogger = new ActionLogger();

            for (User user : beneficiariesToAdd) {
                actionLogger.logAction(policyOwnerId, "Add Beneficiary", "Added beneficiary: " + user.getFullName(), null);
            }

            for (User user : beneficiariesToRemove) {
                actionLogger.logAction(policyOwnerId, "Remove Beneficiary", "Removed beneficiary: " + user.getFullName(), null);
            }

            beneficiariesToAdd.clear();
            beneficiariesToRemove.clear();

            showAlert("Success", "Beneficiaries saved successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to save beneficiaries.", Alert.AlertType.ERROR);
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
        if (policyOwnerTextField != null && policyOwnerTextField.getScene() != null) {
            Stage stage = (Stage) policyOwnerTextField.getScene().getWindow();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyOwnerScreen.fxml"));
            Parent parent = fxmlLoader.load();
            PolicyOwner controller = fxmlLoader.getController();

            User currentUser = UserSession.getCurrentUser();
            controller.loadData(currentUser);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow(); // Get the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
