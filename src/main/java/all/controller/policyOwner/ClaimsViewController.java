package all.controller.policyOwner;

import all.controller.UserSession;
import all.model.customer.ClaimManagement;
import all.service.ClaimService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.tableview2.FilteredTableView;

import java.util.Date;

public class ClaimsViewController {

    public AnchorPane claimsView;
    @FXML
    private TextField searchField;
    @FXML
    private FilteredTableView<ClaimManagement> claimsTable;
    @FXML
    private TableColumn<ClaimManagement, String> claimIdColumn;
    @FXML
    private TableColumn<ClaimManagement, String> customerIdColumn;
    @FXML
    private TableColumn<ClaimManagement, String> insuredPersonColumn;
    @FXML
    private TableColumn<ClaimManagement, Double> claimAmountColumn;
    @FXML
    private TableColumn<ClaimManagement, String> statusColumn;
    @FXML
    private TableColumn<ClaimManagement, Date> claimDateColumn;
    @FXML
    private TableColumn<ClaimManagement, Date> examDateColumn;
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
    }

    private void initializeClaimList() {
        String id = UserSession.getCurrentUser().getUsername();
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
    public void addClaim() {
        // Logic to add a new claim (e.g., show dialog to enter claim details and call claimService.addClaim)
        ClaimManagement newClaim = new ClaimManagement(
                "new_id", "customer_id", new Date(), "insured_person",
                new Date(), new String[]{"document1", "document2"},
                1000.0, "receiver_banking_info", "New"
        );
        claimService.addClaim(newClaim);
        claimList.add(newClaim);
        filteredData.setPredicate(null); // Reset filter
        updateTableView(pagination.getCurrentPageIndex());
    }

    @FXML
    public void updateClaim() {
        // Logic to update an existing claim (e.g., show dialog to edit claim details and call claimService.updateClaim)
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            selectedClaim.setInsuredPerson("Updated Person");
            selectedClaim.setClaimAmount(2000.0);
            selectedClaim.setStatus("Processing");
            claimService.updateClaim(selectedClaim);
            claimsTable.refresh();
        }
    }

    @FXML
    public void removeClaim() {
        // Logic to delete a claim
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            claimService.deleteClaim(selectedClaim.getId());
            claimList.remove(selectedClaim);
            filteredData.setPredicate(null); // Reset filter
            updateTableView(pagination.getCurrentPageIndex());
        }
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
