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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsuranceManagement {

    @FXML private ComboBox<String> filterOptions;
    @FXML private ListView<ClaimManagement> claimsListView;
    @FXML private Label notificationLabel;
    @FXML private Button backButton;

    private final ClaimDatabase claimDatabase = new ClaimDatabase();
    private Map<String, List<String>> notifications = new HashMap<>();

    @FXML
    public void initialize() {
        filterOptions.setItems(FXCollections.observableArrayList("All", "New", "Approved", "Rejected", "Processing", "More Information Required"));
        filterOptions.setOnAction(event -> loadClaims());
        loadClaims();
        updateNotificationDisplay();
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
    private void approveClaim() {
        ClaimManagement selectedClaim = claimsListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null && "Processing".equals(selectedClaim.getStatus())) {
            selectedClaim.setStatus("Approved");
            claimDatabase.updateClaim(selectedClaim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Approve Claim", "Approved claim: " + selectedClaim.getId(), selectedClaim.getId());

            loadClaims();
        }
    }

    @FXML
    private void rejectClaim() {
        ClaimManagement selectedClaim = claimsListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null && "Processing".equals(selectedClaim.getStatus())) {
            selectedClaim.setStatus("Rejected");
            claimDatabase.updateClaim(selectedClaim);

            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(UserSession.getCurrentUser().getId(), "Reject Claim", "Rejected claim: " + selectedClaim.getId(), selectedClaim.getId());

            loadClaims();
        }
    }

    private void updateNotificationDisplay() {
        int totalNotifications = notifications.values().stream().mapToInt(List::size).sum();
        notificationLabel.setText("Notifications (" + totalNotifications + ")");
    }

    public void receiveNotification(String claimId, String status) {
        notifications.putIfAbsent(status, new ArrayList<>());
        notifications.get(status).add(claimId);
        updateNotificationDisplay();
    }

    @FXML
    private void showNotifications() {

    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InsuranceManagerScreen.fxml"));
            Parent parent = fxmlLoader.load();
            InsuranceManager controller = fxmlLoader.getController();

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
}
