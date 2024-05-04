package all.controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import all.auth.ClaimDatabase;

import java.util.Optional;

public class ClaimManagement {
    @FXML
    private TableView<all.model.customer.ClaimManagement> claimsTable;
    @FXML
    private TableColumn<all.model.customer.ClaimManagement, Integer> idColumn;
    @FXML
    private TableColumn<all.model.customer.ClaimManagement, Integer> customerIdColumn;
    @FXML
    private TableColumn<all.model.customer.ClaimManagement, String> descriptionColumn;
    @FXML
    private TableColumn<all.model.customer.ClaimManagement, String> statusColumn;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;


    private final ClaimDatabase dbService = new ClaimDatabase();
    private final ObservableList<all.model.customer.ClaimManagement> claimData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupColumnFactories();
        loadData();
    }

    private void setupColumnFactories() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() {
        ObservableList<all.model.customer.ClaimManagement> claims = FXCollections.observableArrayList(dbService.getAllClaims());
        claimsTable.setItems(claims);
    }

    @FXML
    public void handleAddNewClaim() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Claim");
        dialog.setHeaderText("Create a New Claim");
        dialog.setContentText("Please enter the claim description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            all.model.customer.ClaimManagement newClaim = new all.model.customer.ClaimManagement(null, 1, description, "New"); // Assume customerID is 1 for simplicity
            dbService.addClaim(newClaim);
            loadData();
        });
    }

    @FXML
    public void handleDeleteClaim() {
        all.model.customer.ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
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
    }
    @FXML
    public void handleUpdateClaim() {
        all.model.customer.ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Claim Selected");
            alert.setHeaderText("Update Error");
            alert.setContentText("Please select a claim to update.");
            alert.showAndWait();
            return;
        }

        // Example dialog for editing the claim; in practice, you might want to use a more complex form
        TextInputDialog dialog = new TextInputDialog(selectedClaim.getDescription());
        dialog.setTitle("Update Claim");
        dialog.setHeaderText("Edit the Claim Description");
        dialog.setContentText("Enter the new description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            selectedClaim.setDescription(description); // Updating the description
            dbService.updateClaim(selectedClaim);
            loadData(); // Refresh the table view after the update
        });
    }


}
