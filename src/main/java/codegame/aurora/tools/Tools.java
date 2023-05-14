package codegame.aurora.tools;

import codegame.aurora.astros.Astronaut;
import codegame.aurora.astros.AstronautIntern;
import codegame.aurora.astros.ManagingAstronaut;
import codegame.aurora.dialogs.AstroCreatorController;
import codegame.aurora.dialogs.AstroEditorController;
import codegame.aurora.dialogs.AstroTabController;
import codegame.aurora.modules.HabitationModule;
import codegame.aurora.modules.MaintenanceModule;
import codegame.aurora.modules.ScientificModule;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codegame.aurora.Main.astros;
import static codegame.aurora.Main.root;

public class Tools {
    public static final double windowSizeX = 1280;
    public static final double windowSizeY = 720;
    public static AstronautIntern astroToEdit = null;
    public static List<AstronautIntern> activeAstros = new ArrayList<>();
    public static List<Object> modules = new ArrayList<>();
    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> AstroCreatorController.showDialog();
                case F2 -> {if(astroToEdit != null) AstroEditorController.showDialog();}
                case F7 -> {if(!activeAstros.isEmpty())cloneObject();}
                case TAB -> AstroTabController.showDialog();
                case DELETE -> {if(!activeAstros.isEmpty())deleteObject();}
                case ESCAPE -> clearActiveObjects();
                case UP,DOWN,LEFT,RIGHT -> moveObject(keyEvent.getCode());
                case W, S, A, D -> moveScene(keyEvent.getCode());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void moveScene(KeyCode code) {
        double maxX = root.getBoundsInParent().getWidth() - windowSizeX;
        double maxY = root.getBoundsInParent().getHeight() - windowSizeY;
        switch (code) {
            case W -> root.setTranslateY(Math.min(root.getTranslateY() + 10, 0));
            case A -> root.setTranslateX(Math.min(root.getTranslateX() + 10, 0));
            case S -> root.setTranslateY(Math.max(root.getTranslateY() - 10, -maxY));
            case D -> root.setTranslateX(Math.max(root.getTranslateX() - 10, -maxX));
        }
    }
    public static void setOnMouseClicked(MouseEvent mouseEvent) {
//        System.out.println(mouseEvent.toString());
    }
    public static void registerAstronaut(String name, int Class, int experience, int energy) throws IOException {
        if(Class == 0) astros.add(new AstronautIntern(name, energy, experience));
        else if(Class == 1) astros.add(new Astronaut(name, energy, experience));
        else if(Class == 2) astros.add(new ManagingAstronaut(name, energy, experience));
    }
    public static void registerModules(){
        modules.add(MaintenanceModule.getInstance());
        modules.add(HabitationModule.getInstance());
        modules.add(ScientificModule.getInstance());
    }
    public static void initializeUI() {
        ImageView ship = new ImageView("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\ship.png");
        root.getChildren().add(ship);
        double centerX = ship.getImage().getWidth() / 2;
        double centerY = ship.getImage().getHeight() / 2;
        root.setTranslateX(-centerX + windowSizeX / 2);
        root.setTranslateY(-centerY + windowSizeY / 2);
        initializeCollisionBox();
        registerModules();
    }
    public static Rectangle initializeCollisionBox(){
        Rectangle collisionBox = new Rectangle();
        collisionBox.setHeight(1350);
        collisionBox.setWidth(1700);
        collisionBox.setFill(Color.ORANGE);
        collisionBox.setVisible(false);
        collisionBox.setLayoutX(300);
        collisionBox.setLayoutY(150);
        root.getChildren().add(collisionBox);

        return collisionBox;
    }
    private static void moveObject(KeyCode code){
        List<AstronautIntern> copy = List.copyOf(activeAstros);
        for(var astro: copy){
            astro.toMove(code);
        }
    }
    private static void clearActiveObjects(){
        List<AstronautIntern> copy = List.copyOf(activeAstros);
        for (var astro : copy) {
            astro.setActive();
        }
        activeAstros.clear();
    }
    private static void cloneObject() {
        List<AstronautIntern> copy = List.copyOf(activeAstros);
        for (var astro : copy) {
            astro.clone();
        }
    }
    private static void deleteObject(){
        for (var astro: activeAstros) {
            astro.delete();
        }
        activeAstros.clear();
        astroToEdit = null;
    }
}
