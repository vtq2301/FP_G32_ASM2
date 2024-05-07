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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Label userNameEmpty;
    @FXML
    private Label passwordEmpty;
    @FXML
    private Label invalidLogin;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusText;
//    private AuthService;
    public void loginButtonAction(ActionEvent e){
        String username = usernameField.getText();
        String password = passwordField.getText();
    }
    private void loadAdminScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminScreen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found in the specified path.");
            }
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusText.setText("Failed to load the Admin screen: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            statusText.setText("An error occurred: " + e.getMessage());
        }
    }
    public void radioButtonChanged(){

    }
    public void resetMessage(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
