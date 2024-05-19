package all.controller.policyOwner;

import all.auth.ActionLogger;
import all.controller.UniqueIDGenerator;
import all.controller.UserSession;
import all.db.ConnectionPool;
import all.model.customer.User;
import all.service.PolicyOwnerService;
import all.util.ValidationUtils;
import all.util.ViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;

import java.sql.SQLException;
import java.util.Objects;

public class BeneficiariesViewController {

    public AnchorPane beneficiariesView;
    @FXML
    private TextField searchField;
    @FXML
    private FilteredTableView<User> beneficiariesTable;
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
    @FXML
    private Label yearlyPayment;

    private final PolicyOwnerService service = new PolicyOwnerService();
    private final double DEPENDENT_RATE = 0.6;

    private ObservableList<User> beneficiaryList = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;
    private final int ROWS_PER_PAGE = 20;
    private User currentUser = UserSession.getUser();

    @FXML
    public void initialize() {
        initializeTableColumns();
        initializeBeneficiaryList();
        initializeSearchField();
        initializePagination();
        calculateYearlyPayment(UserSession.getCurrentUser());
        addTableDoubleClickHandler();
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

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
                        boolean confirmed = ViewUtils.showDeleteConfirmationDialog();
                        if (confirmed) {
                            user.setActive(newValue);
                            service.updateUser(user);
                            refreshTable();
                        } else {
                            toggleSwitch.setSelected(oldValue);
                        }
                    });
                    setGraphic(toggleSwitch);
                }
            }
        });

        popupFilters(idColumn, nameColumn, roleColumn, phoneColumn, emailColumn, addressColumn);
    }

    @SafeVarargs
    private void popupFilters(FilteredTableColumn<User, String>... columns) {
        for (FilteredTableColumn<User, String> column : columns) {
            PopupFilter<User, String> popupFilter = new PopupStringFilter<>(column);
            column.setOnFilterAction(e -> popupFilter.showPopup());
        }
    }

    private void initializeBeneficiaryList() {
        beneficiaryList.setAll(service.findAllBeneficiaries(currentUser));
        filteredData = new FilteredList<>(beneficiaryList, b -> true);
        filteredData.predicateProperty().bind(beneficiariesTable.predicateProperty());
    }

    private void initializeSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(beneficiary -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return beneficiary.getId().toLowerCase().contains(lowerCaseFilter) ||
                        beneficiary.getFullName().toLowerCase().contains(lowerCaseFilter) ||
                        beneficiary.getRole().toLowerCase().contains(lowerCaseFilter) ||
                        beneficiary.getPhoneNumber().toLowerCase().contains(lowerCaseFilter) ||
                        beneficiary.getAddress().toLowerCase().contains(lowerCaseFilter) ||
                        beneficiary.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
            updateTableView(0);
        });
    }

    private void initializePagination() {
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(beneficiariesTable.comparatorProperty());

        pagination.setPageCount((int) Math.ceil((double) filteredData.size() / ROWS_PER_PAGE));
        pagination.currentPageIndexProperty().addListener((observable, oldIndex, newIndex) -> updateTableView(newIndex.intValue()));

        updateTableView(0);
    }

    private void updateTableView(int index) {
        int fromIndex = index * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredData.size());
        beneficiariesTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
    }

    private void calculateYearlyPayment(User user) {
        int rate = service.getRate(user);
        if (rate <= 0) return;
        double totalPayment = beneficiaryList.stream().mapToDouble(beneficiary -> {
            if (Objects.equals(beneficiary.getRole(), "PolicyHolder")) {
                return rate;
            }
            return rate * DEPENDENT_RATE;
        }).sum();
        yearlyPayment.setText(String.format("$%.2f", totalPayment));
    }

    private void addTableDoubleClickHandler() {
        beneficiariesTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    User rowData = row.getItem();
                    showUserInfo(rowData);
                }
            });
            return row;
        });
    }

    private void showUserInfo(User user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Information");
        alert.setHeaderText(null);
        alert.setContentText("User Details:\n" +
                "ID: " + user.getId() + "\n" +
                "Name: " + user.getFullName() + "\n" +
                "Role: " + user.getRole() + "\n" +
                "Phone: " + user.getPhoneNumber() + "\n" +
                "Address: " + user.getAddress() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Active: " + user.isActive());
        alert.showAndWait();
    }

    @FXML
    private void addUser() throws SQLException {
        User newUser = showUserDialog(null);
        if (newUser != null) {
            service.addBeneficiary(newUser, UserSession.getCurrentUser().getId());
            beneficiaryList.add(newUser);
            filteredData.setPredicate(null); // Reset filter
            updateTableView(pagination.getCurrentPageIndex());
            ActionLogger actionLogger = new ActionLogger();
            actionLogger.logAction(currentUser.getId(), "Add User", "Add User", null);
        }
    }

    @FXML
    private void updateUser() throws SQLException {
        User selectedUser = beneficiariesTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            User updatedUser = showUserDialog(selectedUser);
            if (updatedUser != null) {
                service.updateUser(updatedUser);
                beneficiariesTable.refresh();
                ActionLogger actionLogger = new ActionLogger();
                actionLogger.logAction(currentUser.getId(), "Update User", "Update User", null);
            }
        }
    }

    private User showUserDialog(User user) throws SQLException {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Add User" : "Update User");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.setText(user != null ? user.getId() : UniqueIDGenerator.generateUniqueID(ConnectionPool.getInstance().getConnection()));
        idField.setDisable(true);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.setText(user != null ? user.getFullName() : "");

        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        roleField.setText(user != null ? user.getRole() : "");

        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        phoneNumberField.setText(user != null ? user.getPhoneNumber() : "");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        addressField.setText(user != null ? user.getAddress() : "");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setText(user != null ? user.getUsername() : "");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText(user != null ? user.getEmail() : "");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ToggleSwitch isActiveSwitch = new ToggleSwitch("Active");
        isActiveSwitch.setSelected(user != null && user.isActive());

        VBox vBox = new VBox();
        vBox.getChildren().addAll(idField, fullNameField, roleField, phoneNumberField, addressField, emailField, passwordField, isActiveSwitch);
        dialog.getDialogPane().setContent(vBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String id = idField.getText();
                String fullName = fullNameField.getText();
                String role = roleField.getText();
                String phoneNumber = phoneNumberField.getText();
                String address = addressField.getText();
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();

                if (!ValidationUtils.isValidEmail(email)) {
                    ViewUtils.showErrorMessage("Invalid email format.");
                    return null;
                }

                if (!ValidationUtils.isValidPhone(phoneNumber)) {
                    ViewUtils.showErrorMessage("Invalid phone number format.");
                    return null;
                }

                if (password.isEmpty() && user == null) {
                    ViewUtils.showErrorMessage("Password is required for a new user.");
                    return null;
                }

                if (!ValidationUtils.isValidPassword(password)) {
                    ViewUtils.showErrorMessage("Password must contain at least 6 characters, including a number and a special character.");
                    return null;
                }

                if (ValidationUtils.userExists(id) && user == null) {
                    ViewUtils.showErrorMessage("User with this ID already exists.");
                    return null;
                }

                return new User.Builder()
                        .setId(id)
                        .setFullName(fullName)
                        .setRole(role)
                        .setPhoneNumber(phoneNumber)
                        .setAddress(address)
                        .setEmail(email)
                        .setPassword(password)
                        .setIsActive(isActiveSwitch.isSelected())
                        .setUsername(username)
                        .build();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    @FXML
    private void removeUser() {
        User selectedUser = beneficiariesTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean confirmed = ViewUtils.showDeleteConfirmationDialog();
            if (confirmed) {
                service.removeBeneficiary(selectedUser.getId());
                beneficiaryList.remove(selectedUser);
                updateTableView(pagination.getCurrentPageIndex());
                ActionLogger actionLogger = new ActionLogger();
                actionLogger.logAction(currentUser.getId(), "Remove User", "Remove A Beneficiary", null);
            }
        }
    }

    private void refreshTable() {
        beneficiariesTable.refresh();
    }
}
