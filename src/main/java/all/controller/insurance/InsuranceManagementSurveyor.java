package all.controller.insurance;

import all.auth.ClaimDatabase;
import all.auth.ActionLogger;
import all.controller.UserSession;
import all.model.customer.ClaimManagement;
import all.model.customer.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsuranceManagementSurveyor{

    @FXML
    private ComboBox<String> filterOptions;
    @FXML
    private TableView<ClaimManagement> claimsTable;
    @FXML
    private TableColumn<ClaimManagement, String> idColumn;
    @FXML
    private TableColumn<ClaimManagement, String> customerIdColumn;
    @FXML
    private TableColumn<ClaimManagement, String> claimDateColumn;
    @FXML
    private TableColumn<ClaimManagement, String> insuredPersonColumn;
    @FXML
    private TableColumn<ClaimManagement, String> examDateColumn;
    @FXML
    private TableColumn<ClaimManagement, String> documentsColumn;
    @FXML
    private TableColumn<ClaimManagement, Double> claimAmountColumn;
    @FXML
    private TableColumn<ClaimManagement, String> receiverBankingInfoColumn;
    @FXML
    private TableColumn<ClaimManagement, String> statusColumn;
    @FXML
    private Label notificationLabel;
    @FXML
    private Button backButton;

    private final ClaimDatabase claimDatabase = new ClaimDatabase();
    private Map<String, List<String>> notifications = new HashMap<>();

    @FXML
    public void initialize() {
        filterOptions.setItems(FXCollections.observableArrayList("All", "New", "Approved", "Rejected", "Processing", "More Information Required"));
        filterOptions.setOnAction(event -> loadClaims());
        setupColumnFactories();
        loadClaims();
        updateNotificationDisplay();
    }

    public void loadClaims() {
        String filter = filterOptions.getValue();
        List<ClaimManagement> claims;
        if ("All".equals(filter) || filter == null) {
            claims = claimDatabase.getAllClaims();
        } else {
            claims = claimDatabase.getClaimsByStatus(filter);
        }
        ObservableList<ClaimManagement> observableClaims = FXCollections.observableArrayList(claims);
        claimsTable.setItems(observableClaims);
    }

    private void setupColumnFactories() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        claimDateColumn.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        insuredPersonColumn.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        examDateColumn.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        documentsColumn.setCellValueFactory(new PropertyValueFactory<>("documents"));
        claimAmountColumn.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        receiverBankingInfoColumn.setCellValueFactory(new PropertyValueFactory<>("receiverBankingInfo"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void requestMoreInfo() {
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
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
        ClaimManagement selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
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

    private void updateNotificationDisplay() {
        int totalNotifications = notifications.values().stream().mapToInt(List::size).sum();
        notificationLabel.setText("Notifications (" + totalNotifications + ")");
    }

    @FXML
    private void showNotifications() {
        System.out.println("Show notifications clicked!");
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InsuranceSurveyorScreen.fxml"));
            Parent parent = fxmlLoader.load();
            InsuranceSurveyor controller = fxmlLoader.getController();

            User currentUser = UserSession.getCurrentUser();
            controller.loadData(currentUser);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveNotification(String claimId, String status) {
        notifications.putIfAbsent(status, new ArrayList<>());
        notifications.get(status).add(claimId);
        updateNotificationDisplay();
    }
}
