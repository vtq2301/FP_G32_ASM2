module all {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    // Open to javafx.fxml for reflection by FXMLLoader
    opens all.controller to javafx.fxml;

    // Export packages if necessary for other uses
    exports all.controller;
    exports all;
    exports all.model;
    opens all.model to javafx.fxml;
}
