package all.controller.policyOwner;

import all.controller.UserSession;
import all.db.ConnectionPool;
import all.model.MyLogRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.tableview2.FilteredTableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class LogsViewController {

    public AnchorPane logsView;
    @FXML
    private TextField searchField;
    @FXML
    private FilteredTableView<MyLogRecord> logsTable;
    @FXML
    private TableColumn<MyLogRecord, String> idColumn;
    @FXML
    private TableColumn<MyLogRecord, String> actionTypeColumn;
    @FXML
    private TableColumn<MyLogRecord, String> actionDescriptionColumn;
    @FXML
    private TableColumn<MyLogRecord, Date> timestampsColumn;
    @FXML
    private TableColumn<MyLogRecord, String> usernameColumn;
    @FXML
    private TableColumn<MyLogRecord, String> claimIdColumn;

    private final ObservableList<MyLogRecord> logList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        actionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("actionDescription"));
        timestampsColumn.setCellValueFactory(new PropertyValueFactory<>("timestamps"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        claimIdColumn.setCellValueFactory(new PropertyValueFactory<>("claimId"));

        logList.setAll(findAll(UserSession.getCurrentUser().getUsername()));

        FilteredList<MyLogRecord> filteredData = new FilteredList<>(logList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(log -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return log.getId().toLowerCase().contains(lowerCaseFilter) ||
                        log.getActionType().toLowerCase().contains(lowerCaseFilter) ||
                        log.getActionDescription().toLowerCase().contains(lowerCaseFilter) ||
                        log.getTimestamps().toString().toLowerCase().contains(lowerCaseFilter) ||
                        log.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                        log.getClaimId().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<MyLogRecord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(logsTable.comparatorProperty());

        logsTable.setItems(sortedData);
    }

    private ObservableList<MyLogRecord> findAll(String username) {
        ObservableList<MyLogRecord> logs = FXCollections.observableArrayList();
        Connection conn = null;
        try  {
            conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM user_actions WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    logs.add(new MyLogRecord(
                            rs.getString("id"),
                            rs.getString("action_type"),
                            rs.getString("action_description"),
                            rs.getDate("timestamp"),
                            rs.getString("username"),
                            rs.getString("claim_id")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
        return logs;
    }
}
