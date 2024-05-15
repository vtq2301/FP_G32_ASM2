package all.controller.insurance;

import all.auth.ClaimDatabase;
import all.auth.ActionLogger;
import all.controller.UserSession;
import all.model.customer.ClaimManagement;
import all.model.customer.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class InsuranceSurveyor {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField surveyorIdField;
    @FXML private Button requestMoreInfoButton;
    @FXML private Button proposeClaimButton;
    @FXML private ComboBox<String> filterOptions;
    @FXML private ListView<ClaimManagement> claimsListView;

    private final ClaimDatabase claimDatabase = new ClaimDatabase();

    @FXML
    public void initialize() {
        filterOptions.setItems(FXCollections.observableArrayList("All", "Pending", "Approved", "Rejected"));
        filterOptions.setOnAction(event -> loadClaims());
    }

    public void loadData(User user) {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getAddress());
        contactNumberField.setText(user.getPhoneNumber());
        surveyorIdField.setText("SURV123456"); // Example Surveyor ID

        loadClaims();
    }

    private void loadClaims() {
        String filter = filterOptions.getValue();
        List<ClaimManagement> claims;
        if ("All".equals(filter) || filter == null) {
            claims = claimDatabase.getAllClaims();
        } else {
            claims = claimDatabase.getClaimsByStatus(filter);
        }

        ObservableList<ClaimManagement> observableClaims = FXCollections.observableArrayList(claims);
        claimsListView.setItems(observableClaims);
    }

    @FXML
    private void requestMoreInfo() {
        ClaimManagement selectedClaim = claimsListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            selectedClaim.setStatus("More Information Required");
            claimDatabase.updateClaim(selectedClaim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Request More Info", "Requested more info for claim: " + selectedClaim.getId(), selectedClaim.getId());

            notifyManagers(selectedClaim.getId(), "More Information Required");
            loadClaims();
        }
    }

    @FXML
    private void proposeClaim() {
        ClaimManagement selectedClaim = claimsListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            selectedClaim.setStatus("Processing");
            claimDatabase.updateClaim(selectedClaim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Propose Claim", "Proposed claim: " + selectedClaim.getId(), selectedClaim.getId());

            notifyManagers(selectedClaim.getId(), "Processing");
            loadClaims();
        }
    }

    private void notifyManagers(String claimId, String action) {
        System.out.println("Notification to Managers: Claim ID " + claimId + " requires " + action);
    }
}
