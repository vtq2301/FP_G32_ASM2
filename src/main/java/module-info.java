module rmit.fp.g32_asm2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens rmit.fp.g32_asm2 to javafx.fxml;
    opens rmit.fp.g32_asm2.controller to javafx.fxml;
    opens rmit.fp.g32_asm2.view to javafx.fxml;
    opens rmit.fp.g32_asm2.auth to javafx.fxml;
    opens rmit.fp.g32_asm2.model.customer to javafx.fxml;
    opens rmit.fp.g32_asm2.model.admin to javafx.fxml;
    opens rmit.fp.g32_asm2.model.provider to javafx.fxml;
    opens rmit.fp.g32_asm2.database to javafx.fxml;
    opens rmit.fp.g32_asm2.log to javafx.fxml;


    exports rmit.fp.g32_asm2;
    exports rmit.fp.g32_asm2.view;
    exports rmit.fp.g32_asm2.controller;
    exports rmit.fp.g32_asm2.auth;
    exports rmit.fp.g32_asm2.model;
    exports rmit.fp.g32_asm2.database;
    exports rmit.fp.g32_asm2.log;
}