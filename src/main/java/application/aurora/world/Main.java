package application.aurora.world;

import application.aurora.micro_objects.AstronautIntern;
import application.aurora.tools.KeyEventHandler;
import application.aurora.tools.MouseEventHandler;
import application.aurora.tools.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static application.aurora.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.*;

public class Main extends Application {
    public static List<AstronautIntern> astronauts = new ArrayList<>();
    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Scene scene = new Scene(getStackPane(),SCENE_WIDTH,SCENE_HEIGHT);
        stage.setTitle("Project Aurora");
        stage.getIcons().add(getLogo());

        Tools.initializeEnvironment();
        scene.setOnKeyPressed(KeyEventHandler::setOnKeyPressed);
        scene.setOnMouseClicked(MouseEventHandler::setOnMouseClicked);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}