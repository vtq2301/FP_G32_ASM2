package rmit.fp.g32_asm2.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {
    private static Routes instance;

    // Maps to store routes for different roles and sub-roles
    private final Map<String, Map<String, String>> routes = new HashMap<>();

    private Routes() {
        initializeRoutes();
    }

    public static synchronized Routes getInstance() {
        if (instance == null) {
            instance = new Routes();
        }
        return instance;
    }

    private void initializeRoutes() {
        // Initialize routes for customer roles
        Map<String, String> dependentRoutes = new HashMap<>();
        dependentRoutes.put("dependentDashboard", "/view/customer/dependent/dashboard.fxml");

        Map<String, String> policyHolderRoutes = new HashMap<>();
        policyHolderRoutes.put("Dashboard", "/view/customer/policy-holder/dashboard.fxml");

        Map<String, String> policyOwnerRoutes = new HashMap<>();
        policyOwnerRoutes.put("Dashboard", "/view/customer/policy-owner/dashboard.fxml");

        // Initialize routes for provider roles
        Map<String, String> managerRoutes = new HashMap<>();
        managerRoutes.put("Dashboard", "/view/provider/manager/dashboard.fxml");

        Map<String, String> surveyorRoutes = new HashMap<>();
        surveyorRoutes.put("Dashboard", "/view/provider/surveyor/dashboard.fxml");

        // Initialize routes for admin roles
        Map<String, String> adminRoutes = new HashMap<>();
        adminRoutes.put("Dashboard", "/view/admin/dashboard.fxml");

        // Add routes to the main map
        routes.put("PolicyHolder", policyHolderRoutes);
        routes.put("Dependent", dependentRoutes);
        routes.put("PolicyOwner", policyOwnerRoutes);
        routes.put("Manager", managerRoutes);
        routes.put("Surveyor", surveyorRoutes);
        routes.put("SystemAdmin", adminRoutes);
    }

    public String getRoute(String role, String routeName) {
        Map<String, String> roleRoutes = routes.get(role);
        if (roleRoutes != null) {
            return roleRoutes.get(routeName);
        }
        return null;
    }

    public List<String> getAvailableRoutes(String role) {
        Map<String, String> roleRoutes = routes.get(role);
        if (roleRoutes != null) {
            return List.copyOf(roleRoutes.keySet());
        }
        return List.of();
    }

//    public static void main(String[] args) {
//        // Test the Routes class
//        Routes routes = Routes.getInstance();
//        System.out.println(routes.getRoute("customer", "policyHolderDashboard"));
//        System.out.println(routes.getAvailableRoutes("customer"));
//    }
}
