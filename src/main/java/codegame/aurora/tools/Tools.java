package codegame.aurora.tools;

import codegame.aurora.micro_objects.Astronaut;
import codegame.aurora.micro_objects.AstronautIntern;
import codegame.aurora.micro_objects.ManagingAstronaut;
import codegame.aurora.windows.Creations;
import codegame.aurora.windows.Parameters;
import codegame.aurora.windows.Tabulations;
import codegame.aurora.macro_objects.HabitationModule;
import codegame.aurora.macro_objects.MaintenanceModule;
import codegame.aurora.macro_objects.Module;
import codegame.aurora.macro_objects.ScientificModule;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codegame.aurora.Main.astronauts;
import static codegame.aurora.Main.root;

public class Tools {
    public static final double windowSizeX = 1280;
    public static final double windowSizeY = 720;
    public static AstronautIntern astronautToEdit = null;
    public static List<AstronautIntern> activeAstronauts = new ArrayList<>();
    public static List<Module> modules = new ArrayList<>();
    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> Creations.showDialog();
                case F2 -> {if(astronautToEdit != null) Parameters.showDialog();}
                case F7 -> {if(!activeAstronauts.isEmpty())cloneObject();}
                case TAB -> Tabulations.showDialog();
                case DELETE -> {if(!activeAstronauts.isEmpty())deleteObject();}
                case ESCAPE -> clearActiveObjects();
                case UP,DOWN,LEFT,RIGHT -> updateObjectsPosition(keyEvent.getCode());
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
            case W -> root.setTranslateY(Math.min(root.getTranslateY() + 20, 0));
            case A -> root.setTranslateX(Math.min(root.getTranslateX() + 20, 0));
            case S -> root.setTranslateY(Math.max(root.getTranslateY() - 20, -maxY));
            case D -> root.setTranslateX(Math.max(root.getTranslateX() - 20, -maxX));
        }
    }
    public static void setOnMouseClicked(MouseEvent mouseEvent) {
//        System.out.println(mouseEvent.toString());
    }
    public static void registerAstronaut(String name, int Class, int experience, int energy) throws IOException {
        if(Class == 0) astronauts.add(new AstronautIntern(name, energy, experience));
        else if(Class == 1) astronauts.add(new Astronaut(name, energy, experience));
        else if(Class == 2) astronauts.add(new ManagingAstronaut(name, energy, experience));
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
    public static void initializeCollisionBox(){
        Rectangle collisionBox = new Rectangle();
        collisionBox.setHeight(1350);
        collisionBox.setWidth(1700);
        collisionBox.setFill(Color.ORANGE);
        collisionBox.setVisible(false);
        collisionBox.setLayoutX(300);
        collisionBox.setLayoutY(150);
        root.getChildren().add(collisionBox);

    }
    private static void updateObjectsPosition(KeyCode code) {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            moveObject(astronaut.getGroup(), code, 10);
            checkCollision(astronaut);
        }
    }
    private static void checkCollision(AstronautIntern astronaut) {
        boolean isCollided = false;
        for (var module : modules) {
            if (astronaut.getGroup().getBoundsInParent().intersects(module.getGroup().getBoundsInParent())) {
               if(!module.getOccupationAreas().containsValue(astronaut)) {module.setAstronaut(astronaut);}
                astronaut.getGroup().setOpacity(0.5);
                isCollided = true;
            } else {
                module.removeAstronaut(astronaut);
            }
        }
        if (!isCollided) {
            astronaut.getGroup().setOpacity(1);
        }
    }
    private static void moveObject(Group group, KeyCode code, int distance) {
        double x = group.getLayoutX();
        double y = group.getLayoutY();
        switch (code) {
            case UP -> y -= distance;
            case DOWN -> y += distance;
            case LEFT -> x -= distance;
            case RIGHT -> x += distance;
        }
        group.setLayoutX(x);
        group.setLayoutY(y);
        System.out.println("Group position: x=" + x + ", y=" + y);
    }
    private static void clearActiveObjects(){
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            astronaut.setActive();
        }
        activeAstronauts.clear();
    }
    private static void cloneObject() {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            astronaut.clone();
        }
    }
    private static void deleteObject(){
        for (var astronaut : activeAstronauts) {
            astronaut.delete();
        }
        activeAstronauts.clear();
        astronautToEdit = null;
    }
}
