package rmit.fp.g32_asm2;

import javafx.stage.Stage;
import javafx.application.Application;

import rmit.fp.g32_asm2.DAO.UserDAO;
import rmit.fp.g32_asm2.model.RolesEnum;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.admin.SystemAdmin;
import rmit.fp.g32_asm2.model.customer.Customer;
import rmit.fp.g32_asm2.model.customer.CustomerRelations;
import rmit.fp.g32_asm2.model.customer.CustomerType;
import rmit.fp.g32_asm2.model.customer.PolicyOwner;
import rmit.fp.g32_asm2.service.AdminService;
import rmit.fp.g32_asm2.service.CustomerService;
import rmit.fp.g32_asm2.util.HashUtils;
import rmit.fp.g32_asm2.util.ViewUtils;

import java.util.UUID;


public class MyApplication extends Application {
    public static void main(String[] args){

        launch(args);
//        AdminService adminService = new AdminService();
//        CustomerService customerService = new CustomerService();

//        register("po001", "abc123");
//        register("po002", "abc123");
//        PolicyOwner po1 = (PolicyOwner) adminService.getUserByUsernameAndPassword("po001", "abc123");
//        PolicyOwner po2 = (PolicyOwner) adminService.getUserByUsernameAndPassword("po002", "abc123");

//        CustomerRelations cr1 = new CustomerRelations(po1.getId(), po1.getRoleId(),null, null, 5.0);
//        CustomerRelations cr2 = new CustomerRelations(po2.getId(), po2.getRoleId(),null, null, 8.0);
//
//        CustomerRelations cr1 = new CustomerRelations();
//        cr1.setInsuranceRate(5.0);
//        cr1.setUserId(po1.getId());
//        cr1.setRoleId(po1.getRoleId());
//
//        customerService.addCustomer(cr1);
//        customerService.addCustomer(cr2);

//        System.out.println(po1);
//        System.out.println(po2);
//
//
//        po1 = (PolicyOwner) adminService.getUserByUsernameAndPassword("po001", "abc123");
//        po2 = (PolicyOwner) adminService.getUserByUsernameAndPassword("po002", "abc123");
//        System.out.println(po1);
//        System.out.println(po2);
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
