package rmit.fp.g32_asm2.controller.customer.policyOwner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    @FXML
    private LayoutController commonLayoutController;

    @FXML
    public void initialize() {
        try {
            VBox customerContent = FXMLLoader.load(getClass().getResource("/view/admin-content.fxml"));
            commonLayoutController.setMainContent(customerContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
