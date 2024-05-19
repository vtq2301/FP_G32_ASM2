/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller;

import all.db.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import all.auth.ClaimDatabase;
import all.model.customer.ClaimManagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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

    private final ObservableList<ClaimManagement> claimData = FXCollections.observableArrayList();
    public Button btnSumUp;
    public TextField tfTotalAmount;
    public Button btnReload;
    public TextField tfSearch;


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
        retrieveClaimInfo();

    }

    private void loadData() {
        setupColumnFactories();
        claimData.setAll(ClaimDatabase.getAllClaims());
        tvClaims.setItems(claimData);
    }
    private void setBtnBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminScreen.fxml"));
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
        sumUpAllClaims();
    }

    private void sumUpAllClaims() {
        double totalAmount = 0;
        ObservableList<ClaimManagement> items = tvClaims.getItems();
        for(ClaimManagement item:items){
            Stream<ClaimManagement> filteredClaims = claimData.stream()
                    .filter(claim -> claim.getStatus().equals("Approved"))
                    .filter(claim->claim.getReceiverBankingInfo().equals(colReceiverBankingInfo.getCellObservableValue(item).getValue()))
                    .filter(claim->claim.getClaimDate().equals(colClaimDate.getCellObservableValue(item).getValue()));

            double claimAmount = filteredClaims.mapToDouble(ClaimManagement::getClaimAmount).sum();
            totalAmount += claimAmount;
        }

        // Assuming tfTotalAmount is a TextField or Label, set the total amount:
        tfTotalAmount.setText(String.format("%.2f", totalAmount));
    }

    public void handleReloadButton(ActionEvent event) {
        setBtnReload();
    }

    private void retrieveClaimInfo() {
        //initial filter
        FilteredList<ClaimManagement> filteredList = new FilteredList<>(claimData, b->true);
        tfSearch.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(claimManagement -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();
                if(claimManagement.getCustomerId().toLowerCase().indexOf(searchKeyWord)> -1){
                    return true;
                } else if (claimManagement.getInsuredPerson().toLowerCase().indexOf(searchKeyWord)> -1){
                    return true;
                }else if(claimManagement.getClaimDate().toString().indexOf(searchKeyWord)> -1){
                    return true;
                }else if(claimManagement.getExamDate().toString().indexOf(searchKeyWord)> -1){
                    return true;
                }
                else if (claimManagement.getReceiverBankingInfo().toLowerCase().indexOf(searchKeyWord)> -1){
                    return true;
                }else if (claimManagement.getStatus().toLowerCase().indexOf(searchKeyWord)> -1){
                    return true;
                }else
                    return false;
            });
        });
        SortedList<ClaimManagement> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tvClaims.comparatorProperty());
        tvClaims.setItems(sortedList);

    }private void setBtnReload(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) btnReload.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Claim Management");
            currentstage.show();
        }  catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }}
}
