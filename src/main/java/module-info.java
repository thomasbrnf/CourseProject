module codegame.aurora {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;

    opens codegame.aurora to javafx.fxml;
    exports codegame.aurora;
    exports codegame.aurora.windows;
    opens codegame.aurora.windows to javafx.fxml;
    exports codegame.aurora.micro_objects;
    opens codegame.aurora.micro_objects to javafx.fxml;
}