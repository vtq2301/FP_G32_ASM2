package all.controllers;

import all.data.TextFileUtils;
import all.models.PolicyHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import java.util.ArrayList;
import java.util.List;

public class PolicyHolderController {

    @FXML
    private TextField idField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private List<PolicyHolder> policyHolders = new ArrayList<>();

    @FXML
    public void createPolicyHolder() {
        String id = idField.getText();
        String fullName = fullNameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        saveToTextFile(id, fullName, phone, address, email, password);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Creation Successful");
        alert.setHeaderText(null);
        alert.setContentText("Policy Holder created successfully.");
        alert.showAndWait();
    }

    private void saveToTextFile(String id, String fullName, String phone, String address, String email, String password) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("src/main/resources/policy_holders.txt", true)))) {
            writer.println(id + "," + fullName + "," + phone + "," + address + "," + email + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void retrievePolicyHolder() {
        policyHolders = TextFileUtils.loadPolicyHolders();
    }

    public void updatePolicyHolder() {
        String id = idField.getText();
        String fullName = fullNameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Policy Holder updated successfully.");
        alert.showAndWait();
    }

    public void deletePolicyHolder(ActionEvent actionEvent) {

    }
}
