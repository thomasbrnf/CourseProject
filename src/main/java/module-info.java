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
    exports codegame.aurora.dialogs;
    opens codegame.aurora.dialogs to javafx.fxml;
    exports codegame.aurora.astros;
    opens codegame.aurora.astros to javafx.fxml;
}