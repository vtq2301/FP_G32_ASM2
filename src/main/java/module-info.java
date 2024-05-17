module rmit.fp.g32_asm2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires org.postgresql.jdbc;

    // Open specific packages for JavaFX FXML reflection
    opens rmit.fp.g32_asm2 to javafx.fxml;
    opens rmit.fp.g32_asm2.view to javafx.fxml;
    opens rmit.fp.g32_asm2.model.Claim to javafx.fxml;
    opens rmit.fp.g32_asm2.model to javafx.fxml;
    opens rmit.fp.g32_asm2.model.customer to javafx.fxml;
    opens rmit.fp.g32_asm2.model.provider to javafx.fxml;
    opens rmit.fp.g32_asm2.controller.customer.policyHolder to javafx.fxml;
    opens rmit.fp.g32_asm2.controller.components to javafx.fxml;
    opens rmit.fp.g32_asm2.controller.common to javafx.fxml;
    opens rmit.fp.g32_asm2.controller.admin to javafx.fxml;
    opens rmit.fp.g32_asm2.controller.customer.policyOwner to javafx.fxml;

    // Export packages to be used by other modules
    exports rmit.fp.g32_asm2;
    exports rmit.fp.g32_asm2.view;
    exports rmit.fp.g32_asm2.model.Claim;
    exports rmit.fp.g32_asm2.model;
    exports rmit.fp.g32_asm2.model.customer;
    exports rmit.fp.g32_asm2.model.provider;
    exports rmit.fp.g32_asm2.controller.customer.policyHolder;
    exports rmit.fp.g32_asm2.controller.components;
    exports rmit.fp.g32_asm2.controller.common;
    exports rmit.fp.g32_asm2.controller.admin;
    exports rmit.fp.g32_asm2.controller.customer.policyOwner;
}
