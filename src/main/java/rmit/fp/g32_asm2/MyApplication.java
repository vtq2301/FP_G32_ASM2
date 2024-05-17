package rmit.fp.g32_asm2;

import javafx.stage.Stage;
import javafx.application.Application;

import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.customer.CustomerType;
import rmit.fp.g32_asm2.model.customer.PolicyOwner;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.util.HashUtils;
import rmit.fp.g32_asm2.util.ViewUtils;


public class MyApplication extends Application {
    public static void main(String[] args){

        launch(args);

    }

    private static boolean register(String username, String password) {
        UserService userService = new UserService();
        User user = new PolicyOwner.Builder()
                .withUsername(username)
                .withName(username)
                .withRoleId(CustomerType.POLICY_OWNER.getValue())
                .withHashPassword(HashUtils.hashPassword(password))
                .build();

        return userService.addUser(user);

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        ViewUtils viewUtils = ViewUtils.getInstance();
        viewUtils.setPrimaryStage(stage);
        viewUtils.renderView("/view/common/login-view.fxml", "Login");
    }
}
