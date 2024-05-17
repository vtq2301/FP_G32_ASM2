package rmit.fp.g32_asm2.controller.customer.policyOwner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.auth.AuthService;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.service.ClaimService;
import rmit.fp.g32_asm2.service.CustomerService;
import rmit.fp.g32_asm2.util.ViewUtils;

import java.io.IOException;
import java.util.Objects;

public class DashboardController {
    private final CustomerService customerService = new CustomerService();
    private final ClaimService claimService = new ClaimService();
    private final UserService userService = new UserService();
    private final User currentUser = AuthContext.getCurrentUser();
    @FXML private ImageView userIcon;
    @FXML private VBox mainContent;

    @FXML
    public void initialize(){
        showClaims();

    }

    public void setMainContent(VBox content) {
        mainContent.getChildren().setAll(content);
    }

    @FXML
    private void showClaims(){
        loadContent("/view/customer/policy-owner/claims-view.fxml");
    }

    private void loadContent(String fxml) {
        try {
            VBox content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            setMainContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBeneficiaries(ActionEvent actionEvent) {
        loadContent("/view/customer/policy-owner/users-view.fxml");
    }

    @FXML
    private void showLogs(ActionEvent actionEvent){

    }

    @FXML
    private void showProfile(ActionEvent actionEvent) {
        System.out.println(currentUser);
//        loadContent("/view/customer/policy-owner/profile.fxml");
        loadContent("/view/common/user-info-view.fxml");

    }

    @FXML
    private void logout(ActionEvent actionEvent) {
        AuthService.logout();
//        System.out.println(currentUser + ", " + AuthContext.getCurrentUser());
        ViewUtils.getInstance().renderView("/view/common/login-view.fxml", "Login");
    }
}
