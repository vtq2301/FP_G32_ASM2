package rmit.fp.g32_asm2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    @FXML
    private Label labelAdmin;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private void handlePolicyHolderButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PolicyHolderScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Policy Holders");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleDependentButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DependentScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Dependent");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleInsuranceManagerButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InsuranceManagerScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Insurance Manager");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleInsuranceSurveyorsButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InsuranceSurveyorScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Insurance Surveyors");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleClaimButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClaimScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Claim Management");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleAdminButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminInfoScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Admin Information");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
    private void handleLogoutButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Login Screen");
            currentstage.show();
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the screen: " + e.getMessage());
        }
    }
}
