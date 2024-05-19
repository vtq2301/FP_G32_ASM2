/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller;

import all.auth.ActionHistoryService;
import all.model.UserAction;
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

    public void loadActionHistory(String userId) {
        ActionHistoryService actionHistoryService = new ActionHistoryService();
        List<UserAction> actions = actionHistoryService.getUserActions(userId);
        ObservableList<UserAction> actionData = FXCollections.observableArrayList(actions);
        historyTable.setItems(actionData);
    }
}
