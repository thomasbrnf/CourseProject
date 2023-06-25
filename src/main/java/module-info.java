module application.aurora {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;

    opens application.aurora to javafx.fxml;
    exports application.aurora;
    exports application.aurora.windows;
    opens application.aurora.windows to javafx.fxml;
    exports application.aurora.micro_objects;
    opens application.aurora.micro_objects to javafx.fxml;
    exports application.aurora.tools.tools_micro_objects;
    opens application.aurora.tools.tools_micro_objects to javafx.fxml;
}