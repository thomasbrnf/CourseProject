package codegame.aurora;

import codegame.aurora.micro_objects.AstronautIntern;
import codegame.aurora.tools.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static Pane root = new Pane();
    public static List<AstronautIntern> astronauts = new ArrayList<>();
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(root,1280,720);
        stage.setTitle("Project Aurora");

        Tools.initializeUI();
        scene.setOnKeyPressed(Tools::setOnKeyPressed);
        scene.setOnMouseClicked(Tools::setOnMouseClicked);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}