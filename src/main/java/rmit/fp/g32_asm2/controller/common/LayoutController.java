package rmit.fp.g32_asm2.controller.common;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class LayoutController {
    @FXML
    private VBox mainContent;

    public void setMainContent(VBox content) {
        mainContent.getChildren().setAll(content);
    }
}
