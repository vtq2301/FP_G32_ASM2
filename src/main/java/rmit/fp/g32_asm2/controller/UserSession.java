package rmit.fp.g32_asm2.controller;


import rmit.fp.g32_asm2.model.User;

public class UserSession {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }

}
