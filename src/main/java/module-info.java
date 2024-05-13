module all {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;


    opens all.controller to javafx.fxml;
    opens all.controller.customer to javafx.fxml;
    opens all.model.customer to javafx.fxml;
    opens all.controller.insurance to javafx.fxml;


    exports all.controller;
    exports all.controller.customer;
    exports all.model.customer;
    exports all.controller.insurance to javafx.fxml;



    exports all;
    exports all.auth;
    opens all.auth to javafx.fxml;
}
