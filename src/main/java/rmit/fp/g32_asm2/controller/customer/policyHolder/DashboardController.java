package rmit.fp.g32_asm2.controller.customer.policyHolder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import rmit.fp.g32_asm2.controller.components.CardController;

public class DashboardController {
    @FXML private TilePane tilePane;


    public void initialize() {

        try {
            // Example of dynamically loading multiple cards
            for (int i = 0; i < 5; i++) {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/view/components/card.fxml"));
                Node card = cardLoader.load();
                CardController controller = cardLoader.getController();
                controller.setTitle("Card " + (i + 1));
                controller.setDescription("Description " + (i + 1));
                tilePane.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
