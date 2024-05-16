package rmit.fp.g32_asm2.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardController {
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }
}
