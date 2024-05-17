package rmit.fp.g32_asm2.controller.customer.policyOwner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.model.RolesEnum;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.util.ViewUtils;

import java.util.List;

import static rmit.fp.g32_asm2.util.ViewUtils.loadUserInfoView;

public class UsersViewController {

    @FXML
    private FilteredTableView<User> usersTable;

    @FXML
    private FilteredTableColumn<User, String> idColumn;

    @FXML
    private FilteredTableColumn<User, String> nameColumn;

    @FXML
    private FilteredTableColumn<User, String> roleColumn;

    @FXML
    private FilteredTableColumn<User, String> phoneColumn;

    @FXML
    private FilteredTableColumn<User, String> emailColumn;

    @FXML
    private FilteredTableColumn<User, String> addressColumn;

    @FXML
    private FilteredTableColumn<User, Boolean> isActiveColumn;

    @FXML
    private Pagination pagination;

    private final ObservableList<User> userData = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;
    private static final int ROWS_PER_PAGE = 15;
    private final UserService userService = new UserService();
    private final User currentUser = AuthContext.getCurrentUser();

    @FXML
    private void initialize() {
        setupColumns();
        setupData();
        setupPagination();
        setupRowFactory();
        setupCellFactories();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        popupFilters(idColumn, nameColumn, roleColumn, phoneColumn, emailColumn, addressColumn);
    }

    private void setupData() {
        refreshTable();
    }

    private void setupPagination() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updatePage(newIndex.intValue()));
        updatePage(0); // Initialize first page
    }

    private void updatePage(int index) {
        int fromIndex = index * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredData.size());
        usersTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
    }

    private void setupRowFactory() {
        usersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    User rowData = row.getItem();
                    viewInfo(rowData);
                }
            });
            return row;
        });
    }

    private void setupCellFactories() {
        isActiveColumn.setCellFactory(column -> new TableCell<>() {
            private final ToggleSwitch toggleSwitch = new ToggleSwitch();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    toggleSwitch.setSelected(item);
                    toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        User user = getTableView().getItems().get(getIndex());
                        boolean confirmed = ViewUtils.getInstance().showDeleteConfirmationDialog();
                        if (confirmed) {
                            user.setIsActive(newValue);
                            userService.updateUser(user);
                            refreshTable(); // Refresh the table after updating the user
                        } else {
                            toggleSwitch.setSelected(oldValue);
                        }
                    });
                    setGraphic(toggleSwitch);
                }
            }
        });
    }

    @SafeVarargs
    private void popupFilters(FilteredTableColumn<User, String>... columns) {
        for (FilteredTableColumn<User, String> column : columns) {
            PopupFilter<User, String> popupFilter = new PopupStringFilter<>(column);
            column.setOnFilterAction(e -> popupFilter.showPopup());
        }
    }

    @FXML
    private void viewInfo() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            viewInfo(selectedUser);
        }
    }

    private void viewInfo(User user) {
        System.out.println(user);
        loadUserInfoView(getClass(), user);
    }

    @FXML
    private void addUser() {
        loadUserInfoView(getClass(), null);
        refreshTable(); // Refresh the table after adding a user
    }

    @FXML
    private void updateUser() {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            loadUserInfoView(getClass(), user);
            refreshTable(); // Refresh the table after updating a user
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean confirmed = ViewUtils.getInstance().showDeleteConfirmationDialog();
            if (confirmed) {
                userService.deleteUser(selectedUser.getId());
                userData.remove(selectedUser);
                refreshTable(); // Refresh the table after deleting a user
            }
        }
    }

    private void refreshTable() {
        List<User> users = userService.getBeneficiaries(currentUser.getId());
        userData.setAll(users);

        filteredData = new FilteredList<>(userData, p -> true);
        filteredData.predicateProperty().bind(usersTable.predicateProperty());
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedData);

        int pageCount = (int) Math.ceil(filteredData.size() / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
    }
}
