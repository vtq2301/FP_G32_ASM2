package all.controller.policyOwner;

import all.controller.UserSession;
import all.model.customer.User;
import all.service.PolicyOwnerService;
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

import java.util.Objects;

public class BeneficiariesViewController {

    public AnchorPane beneficiariesView;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<User> beneficiariesTable;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> addressColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Boolean> isActiveSwitchColumn;
    @FXML
    private Pagination pagination;
    @FXML
    private Label yearlyPayment;

    private final PolicyOwnerService service = new PolicyOwnerService();
    private final double DEPENDENT_RATE = 0.6;

    private ObservableList<User> beneficiaryList = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;
    private final int ROWS_PER_PAGE = 10;

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
        isActiveSwitchColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
    }

    private void initializeBeneficiaryList() {
        User currentUser = UserSession.getCurrentUser();
        beneficiaryList.setAll(service.findAllBeneficiaries(currentUser));
        filteredData = new FilteredList<>(beneficiaryList, b -> true);
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
                        beneficiary.getAddress().toLowerCase().contains(lowerCaseFilter);
//                        beneficiary.getEmail().toLowerCase().contains(lowerCaseFilter);
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
                    System.out.println(rowData);
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
    private void addUser() {
        // Logic to add a new user (e.g., show dialog to enter user details and call service.addBeneficiary)
        User newUser = new User.Builder()
                .setId("new_id")
                .setPassword("password")
                .setRole("Dependent")
                .setFullName("New User")
                .setAddress("New Address")
                .setPhoneNumber("1234567890")
                .setUsername("newuser")
                .setPolicyHolderId("policyHolderId")
                .setEmail("newuser@example.com")
                .setIsActive(true)
                .build();
        service.addBeneficiary(newUser, UserSession.getCurrentUser().getId());
        beneficiaryList.add(newUser);
        filteredData.setPredicate(null); // Reset filter
        updateTableView(pagination.getCurrentPageIndex());
    }

    @FXML
    private void updateUser() {
        // Logic to update an existing user (e.g., show dialog to edit user details and call service.updateBeneficiary)
        User selectedUser = beneficiariesTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setFullName("Updated Name");
            selectedUser.setAddress("Updated Address");
            selectedUser.setPhoneNumber("0987654321");
            selectedUser.setEmail("updateduser@example.com");
            service.updateBeneficiary(selectedUser);
            beneficiariesTable.refresh();
        }
    }

    @FXML
    private void removeUser() {
        // Logic to remove a user
        User selectedUser = beneficiariesTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            service.removeBeneficiary(selectedUser.getId());
            beneficiaryList.remove(selectedUser);
            filteredData.setPredicate(null); // Reset filter
            updateTableView(pagination.getCurrentPageIndex());
        }
    }
}
