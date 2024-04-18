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

    opens rmit.fp.g32_asm2 to javafx.fxml;
    exports rmit.fp.g32_asm2;
    exports rmit.fp.g32_asm2.view;
    opens rmit.fp.g32_asm2.view to javafx.fxml;
    exports rmit.fp.g32_asm2.controller;
    opens rmit.fp.g32_asm2.controller to javafx.fxml;
}