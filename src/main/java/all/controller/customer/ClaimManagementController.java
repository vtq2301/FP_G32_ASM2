package all.controller.customer;

import all.auth.ActionLogger;
import all.auth.ClaimDatabase;
import all.model.customer.ClaimManagement;
import all.model.customer.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class ClaimManagementController {
    @FXML private TableView<ClaimManagement> claimsTable;
    @FXML private TableColumn<ClaimManagement, Integer> idColumn;
    @FXML private TableColumn<ClaimManagement, String> customerIdColumn;
    @FXML private TableColumn<ClaimManagement, String> descriptionColumn;
    @FXML private TableColumn<ClaimManagement, String> statusColumn;
    @FXML private Button addButton, deleteButton, updateButton;

    private final ClaimDatabase dbService = new     ClaimDatabase();
    private final ObservableList<ClaimManagement> claimData = FXCollections.observableArrayList();
    private String policyHolderId;
    private String userRole;

    @FXML
    public void initialize() {
        setupColumnFactories();
    }


    public void initializeData(String policyHolderId, String userRole) {
        this.policyHolderId = policyHolderId;
        this.userRole = userRole;
        loadData();
        Platform.runLater(this::configureAccessBasedOnRole);
    }

    private void loadData() {
        if ("dependent".equalsIgnoreCase(userRole)) {
            claimData.setAll(dbService.getClaimsForDependent(policyHolderId));
        } else {
            claimData.setAll(dbService.getClaimsForPolicyHolder(policyHolderId));
        }
        claimsTable.setItems(claimData);
    }



    private void setupColumnFactories() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void configureAccessBasedOnRole() {
        boolean isDependent = "dependent".equalsIgnoreCase(userRole);
        addButton.setDisable(isDependent);
        deleteButton.setDisable(isDependent);
        updateButton.setDisable(isDependent);
    }

    @FXML
    private void handleAddNewClaim() {
        if ("dependent".equalsIgnoreCase(userRole)) {
            showAlert("Access Denied", "You do not have permission to add claims.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Claim");
        dialog.setHeaderText("Create a New Claim");
        dialog.setContentText("Please enter the claim description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            if (policyHolderId == null || policyHolderId.isEmpty()) {
                System.err.println("Error: PolicyHolder ID is invalid or not set.");
                return;
            }

            ClaimManagement newClaim = new ClaimManagement(null, this.policyHolderId, description, "New");
            dbService.addClaim(newClaim);


            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolderId, "Add Claim", "Added a new claim with description: " + description, newClaim.getId());


            loadData();
        });
    }


    @FXML
    private void handleDeleteClaim() {
        if ("dependent".equalsIgnoreCase(userRole)) {
            showAlert("Access Denied", "You do not have permission to delete claims.");
            return;
        }

        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            dbService.deleteClaim(selectedClaim.getId());
            loadData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Claim Selected");
            alert.setContentText("Please select a claim in the table.");
            alert.showAndWait();
        }
        ActionLogger actionLogger = new ActionLogger();
        actionLogger.logAction(policyHolderId, "Delete Claim", "Deleted claim with ID: " + selectedClaim.getId(), selectedClaim.getId());
    }


    @FXML
    private void handleUpdateClaim() {
        if ("dependent".equalsIgnoreCase(userRole)) {
            showAlert("Access Denied", "You do not have permission to update claims.");
            return;
        }

        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Claim Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Pleas e select a claim to update.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedClaim.getDescription());
        dialog.setTitle("Update Claim");
        dialog.setHeaderText("Edit the Claim Description");
        dialog.setContentText("Enter the new description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            selectedClaim.setDescription(description);
            dbService.updateClaim(selectedClaim);
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolderId, "Update Claim", "Updated claim with new description: " + description, selectedClaim.getId());
            loadData();
        });
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
