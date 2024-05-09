module all {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    // Open to javafx.fxml for reflection by FXMLLoader
    opens all.controller to javafx.fxml;
    opens all.controller.customer to javafx.fxml;
    opens all.model.customer to javafx.fxml;

    // Export packages if necessary for other uses
    exports all.controller;
    exports all.controller.customer;
    exports all.model.customer;

    // Exports the root package if there are any common utilities or classes directly under it
    exports all;
    exports all.auth;
    opens all.auth to javafx.fxml;
}
