package rmit.fp.g32_asm2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rmit.fp.g32_asm2.auth.ClaimDatabase;
import rmit.fp.g32_asm2.model.customer.ClaimManagement;
import java.net.URL;
import java.util.ResourceBundle;

public class ClaimScreenController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    public TableView<ClaimManagement> tvClaims = new TableView<ClaimManagement>();
    @FXML
    public TableColumn<ClaimManagement,String> colId;
    @FXML
    public TableColumn<ClaimManagement,String> colCustomerId;
    @FXML
    public TableColumn<ClaimManagement,String> colClaimDate;
    @FXML
    public TableColumn<ClaimManagement,String> colInsuredPerson;
    @FXML
    public TableColumn<ClaimManagement,String> colExamDate;
    @FXML
    public TableColumn<ClaimManagement,String> colDocuments;
    @FXML
    public TableColumn<ClaimManagement,String> colClaimAmount;
    @FXML
    public TableColumn<ClaimManagement,String> colReceiverBankingInfo;
    @FXML
    public TableColumn<ClaimManagement,String> colStatus;
    private final ClaimDatabase dbService = new ClaimDatabase();
    private final ObservableList<ClaimManagement> claimData = FXCollections.observableArrayList();
    public Button btnSumUp;
    public TextField tfTotalAmount;
    public Button btnReload;

    public void handleBackButton(ActionEvent event) {setBtnBack();
    }
    public void handleSumUpButton(ActionEvent event){setBtnSumUp();}
    private void setupColumnFactories() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDocuments.setCellValueFactory(new PropertyValueFactory<ClaimManagement, String>("documents"));
        colExamDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        colClaimDate.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        colInsuredPerson.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        colReceiverBankingInfo.setCellValueFactory(new PropertyValueFactory<>("receiverBankingInfo"));
        colClaimAmount.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        setupColumnFactories();
        claimData.setAll(ClaimDatabase.getAllClaims());
        tvClaims.setItems(claimData);
    }
    private void setBtnBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/AdminScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) backButton.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Admin System");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void setBtnSumUp(){
        claimData.setAll(ClaimDatabase.getClaimsByStatus("New"));
        tvClaims.setItems(claimData);
        double s = 0;
        for (int i = 0; i <claimData.size(); i++){

            s += claimData.get(i).getClaimAmount();
        }

        tfTotalAmount.setText(Double.toString(s));
    }
    public void handleReloadButton(ActionEvent event) {
        loadData();
    }
}

