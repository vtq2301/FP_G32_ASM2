package rmit.fp.g32_asm2.auth;

import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.service.ClaimService;
import rmit.fp.g32_asm2.service.CustomerService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthService {
    private static final UserService userService = new UserService();
    private static final CustomerService customerService = new CustomerService();
    private static final ClaimService claimService = new ClaimService();
    public static boolean login(String usernameOrEmail, String password) {

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            System.out.println("Username/email and password cannot be empty.");
            return false;
        }

        User user = null;

        if (isValidEmail(usernameOrEmail)) {
            System.out.println("email is valid.");
            user = userService.getUserByEmailAndPassword(usernameOrEmail, password);
        } else {
            user = userService.getUserByUsernameAndPassword(usernameOrEmail, password);
        }

        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        if (!user.getIsActive()) {
            System.out.println("User is not active.");
            return false;
        }


        AuthContext.setCurrentUser(user);
        System.out.println("Logged in. Welcome " + user);
        return true;
    }

    public static void logout() {
        User currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }
        AuthContext.clear();
        System.out.println("Logged out.");
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+\\w{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
