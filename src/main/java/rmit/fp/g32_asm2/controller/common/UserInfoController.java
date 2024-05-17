package rmit.fp.g32_asm2.controller.common;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.model.RolesEnum;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.UserType;
import rmit.fp.g32_asm2.model.customer.*;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.util.ValidationUtils;
import rmit.fp.g32_asm2.util.ViewUtils;

import static rmit.fp.g32_asm2.model.customer.CustomerType.*;

public class UserInfoController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<RolesEnum> roleComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private ToggleSwitch isActiveSwitch;

    @FXML
    private TextField rateField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private User user;
    private final User currentUser = AuthContext.getCurrentUser();
    private final UserService userService = new UserService();
    public UserInfoController() {
        // Default constructor
    }

    public void setUser(User user) {
        this.user = user;
        updateUserFields();
        disableAllFields(true);
    }

    @FXML
    public void initialize() {
        updateRoleCombobox();
        if (user == null) {
            disableAllFields(false);
        } else {
            setUser(user);
        }
    }

    private void updateRoleCombobox() {
        boolean isAdmin = currentUser.getRole().equals(UserType.ADMIN);
        boolean isOwner = currentUser.getRole().equals(POLICY_OWNER);
        if (isAdmin) {
            roleComboBox.getItems().setAll(RolesEnum.values());
        } else if (isOwner) {
            roleComboBox.getItems().setAll(POLICY_HOLDER, DEPENDENT);
        }
    }
    private void disableAllFields(boolean active) {
        usernameField.setDisable(active);
        phoneField.setDisable(active);
        emailField.setDisable(active);
        addressField.setDisable(active);
        nameField.setDisable(active);
        passwordField.setDisable(active);
        isActiveSwitch.setDisable(active);
        rateField.setDisable(active);
        roleComboBox.setDisable(active);
    }

    private void updateUserFields() {
        usernameField.setText(user.getUsername());
        nameField.setText(user.getName());
        roleComboBox.setValue(user.getRole());

        phoneField.setText(user.getPhoneNumber());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        isActiveSwitch.setSelected(user.getIsActive());
        if (user instanceof Customer) {
            double rate = ((Customer) user).getRate();
            rateField.setText(String.valueOf(rate));
        }
    }

    @FXML
    private void saveUser() {
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (!ValidationUtils.isValidEmail(email)) {
            ViewUtils.showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            ViewUtils.showAlert("Invalid Phone Number", "Please enter a valid phone number.");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            ViewUtils.showAlert("Invalid Password", "Password must be at least 6 characters long, contain at least one uppercase letter, one symbol, and one number.");
            return;
        }

        // Proceed with saving the user
        if (user == null) {
            user = new User.Builder()
                    .withUsername(usernameField.getText())
                    .withHashPassword(password) // Hash the password before saving
                    .withName(nameField.getText())
                    .withRoleId(roleComboBox.getValue().getValue())
                    .withPhoneNumber(phone)
                    .withEmail(email)
                    .withAddress(addressField.getText())
                    .withIsActive(isActiveSwitch.isSelected())
                    .build();

            switch (user.getRole()) {
                case POLICY_HOLDER -> ((PolicyHolder) user).setRate(((PolicyOwner) currentUser).getRate());
                case DEPENDENT -> ((Dependent) user).setRate(((PolicyOwner) currentUser).getRate() * 0.6);
                case POLICY_OWNER -> ((PolicyOwner) user).setRate(Double.parseDouble(rateField.getText()));
                default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
            }


            // Save the new user using your user service
             userService.addUser(user);

        } else {
            // Update existing user
//            user.setUsername(usernameField.getText());
            user.setName(nameField.getText());
//            user.setRole(roleComboBox.getValue());
            user.setPhoneNumber(phone);
            user.setEmail(email);
            user.setAddress(addressField.getText());
            user.setIsActive(isActiveSwitch.isSelected());
            switch (user.getRole()) {
                case POLICY_HOLDER -> ((PolicyHolder) user).setRate(((PolicyOwner) currentUser).getRate());
                case DEPENDENT -> ((Dependent) user).setRate(((PolicyOwner) currentUser).getRate() * 0.6);
                case POLICY_OWNER -> ((PolicyOwner) user).setRate(Double.parseDouble(rateField.getText()));
                default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
            }

            // Save the updated user using your user service
             userService.updateUser(user);
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }




    @FXML
    private void cancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void enableRate() {
        rateField.setDisable(false);
    }

    @FXML
    private void enablePasswordField() {
        passwordField.setDisable(false);
    }

    @FXML
    private void enablePhoneField() {
        phoneField.setDisable(false);
    }

    @FXML
    private void enableEmailField() {
        emailField.setDisable(false);
    }

    @FXML
    private void enableAddressField() {
        addressField.setDisable(false);
    }

    @FXML
    private void enableRoleComboBox() {
        roleComboBox.setDisable(false);
    }
}
