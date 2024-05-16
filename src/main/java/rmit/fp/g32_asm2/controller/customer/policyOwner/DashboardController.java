package rmit.fp.g32_asm2.controller.customer.policyOwner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import rmit.fp.g32_asm2.auth.AuthContext;
import rmit.fp.g32_asm2.controller.common.LayoutController;
import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.customer.PolicyOwner;
import rmit.fp.g32_asm2.model.log.MyLogRecord;
import rmit.fp.g32_asm2.service.AdminService;
import rmit.fp.g32_asm2.service.ClaimService;
import rmit.fp.g32_asm2.service.CustomerService;
import rmit.fp.g32_asm2.util.ViewUtils;
import rmit.fp.g32_asm2.view.Routes;

import java.io.IOException;
import java.util.List;

public class DashboardController {
    private final CustomerService customerService = new CustomerService();
    private final ClaimService claimService = new ClaimService();
    private final AdminService adminService = new AdminService();
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
            VBox content = FXMLLoader.load(getClass().getResource(fxml));
            setMainContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBeneficiaries(ActionEvent actionEvent) {
    }

    @FXML
    private void showLogs(ActionEvent actionEvent){

    }

    @FXML
    private void showProfile(MouseEvent mouseEvent) {
        System.out.println(currentUser);
    }
}
