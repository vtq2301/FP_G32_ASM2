package rmit.fp.g32_asm2;

import javafx.stage.Stage;
import javafx.application.Application;

import rmit.fp.g32_asm2.DAO.UserDAO;
import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.RolesEnum;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.admin.SystemAdmin;
import rmit.fp.g32_asm2.model.customer.Customer;
import rmit.fp.g32_asm2.model.customer.CustomerRelations;
import rmit.fp.g32_asm2.model.customer.CustomerType;
import rmit.fp.g32_asm2.model.customer.PolicyOwner;
import rmit.fp.g32_asm2.service.AdminService;
import rmit.fp.g32_asm2.service.ClaimService;
import rmit.fp.g32_asm2.service.CustomerService;
import rmit.fp.g32_asm2.util.HashUtils;
import rmit.fp.g32_asm2.util.ViewUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


public class MyApplication extends Application {
    public static void main(String[] args){

        launch(args);

    }

    private static boolean register(String username, String password) {
        AdminService adminService = new AdminService();
        User user = new PolicyOwner.Builder()
                .withUsername(username)
                .withName(username)
                .withRoleId(CustomerType.POLICY_OWNER.getValue())
                .withHashPassword(HashUtils.hashPassword(password))
                .build();

        return adminService.addUser(user);

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        ViewUtils viewUtils = ViewUtils.getInstance();
        viewUtils.setPrimaryStage(stage);
        viewUtils.renderView("/view/common/login-view.fxml", "Login");
    }
}
