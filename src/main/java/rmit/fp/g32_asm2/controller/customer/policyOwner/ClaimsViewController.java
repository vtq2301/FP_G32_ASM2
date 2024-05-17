package rmit.fp.g32_asm2.controller.customer.policyOwner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import rmit.fp.g32_asm2.auth.AuthContext;

import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.service.ClaimService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ClaimsViewController {

    @FXML
    private FilteredTableView<Claim> claimsTable;

    @FXML
    private FilteredTableColumn<Claim, String> claimIdColumn;

    @FXML
    private FilteredTableColumn<Claim, String> claimStatusColumn;

    @FXML
    private FilteredTableColumn<Claim, Double> claimAmountColumn;

    @FXML
    private FilteredTableColumn<Claim, String> claimInsuredPersonIdColumn;

    @FXML
    private FilteredTableColumn<Claim, Date> claimExamDataColumn;

    @FXML
    private FilteredTableColumn<Claim, Date> claimFiledDateColumn;

    @FXML
    private Pagination pagination;

    private ObservableList<Claim> claimData = FXCollections.observableArrayList();
    private FilteredList<Claim> filteredData;
    private static final int ROWS_PER_PAGE = 15;
    private ClaimService claimService = new ClaimService();
    private User currentUser = AuthContext.getCurrentUser();

    @FXML
    private void initialize() {
        // Initialize the table columns.
        claimIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        claimStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        claimAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        claimInsuredPersonIdColumn.setCellValueFactory(new PropertyValueFactory<>("insuredPersonId"));
        claimExamDataColumn.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        claimFiledDateColumn.setCellValueFactory(new PropertyValueFactory<>("examDate")); // Assuming filed date is exam date for demo purposes


        List<Claim> claims = claimService.getBeneficiaryClaims(currentUser.getId());
        claimData.setAll(claims);
        // Add sample data to the table.
//        claimData.addAll(
//                new Claim("C001", "P001", new Date(), Arrays.asList("Doc1", "Doc2"), 1000.0, Claim.Status.FILED),
//                new Claim("C002", "P002", new Date(), Arrays.asList("Doc3", "Doc4"), 2000.0, Claim.Status.PROCESSING),
//                new Claim("C003", "P003", new Date(), Arrays.asList("Doc5"), 500.0, Claim.Status.ACCEPTED),
//                new Claim("C004", "P004", new Date(), Arrays.asList("Doc6", "Doc7"), 1500.0, Claim.Status.REJECTED),
//                new Claim("C005", "P005", new Date(), Arrays.asList("Doc8"), 2500.0, Claim.Status.DONE),
//                new Claim("C001", "P001", new Date(), Arrays.asList("Doc1", "Doc2"), 1000.0, Claim.Status.FILED),
//                new Claim("C002", "P002", new Date(), Arrays.asList("Doc3", "Doc4"), 2000.0, Claim.Status.PROCESSING),
//                new Claim("C003", "P003", new Date(), Arrays.asList("Doc5"), 500.0, Claim.Status.ACCEPTED),
//                new Claim("C004", "P004", new Date(), Arrays.asList("Doc6", "Doc7"), 1500.0, Claim.Status.REJECTED),
//                new Claim("C005", "P005", new Date(), Arrays.asList("Doc8"), 2500.0, Claim.Status.DONE),
//                new Claim("C001", "P001", new Date(), Arrays.asList("Doc1", "Doc2"), 1000.0, Claim.Status.FILED),
//                new Claim("C002", "P002", new Date(), Arrays.asList("Doc3", "Doc4"), 2000.0, Claim.Status.PROCESSING),
//                new Claim("C003", "P003", new Date(), Arrays.asList("Doc5"), 500.0, Claim.Status.ACCEPTED),
//                new Claim("C004", "P004", new Date(), Arrays.asList("Doc6", "Doc7"), 1500.0, Claim.Status.REJECTED),
//                new Claim("C005", "P005", new Date(), Arrays.asList("Doc8"), 2500.0, Claim.Status.DONE),
//                new Claim("C001", "P001", new Date(), Arrays.asList("Doc1", "Doc2"), 1000.0, Claim.Status.FILED),
//                new Claim("C002", "P002", new Date(), Arrays.asList("Doc3", "Doc4"), 2000.0, Claim.Status.PROCESSING),
//                new Claim("C003", "P003", new Date(), Arrays.asList("Doc5"), 500.0, Claim.Status.ACCEPTED),
//                new Claim("C004", "P004", new Date(), Arrays.asList("Doc6", "Doc7"), 1500.0, Claim.Status.REJECTED),
//                new Claim("C005", "P005", new Date(), Arrays.asList("Doc8"), 2500.0, Claim.Status.DONE),
//                new Claim("C001", "P001", new Date(), Arrays.asList("Doc1", "Doc2"), 1000.0, Claim.Status.FILED),
//                new Claim("C002", "P002", new Date(), Arrays.asList("Doc3", "Doc4"), 2000.0, Claim.Status.PROCESSING),
//                new Claim("C003", "P003", new Date(), Arrays.asList("Doc5"), 500.0, Claim.Status.ACCEPTED),
//                new Claim("C004", "P004", new Date(), Arrays.asList("Doc6", "Doc7"), 1500.0, Claim.Status.REJECTED),
//                new Claim("C005", "P005", new Date(), Arrays.asList("Doc8"), 2500.0, Claim.Status.DONE)
//        );

        // Create a FilteredList wrapping the ObservableList.
        filteredData = new FilteredList<>(claimData, p -> true);

        // Bind the predicate and comparator properties.
        filteredData.predicateProperty().bind(claimsTable.predicateProperty());
        SortedList<Claim> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(claimsTable.comparatorProperty());
        claimsTable.setItems(sortedData);

        // Add popup filter to columns.
        PopupFilter<Claim, String> popupClaimIdFilter = new PopupStringFilter<>(claimIdColumn);
        claimIdColumn.setOnFilterAction(e -> popupClaimIdFilter.showPopup());

        PopupFilter<Claim, String> popupClaimStatusFilter = new PopupStringFilter<>(claimStatusColumn);
        claimStatusColumn.setOnFilterAction(e -> popupClaimStatusFilter.showPopup());

        PopupFilter<Claim, String> popupClaimInsuredPersonIdFilter = new PopupStringFilter<>(claimInsuredPersonIdColumn);
        claimInsuredPersonIdColumn.setOnFilterAction(e -> popupClaimInsuredPersonIdFilter.showPopup());

        // Add south filter to columns.
        SouthFilter<Claim, String> southClaimIdFilter = new SouthFilter<>(claimIdColumn, String.class);
        claimIdColumn.setSouthNode(southClaimIdFilter);

        SouthFilter<Claim, String> southClaimStatusFilter = new SouthFilter<>(claimStatusColumn, String.class);
        claimStatusColumn.setSouthNode(southClaimStatusFilter);

        SouthFilter<Claim, String> southClaimInsuredPersonIdFilter = new SouthFilter<>(claimInsuredPersonIdColumn, String.class);
        claimInsuredPersonIdColumn.setSouthNode(southClaimInsuredPersonIdFilter);

        // Set up pagination.
        int pageCount = (int) Math.ceil(filteredData.size() / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updatePage(newIndex.intValue()));

        updatePage(0); // Initialize first page
    }

    private void updatePage(int index) {
        int fromIndex = index * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredData.size());
        claimsTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
    }

    @FXML
    private void viewDocuments() {
        // Logic to view documents for the selected claim
    }

    @FXML
    private void fileClaim() {
        // Logic to file a new claim
    }

    @FXML
    private void updateClaim() {
        // Logic to update the selected claim
    }

    @FXML
    private void deleteClaim() {
        // Logic to delete the selected claim
    }
}
