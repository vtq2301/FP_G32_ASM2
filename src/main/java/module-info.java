module all {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.postgresql.jdbc;

    opens all.controller to javafx.fxml;
    opens all.controller.customer to javafx.fxml;
    opens all.model.customer to javafx.fxml;
    opens all.controller.insurance to javafx.fxml;
    opens all.controller.policyOwner to javafx.fxml;
    opens all.model to javafx.fxml;

    exports all.controller;
    exports all.controller.customer;
    exports all.model;
    exports all.model.customer;
    exports all.controller.insurance to javafx.fxml;
    exports all.controller.policyOwner;
    exports all.service;


    exports all;
    opens all to javafx.fxml;
    exports all.auth;
    opens all.auth to javafx.fxml;
}
