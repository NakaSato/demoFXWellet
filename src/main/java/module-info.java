module com.wellet.demofxwellet {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires javafx.swing;
    
    // Other UI libraries that are available in build.gradle.kts
    requires transitive org.controlsfx.controls;
    requires transitive com.dlsc.formsfx;
    requires transitive org.kordamp.ikonli.javafx;

    opens com.wellet.fxwellet to javafx.fxml;

    exports com.wellet.fxwellet;
    exports com.wellet.fxwellet.model;
    exports com.wellet.fxwellet.service;

}
