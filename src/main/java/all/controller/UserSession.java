    package all.controller;

    import all.model.customer.User;

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

        public static User getUser() {
            return new User (
                    currentUser.getUsername(),
                    currentUser.getId(),
                    currentUser.getPassword(),
                    currentUser.getRole(),
                    currentUser.getFullName(),
                    currentUser.getAddress(),
                    currentUser.getPhoneNumber(),
                    currentUser.getPolicyHolderId()
            );
        }

    }
