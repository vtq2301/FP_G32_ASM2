package all.controller.policyOwner;

import all.controller.ClaimIDGenerator;
import all.controller.UserSession;
import all.db.ConnectionPool;
import all.model.customer.ClaimManagement;
import all.service.ClaimService;
import all.service.ImageUtils;
import all.util.ValidationUtils;
import all.util.ViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClaimsViewController {

    public AnchorPane claimsView;
    @FXML
    private TextField searchField;
    @FXML
    private FilteredTableView<ClaimManagement> claimsTable;
    @FXML
    private FilteredTableColumn<ClaimManagement, String> claimIdColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, String> customerIdColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, String> insuredPersonColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, Double> claimAmountColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, String> statusColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, Date> claimDateColumn;
    @FXML
    private FilteredTableColumn<ClaimManagement, Date> examDateColumn;
    @FXML
    private Pagination pagination;
    @FXML
    private Label claimedAmount;

    private final ClaimService claimService = new ClaimService();
    private ObservableList<ClaimManagement> claimList = FXCollections.observableArrayList();
    private FilteredList<ClaimManagement> filteredData;
    private final int ROWS_PER_PAGE = 10;

    @FXML
    public void initialize() {
        initializeTableColumns();
        initializeClaimList();
        initializeSearchField();
        initializePagination();
        updateClaimedAmount();
        addTableRowDoubleClickHandler();
    }

    private void initializeTableColumns() {
        claimIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        insuredPersonColumn.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        claimAmountColumn.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        claimDateColumn.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        examDateColumn.setCellValueFactory(new PropertyValueFactory<>("examDate"));

        popupFilters(claimIdColumn, customerIdColumn, insuredPersonColumn, statusColumn);
    }

    @SafeVarargs
    private void popupFilters(FilteredTableColumn<ClaimManagement, String>... columns) {
        for (FilteredTableColumn<ClaimManagement, String> column : columns) {
            PopupFilter<ClaimManagement, String> popupFilter = new PopupStringFilter<>(column);
            column.setOnFilterAction(e -> popupFilter.showPopup());
        }
    }

    private void initializeClaimList() {
        String id = UserSession.getCurrentUser().getId();
        claimList.setAll(claimService.findAllBeneficiaryClaims(id));
        filteredData = new FilteredList<>(claimList, p -> true);
    }

    private void initializeSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(claim -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return claim.getId().toLowerCase().contains(lowerCaseFilter) ||
                        claim.getCustomerId().toLowerCase().contains(lowerCaseFilter) ||
                        claim.getInsuredPerson().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(claim.getClaimAmount()).toLowerCase().contains(lowerCaseFilter) ||
                        claim.getStatus().toLowerCase().contains(lowerCaseFilter) ||
                        claim.getClaimDate().toString().toLowerCase().contains(lowerCaseFilter) ||
                        claim.getExamDate().toString().toLowerCase().contains(lowerCaseFilter);
            });
            updateTableView(0);
        });
    }

    private void initializePagination() {
        SortedList<ClaimManagement> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(claimsTable.comparatorProperty());

        pagination.setPageCount((int) Math.ceil((double) filteredData.size() / ROWS_PER_PAGE));
        pagination.currentPageIndexProperty().addListener((observable, oldIndex, newIndex) -> updateTableView(newIndex.intValue()));

        updateTableView(0);
    }

    private void updateTableView(int index) {
        int fromIndex = index * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredData.size());
        claimsTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
    }

    private void updateClaimedAmount() {
        double totalClaimedAmount = claimList.stream()
                .filter(claim -> "Accepted".equalsIgnoreCase(claim.getStatus()))
                .mapToDouble(ClaimManagement::getClaimAmount)
                .sum();
        claimedAmount.setText(String.format("$%.2f", totalClaimedAmount));
    }

    @FXML
    public void addClaim() throws SQLException {
        ClaimManagement newClaim = showClaimDialog(null);
        if (newClaim != null) {
            claimService.addClaim(newClaim);
            claimList.add(newClaim);
            filteredData.setPredicate(null); // Reset filter
            updateTableView(pagination.getCurrentPageIndex());
        }
    }

    @FXML
    public void updateClaim() throws SQLException {
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            ClaimManagement updatedClaim = showClaimDialog(selectedClaim);
            if (updatedClaim != null) {
                claimService.updateClaim(updatedClaim);
                claimsTable.refresh();
            }
        }
    }

    @FXML
    public void removeClaim() {
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            claimService.deleteClaim(selectedClaim.getId());
            claimList.remove(selectedClaim);
            filteredData.setPredicate(null); // Reset filter
            updateTableView(pagination.getCurrentPageIndex());
        }
    }

    private ClaimManagement showClaimDialog(ClaimManagement claim) throws SQLException {
        Dialog<ClaimManagement> dialog = new Dialog<>();
        dialog.setTitle(claim == null ? "Add Claim" : "Update Claim");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.setText(claim != null ? claim.getId() : ClaimIDGenerator.generateUniqueClaimID(ConnectionPool.getInstance().getConnection()));
        idField.setDisable(true);

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");
        customerIdField.setText(claim != null ? claim.getCustomerId() : "");

        TextField insuredPersonField = new TextField();
        insuredPersonField.setPromptText("Insured Person");
        insuredPersonField.setText(claim != null ? claim.getInsuredPerson() : "");

        TextField bankField = new TextField();
        bankField.setPromptText("Receiver Banking Info");
        bankField.setText(claim != null ? claim.getReceiverBankingInfo() : "");

        TextField claimAmountField = new TextField();
        claimAmountField.setPromptText("Claim Amount");
        claimAmountField.setText(claim != null ? String.valueOf(claim.getClaimAmount()) : "");

        TextField statusField = new TextField();
        statusField.setPromptText("Status");
        statusField.setText(claim != null ? claim.getStatus() : "New");
        statusField.setDisable(true);

        DatePicker claimDatePicker = new DatePicker();
        claimDatePicker.setPromptText("Claim Date");
        claimDatePicker.setValue(claim != null ? new java.sql.Date(claim.getClaimDate().getTime()).toLocalDate() : null);

        DatePicker examDatePicker = new DatePicker();
        examDatePicker.setPromptText("Exam Date");
        examDatePicker.setValue(claim != null ? new java.sql.Date(claim.getExamDate().getTime()).toLocalDate() : null);

        VBox imagesBox = new VBox();
        if (claim != null) {
            List<ImageView> images = ImageUtils.renderUploadViews(claim.getId());
            imagesBox.getChildren().addAll(images);
        }

        Button uploadButton = new Button("Upload Images");
        uploadButton.setOnAction(e -> ImageUtils.uploadImages((Stage) dialog.getDialogPane().getScene().getWindow(), idField.getText()));

        dialog.getDialogPane().setContent(new VBox(10, idField, customerIdField, insuredPersonField, bankField, claimAmountField, statusField, claimDatePicker, examDatePicker, imagesBox, uploadButton));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String id = idField.getText();
                String customerId = customerIdField.getText();
                String insuredPerson = insuredPersonField.getText();
                String bank = bankField.getText();
                double claimAmount = Double.parseDouble(claimAmountField.getText());
                String status = statusField.getText();
                Date claimDate = java.sql.Date.valueOf(claimDatePicker.getValue());
                Date examDate = java.sql.Date.valueOf(examDatePicker.getValue());

                // Assuming documents are the names of files in the upload directory for this claim
                List<String> documents = ImageUtils.fetchImages(id);

                return new ClaimManagement(id, customerId, claimDate, insuredPerson, examDate, documents.toArray(new String[0]), claimAmount, bank, status);
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void addTableRowDoubleClickHandler() {
        claimsTable.setRowFactory(tv -> {
            TableRow<ClaimManagement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ClaimManagement rowData = row.getItem();
                    showClaimDetails(rowData);
                }
            });
            return row;
        });
    }

    private void showClaimDetails(ClaimManagement claim) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Claim Details");
        alert.setHeaderText(null);
        alert.setContentText("Claim ID: " + claim.getId() + "\n" +
                "Customer ID: " + claim.getCustomerId() + "\n" +
                "Insured Person: " + claim.getInsuredPerson() + "\n" +
                "Claim Amount: $" + claim.getClaimAmount() + "\n" +
                "Status: " + claim.getStatus() + "\n" +
                "Claim Date: " + claim.getClaimDate() + "\n" +
                "Exam Date: " + claim.getExamDate() + "\n" +
                "Documents: " + String.join(", ", claim.getDocuments()) + "\n" +
                "Receiver Banking Info: " + claim.getReceiverBankingInfo());
        alert.showAndWait();
    }
}
