package all.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import all.auth.DatabaseService;
import all.model.Claim;

import java.util.Optional;

public class ClaimsController {
    @FXML
    private TableView<Claim> claimsTable;
    @FXML
    private TableColumn<Claim, Integer> idColumn;
    @FXML
    private TableColumn<Claim, Integer> customerIdColumn;
    @FXML
    private TableColumn<Claim, String> descriptionColumn;
    @FXML
    private TableColumn<Claim, String> statusColumn;

    private final DatabaseService dbService = new DatabaseService();
    private final ObservableList<Claim> claimData = FXCollections.observableArrayList();

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
        ObservableList<Claim> claims = FXCollections.observableArrayList(dbService.getAllClaims());
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
            Claim newClaim = new Claim(null, 1, description, "New"); // Assume customerID is 1 for simplicity
            dbService.addClaim(newClaim);
            loadData();
        });
    }

    @FXML
    public void handleDeleteClaim() {
        Claim selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
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
        Claim selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
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
