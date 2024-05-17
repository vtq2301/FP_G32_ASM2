package all.controller.customer;

import all.auth.ActionLogger;
import all.auth.ClaimDatabase;
import all.model.customer.ClaimManagement;
import all.model.customer.User;
import all.controller.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class ClaimManagementController {
    @FXML private TableView<ClaimManagement> claimsTable;
    @FXML private TableColumn<ClaimManagement, Integer> idColumn;
    @FXML private TableColumn<ClaimManagement, String> customerIdColumn;
    @FXML private TableColumn<ClaimManagement, String> statusColumn;
    @FXML private Button addButton, deleteButton, updateButton;
    @FXML private Button backButton;

    private final ClaimDatabase dbService = new ClaimDatabase();
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

        Dialog<ClaimManagement> dialog = new Dialog<>();
        dialog.setTitle("Add New Claim");
        dialog.setHeaderText("Create a New Claim");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        DatePicker claimDatePicker = new DatePicker();
        DatePicker examDatePicker = new DatePicker();
        TextField insuredPersonField = new TextField();
        TextField documentsField = new TextField();
        TextField claimAmountField = new TextField();
        TextField receiverBankingInfoField = new TextField();

        grid.add(new Label("Claim Date:"), 0, 0);
        grid.add(claimDatePicker, 1, 0);
        grid.add(new Label("Exam Date:"), 0, 1);
        grid.add(examDatePicker, 1, 1);
        grid.add(new Label("Insured Person:"), 0, 2);
        grid.add(insuredPersonField, 1, 2);
        grid.add(new Label("Documents:"), 0, 3);
        grid.add(documentsField, 1, 3);
        grid.add(new Label("Claim Amount:"), 0, 4);
        grid.add(claimAmountField, 1, 4);
        grid.add(new Label("Receiver Banking Info:"), 0, 5);
        grid.add(receiverBankingInfoField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                LocalDate claimDate = claimDatePicker.getValue();
                LocalDate examDate = examDatePicker.getValue();
                String insuredPerson = insuredPersonField.getText();
                String[] documents = documentsField.getText().split(",");
                double claimAmount;
                try {
                    claimAmount = Double.parseDouble(claimAmountField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Claim amount must be a number.");
                    return null;
                }
                String receiverBankingInfo = receiverBankingInfoField.getText();

                return new ClaimManagement(null, policyHolderId, Date.valueOf(claimDate), insuredPerson, Date.valueOf(examDate), documents, claimAmount, receiverBankingInfo, "New");
            }
            return null;
        });

        Optional<ClaimManagement> result = dialog.showAndWait();
        result.ifPresent(claim -> {
            if (policyHolderId == null || policyHolderId.isEmpty()) {
                System.err.println("Error: PolicyHolder ID is invalid or not set.");
                return;
            }

            dbService.addClaim(claim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolderId, "Add Claim", "Added a new claim with insured person: " + claim.getInsuredPerson(), claim.getId());

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

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolderId, "Delete Claim", "Deleted claim with ID: " + selectedClaim.getId(), selectedClaim.getId());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Claim Selected");
            alert.setContentText("Please select a claim in the table.");
            alert.showAndWait();
        }
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
            alert.setContentText("Please select a claim to update.");
            alert.showAndWait();
            return;
        }

        Dialog<ClaimManagement> dialog = new Dialog<>();
        dialog.setTitle("Update Claim");
        dialog.setHeaderText("Edit the Claim");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        DatePicker claimDatePicker = new DatePicker(convertToLocalDate(selectedClaim.getClaimDate()));
        DatePicker examDatePicker = new DatePicker(convertToLocalDate(selectedClaim.getExamDate()));
        TextField insuredPersonField = new TextField(selectedClaim.getInsuredPerson());
        TextField documentsField = new TextField(String.join(",", selectedClaim.getDocuments()));
        TextField claimAmountField = new TextField(String.valueOf(selectedClaim.getClaimAmount()));
        TextField receiverBankingInfoField = new TextField(selectedClaim.getReceiverBankingInfo());

        grid.add(new Label("Claim Date:"), 0, 0);
        grid.add(claimDatePicker, 1, 0);
        grid.add(new Label("Exam Date:"), 0, 1);
        grid.add(examDatePicker, 1, 1);
        grid.add(new Label("Insured Person:"), 0, 2);
        grid.add(insuredPersonField, 1, 2);
        grid.add(new Label("Documents:"), 0, 3);
        grid.add(documentsField, 1, 3);
        grid.add(new Label("Claim Amount:"), 0, 4);
        grid.add(claimAmountField, 1, 4);
        grid.add(new Label("Receiver Banking Info:"), 0, 5);
        grid.add(receiverBankingInfoField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                LocalDate claimDate = claimDatePicker.getValue();
                LocalDate examDate = examDatePicker.getValue();
                String insuredPerson = insuredPersonField.getText();
                String[] documents = documentsField.getText().split(",");
                double claimAmount;
                try {
                    claimAmount = Double.parseDouble(claimAmountField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Claim amount must be a number.");
                    return null;
                }
                String receiverBankingInfo = receiverBankingInfoField.getText();

                selectedClaim.setClaimDate(Date.valueOf(claimDate));
                selectedClaim.setExamDate(Date.valueOf(examDate));
                selectedClaim.setInsuredPerson(insuredPerson);
                selectedClaim.setDocuments(documents);
                selectedClaim.setClaimAmount(claimAmount);
                selectedClaim.setReceiverBankingInfo(receiverBankingInfo);

                return selectedClaim;
            }
            return null;
        });

        Optional<ClaimManagement> result = dialog.showAndWait();
        result.ifPresent(claim -> {
            dbService.updateClaim(claim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(policyHolderId, "Update Claim", "Updated claim with insured person: " + claim.getInsuredPerson(), claim.getId());

            loadData();
        });
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader;
            if ("dependent".equalsIgnoreCase(userRole)) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/DependentScreen.fxml"));
            } else {
                fxmlLoader = new FXMLLoader(getClass().getResource("/PolicyHolderScreen.fxml"));
            }
            Parent parent = fxmlLoader.load();
            User currentUser = UserSession.getCurrentUser();

            if ("dependent".equalsIgnoreCase(userRole)) {
                Dependent controller = fxmlLoader.getController();
                controller.loadData(currentUser);
            } else {
                PolicyHolder controller = fxmlLoader.getController();
                controller.loadData(currentUser);
            }

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public LocalDate convertToLocalDate(java.sql.Date sqlDate) {
        return sqlDate.toLocalDate();
    }

    public LocalDate convertToLocalDate(java.util.Date utilDate) {
        return new java.sql.Date(utilDate.getTime()).toLocalDate();
    }
}
