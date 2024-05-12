package rmit.fp.g32_asm2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminScreenController{
    @FXML
    private Label labelAdmin;
    @FXML
    private AnchorPane anchorPane;


    @FXML
    protected void handlePolicyHolderButton(ActionEvent event){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/rmit/fp/g32_asm2/PolicyHolderScreen.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

    }
    @FXML
    private void handleDependentButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/DependentScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Dependent");
            currentstage.show();
        }  catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleInsuranceManagerButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/InsuranceManagerScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Insurance Manager");
            currentstage.show();
        }  catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleInsuranceSurveyorsButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/InsuranceSurveyorScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Insurance Surveyors");
            currentstage.show();
        }  catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleClaimButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/ClaimScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Claim Management");
            currentstage.show();
        }  catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleAdminButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/AdminInfoScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Admin Information");
            currentstage.show();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleLogoutButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rmit/fp/g32_asm2/LoginScreen.fxml"));
            Parent root = loader.load();
            Stage currentstage = (Stage) labelAdmin.getScene().getWindow();
            currentstage.setScene(new Scene(root));
            currentstage.setTitle("Login Screen");
            currentstage.show();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
