package codegame.aurora.tools;

import codegame.aurora.astros.Astronaut;
import codegame.aurora.astros.AstronautIntern;
import codegame.aurora.astros.ManagingAstronaut;
import codegame.aurora.dialogs.AstroCreatorController;
import codegame.aurora.dialogs.AstroEditorController;
import codegame.aurora.dialogs.AstroTabController;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Stack;

import static codegame.aurora.Main.astros;
import static codegame.aurora.Main.root;

public class Tools {
    public static AstronautIntern astroToEdit = null;
    public static void registerAstronaut(String name, int Class, int experience, int energy) throws IOException {
        if(Class == 0) astros.put(new AstronautIntern(name, energy, experience), false);
        else if(Class == 1) astros.put(new Astronaut(name, energy, experience), false);
        else if(Class == 2) astros.put(new ManagingAstronaut(name, energy, experience), false);
    }
    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> AstroCreatorController.showDialog();
                case F2 -> {if(astroToEdit != null) AstroEditorController.showDialog();}
                case TAB -> AstroTabController.showDialog();
//                case DELETE -> {if(activeAstros != null)activeAstros.delete();}
                case P -> AstronautIntern.driftStop();
                case O -> AstronautIntern.driftResume();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void initializeUI() {
        ImageView background = new ImageView("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\gray.png");
        root.getChildren().add(background);
    }
}
