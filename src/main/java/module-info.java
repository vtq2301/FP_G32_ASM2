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

    opens rmit.fp.g32_asm2 to javafx.fxml;
    exports rmit.fp.g32_asm2;
    exports rmit.fp.g32_asm2.view;
    opens rmit.fp.g32_asm2.view to javafx.fxml;

    exports rmit.fp.g32_asm2.controller.customer.policyHolder;
    opens rmit.fp.g32_asm2.controller.customer.policyHolder to javafx.fxml;
    exports rmit.fp.g32_asm2.controller.components;
    opens rmit.fp.g32_asm2.controller.components to javafx.fxml;
    exports rmit.fp.g32_asm2.controller.common;
    opens rmit.fp.g32_asm2.controller.common to javafx.fxml;
    exports rmit.fp.g32_asm2.controller.admin;
    opens rmit.fp.g32_asm2.controller.admin to javafx.fxml;
    exports rmit.fp.g32_asm2.controller.customer.policyOwner;
    opens rmit.fp.g32_asm2.controller.customer.policyOwner to javafx.fxml;
}