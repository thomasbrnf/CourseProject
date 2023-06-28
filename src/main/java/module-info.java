module application.aurora {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;
    requires com.fasterxml.jackson.databind;

    exports application.aurora.windows;
    opens application.aurora.windows to javafx.fxml;
    exports application.aurora.micro_objects;
    opens application.aurora.micro_objects to javafx.fxml;
    exports application.aurora.micro_objects.tools;
    exports application.aurora.ui;
    opens application.aurora.ui to javafx.fxml;
    opens application.aurora.micro_objects.tools to com.fasterxml.jackson.databind, javafx.fxml;
    exports application.aurora.world;
    opens application.aurora.world to javafx.fxml;
}