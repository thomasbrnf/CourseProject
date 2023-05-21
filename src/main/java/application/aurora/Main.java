package application.aurora;

import application.aurora.micro_objects.AstronautIntern;
import application.aurora.tools.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static Pane root = new Pane();
    public static List<AstronautIntern> astronauts = new ArrayList<>();
    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Scene scene = new Scene(root,1280,720);
        stage.setTitle("Project Aurora");

        Tools.initializeEnvironment();
        scene.setOnKeyPressed(Tools::setOnKeyPressed);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}