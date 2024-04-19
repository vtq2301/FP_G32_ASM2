module all {
    requires javafx.controls;
    requires javafx.fxml;

    opens all.controllers to javafx.fxml;
    opens all.models to javafx.fxml;

    exports all; // Export the root package of your all module
}
