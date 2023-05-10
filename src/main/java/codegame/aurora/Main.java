package codegame.aurora;

import codegame.aurora.action.Movement;
import codegame.aurora.astros.Astronaut;
import codegame.aurora.astros.AstronautIntern;
import codegame.aurora.astros.ManagingAstronaut;
import codegame.aurora.tools.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    public static Pane root = new Pane();
    public static final double sizeX = 1280;
    public static final double sizeY = 720;
    public static List<AstronautIntern> activeAstros = new ArrayList<>();

    public static Map<AstronautIntern, Boolean> astros = new HashMap<>();
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(root,1280,720);
        stage.setTitle("Project Aurora");

        Tools.initializeUI();
        scene.setOnKeyPressed(Tools::setOnKeyPressed);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}