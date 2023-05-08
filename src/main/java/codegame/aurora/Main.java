package codegame.aurora;

import codegame.aurora.action.Movement;
import codegame.aurora.astros.Astronaut;
import codegame.aurora.astros.AstronautIntern;
import codegame.aurora.astros.ManagingAstronaut;
import codegame.aurora.dialogs.AstroCreatorController;
import codegame.aurora.dialogs.AstroEditorController;
import codegame.aurora.dialogs.AstroTabController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    public static Pane root = new Pane();
    public static final double sizeX = 1280;
    public static final double sizeY = 720;
    public static AstronautIntern activeAstro = null;

    public static void createAstro(String name, int Class, int experience, int energy) throws IOException {
        if(Class == 0) astros.put(new AstronautIntern(name, energy, experience), false);
        else if(Class == 1) astros.put(new Astronaut(name, energy, experience), false);
        else if(Class == 2) astros.put(new ManagingAstronaut(name, energy, experience), false);
    }
    public static Map<AstronautIntern, Boolean> astros = new HashMap<>();
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(root,sizeX,sizeY);
        stage.setTitle("Project Aurora");
        ImageView background = new ImageView("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\gray.png");
        root.getChildren().add(background);
        scene.setOnKeyPressed(e -> {
            try {
                    switch (e.getCode()) {
                        case F1 -> AstroCreatorController.showDialog();
                        case F2 -> {if(activeAstro != null)AstroEditorController.showDialog();}
                        case TAB -> AstroTabController.showDialog();
                        case DELETE -> {if(activeAstro != null)activeAstro.delete();}
                        case P -> AstronautIntern.driftStop();
                        case O -> AstronautIntern.driftResume();
                    }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}