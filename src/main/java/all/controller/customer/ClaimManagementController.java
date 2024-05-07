package all.controller.customer;

import all.auth.ActionLogger;
import all.auth.ClaimDatabase;
import all.model.customer.ClaimManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class ClaimManagementController {
    @FXML private TableView<ClaimManagement> claimsTable;
    @FXML private TableColumn<ClaimManagement, Integer> idColumn;
    @FXML private TableColumn<ClaimManagement, String> customerIdColumn;
    @FXML private TableColumn<ClaimManagement, String> descriptionColumn;
    @FXML private TableColumn<ClaimManagement, String> statusColumn;

    private final ClaimDatabase dbService = new ClaimDatabase();
    private final ObservableList<ClaimManagement> claimData = FXCollections.observableArrayList();
    private String policyHolderId;

    @FXML
    public void initialize() {
        setupColumnFactories();
    }

    // Updated to accept a String type for policyHolderId
    public void initializeData(String policyHolderId) {
        this.policyHolderId = policyHolderId;
        loadData();
    }

    // Updated loadData to not require any parameters
    private void loadData() {
        claimData.setAll(dbService.getClaimsForPolicyHolder(policyHolderId));
        claimsTable.setItems(claimData);
    }

    private void setupColumnFactories() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    public void handleAddNewClaim() {
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
    public void handleDeleteClaim() {
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            dbService.deleteClaim(selectedClaim.getId());
            loadData(); // Load the data for the current policyHolderId
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
    public void handleUpdateClaim() {
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Claim Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a claim to update.");
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
            loadData(); // Load the data for the current policyHolderId
        });

    }
}
