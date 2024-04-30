package rmit.fp.g32_asm2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloControllerTest {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}