package rmit.fp.g32_asm2.controller;

import rmit.fp.g32_asm2.auth.ActionHistoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;

public class UserActionHistoryController {
    @FXML private TableView<UserAction> historyTable;
    @FXML private TableColumn<UserAction, String> actionTypeColumn;
    @FXML private TableColumn<UserAction, String> descriptionColumn;
    @FXML private TableColumn<UserAction, LocalDateTime> timestampColumn;

    @FXML
    public void initialize() {
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("actionDescription"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    }

    public void loadActionHistory(int userId) {
        ActionHistoryService actionHistoryService = new ActionHistoryService();
        List<UserAction> actions = actionHistoryService.getUserActions(userId);
        ObservableList<UserAction> actionData = FXCollections.observableArrayList(actions);
        historyTable.setItems(actionData);
    }
}