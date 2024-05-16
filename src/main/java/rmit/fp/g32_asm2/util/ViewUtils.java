package rmit.fp.g32_asm2.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class ViewUtils {
    private static ViewUtils instance;

    private final Stack<Pane> panels = new Stack<>();
    private final Stack<Pane> removedPanels = new Stack<>();
    private Stage primaryStage;

    private ViewUtils() {}

    public static synchronized ViewUtils getInstance() {
        if (instance == null) {
            instance = new ViewUtils();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void renderView(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane pane = loader.load();
            primaryStage.setTitle(title);
            panels.push(pane);
            removedPanels.clear(); // Clear forward history when a new view is rendered
            updateStage(pane);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void back() {
        if (panels.size() > 1) {
            Pane paneToRemove = panels.pop();
            removedPanels.push(paneToRemove);
            updateStage(panels.peek());
        }
    }

    public void forward() {
        if (!removedPanels.isEmpty()) {
            Pane paneToAdd = removedPanels.pop();
            panels.push(paneToAdd);
            updateStage(paneToAdd);
        }
    }

    public void clearHistory() {
        panels.clear();
        removedPanels.clear();
    }

    private void updateStage(Pane pane) {
        if (primaryStage != null) {
            primaryStage.setScene(new Scene(pane));
            primaryStage.show();
        } else {
            System.err.println("Primary stage is not set.");
        }
    }

//    public static void main(String[] args) {
//        // This main method is just for testing purposes
//        ViewUtils viewUtils = ViewUtils.getInstance();
//        viewUtils.setPrimaryStage(new Stage());
//        viewUtils.renderView("/view/yourView.fxml"); // Replace with your FXML path
//    }
}
