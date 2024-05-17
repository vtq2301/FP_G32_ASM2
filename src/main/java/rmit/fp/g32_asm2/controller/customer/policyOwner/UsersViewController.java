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
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;

import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.util.ViewUtils;

import java.util.List;

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

    private ObservableList<User> userData = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;
    private static final int ROWS_PER_PAGE = 15;
    private UserService userService = new UserService();
    private User currentUser = AuthContext.getCurrentUser();

    @FXML
    private void initialize() {
        // Initialize the table columns.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        List<User> users = userService.getBeneficiaries(currentUser.getId());
        userData.setAll(users);
        // Add sample data to the table.
//        userData.addAll(
//                new User.Builder().withName("John Doe").withId("U001").withRoleId(11).withPhoneNumber("123-456-7890").withEmail("john.doe@example.com")
//                        .withAddress("123 Elm St").withIsActive(true).build(),
//                new User.Builder().withId("D001").withName("Jane Smith").withRoleId(12).withPhoneNumber("234-567-8901").withEmail("jane.smith@example.com")
//                        .withAddress("456 Oak St").withIsActive(false).build()
//
//        );

        // Create a FilteredList wrapping the ObservableList.
        filteredData = new FilteredList<>(userData, p -> true);

        // Bind the predicate and comparator properties.
        filteredData.predicateProperty().bind(usersTable.predicateProperty());
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedData);

        // Add popup filter to columns.
        popupFilters(idColumn, nameColumn, roleColumn);

        popupFilters(phoneColumn, emailColumn, addressColumn);

        // Set up pagination.
        int pageCount = (int) Math.ceil(filteredData.size() / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updatePage(newIndex.intValue()));

        updatePage(0); // Initialize first page

        // Row double click to view info
        usersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()){
                    User rowData = row.getItem();
                    viewInfo(rowData);
                }
            });
            return row;
        });

        // Set cell factory for isActiveColumn to add a toggle switch
        isActiveColumn.setCellFactory(column -> new TableCell<>() {
            private final ToggleSwitch toggleSwitch = new ToggleSwitch();
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setGraphic(null);
                } else {
                    toggleSwitch.setSelected(item);
                    toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        User user = getTableView().getItems().get(getIndex());
                        System.out.println(user);
                        boolean confirmed = ViewUtils.getInstance().showDeleteConfirmationDialog();
                        if (confirmed) {
                            user.setIsActive(newValue);
                            userService.updateUser(user);
                            System.out.println(user);
                        } else {
                            toggleSwitch.setSelected(oldValue);
                        }
                    });
                    setGraphic(toggleSwitch);
                }
            }
        });
    }

    private void popupFilters(FilteredTableColumn<User, String> phoneColumn, FilteredTableColumn<User, String> emailColumn, FilteredTableColumn<User, String> addressColumn) {
        PopupFilter<User, String> popupPhoneFilter = new PopupStringFilter<>(phoneColumn);
        phoneColumn.setOnFilterAction(e -> popupPhoneFilter.showPopup());

        PopupFilter<User, String> popupEmailFilter = new PopupStringFilter<>(emailColumn);
        emailColumn.setOnFilterAction(e -> popupEmailFilter.showPopup());

        PopupFilter<User, String> popupAddressFilter = new PopupStringFilter<>(addressColumn);
        addressColumn.setOnFilterAction(e -> popupAddressFilter.showPopup());
    }

    private void updatePage(int index) {
        int fromIndex = index * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredData.size());
        usersTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
    }

    @FXML
    private void viewInfo() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            viewInfo(selectedUser);
        }
    }

    private void viewInfo(User user){
        System.out.println(user);
    }

    @FXML
    private void addUser() {
        // Logic to add a new user
        loadUserInfoView();
    }

    @FXML
    private void updateUser() {
        // Logic to update the selected user
        User user = usersTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            System.out.println("Updating " + user);
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean confirmed = ViewUtils.getInstance().showDeleteConfirmationDialog(); // show confirmation
            if (confirmed) {
                // Logic to delete the selected user
                userService.deleteUser(selectedUser.getId());
                userData.remove(selectedUser);
                System.out.println("Deleted user: " + selectedUser);
            }
        }
    }

    private void loadUserInfoView() {
        ViewUtils viewUtils = ViewUtils.getInstance();
        Stage stage = new Stage();
        viewUtils.setPrimaryStage(stage);
        viewUtils.renderView("/view/common/user-info-view.fxml", "User Info");
    }
}
