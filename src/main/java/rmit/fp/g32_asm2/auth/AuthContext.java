package rmit.fp.g32_asm2.auth;

import rmit.fp.g32_asm2.model.User;

public class AuthContext {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getCurrentUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
