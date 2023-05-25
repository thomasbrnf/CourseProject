package application.aurora.tools;

import application.aurora.macro_objects.HabitationModule;
import application.aurora.macro_objects.MaintenanceModule;
import application.aurora.macro_objects.Module;
import application.aurora.micro_objects.Astronaut;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;
import application.aurora.windows.Creations;
import application.aurora.windows.Parameters;
import application.aurora.windows.Tabulations;
import application.aurora.macro_objects.ScientificModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static application.aurora.Main.astronauts;
import static application.aurora.Main.root;

public class Tools {
    public static final double windowSizeX = 1280;
    public static final double windowSizeY = 720;
    public static AstronautIntern electedAstronaut = null;
    public static List<AstronautIntern> activeAstronauts = new ArrayList<>();
    public static List<Module> modules = new ArrayList<>();
    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> Creations.displayWindow();
                case F2 -> {if(electedAstronaut != null) Parameters.displayWindow();}
                case C -> {if(electedAstronaut != null)electedAstronaut.clone();}
                case TAB -> Tabulations.displayWindow();
                case DELETE -> {if(electedAstronaut != null)electedAstronaut.delete();}
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
    public static void registerAstronaut(String name, int Class, int experience, int energy) throws IOException {
        if(Class == 0) astronauts.add(new AstronautIntern(name, energy, experience));
        else if(Class == 1) astronauts.add(new Astronaut(name, energy, experience));
        else if(Class == 2) astronauts.add(new ManagingAstronaut(name, energy, experience));
    }
    public static void registerModules() throws FileNotFoundException {
        modules.add(MaintenanceModule.getInstance());
        modules.add(HabitationModule.getInstance());
        modules.add(ScientificModule.getInstance());
    }
    public static void initializeEnvironment() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/map.png"));
        ImageView ship = new ImageView(image);
        root.getChildren().add(ship);

        double centerX = ship.getImage().getWidth() / 2;
        double centerY = ship.getImage().getHeight() / 2;

        root.setTranslateX(-centerX + windowSizeX / 2);
        root.setTranslateY(-centerY + windowSizeY / 2);

        initializeCollisionBox();
        registerModules();

        writeConsoleToFile();
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
            updateCollisionStatus(astronaut);
        }
    }
    private static void updateCollisionStatus(AstronautIntern astronaut) {
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
    public static ObservableList<AstronautIntern> driftingObjects() {
        List<AstronautIntern> objects = new ArrayList<>();
        Map<AstronautIntern, Set<Module>> astronautModules = new HashMap<>();
        for (var module : modules) {
            for (var astronaut : module.getOccupationAreas().values()) {
                astronautModules.computeIfAbsent(astronaut, k -> new HashSet<>()).add(module);
            }
        }
        for (var astronaut : astronauts) {
            if (!astronautModules.containsKey(astronaut)) {
                objects.add(astronaut);
            }
        }
        return FXCollections.observableArrayList(objects);
    }
    public static ObservableList<AstronautIntern> objectsInModule(String module) throws FileNotFoundException {
        List<AstronautIntern> objects = new ArrayList<>();
        switch (module) {
            case "Habitation" -> objects.addAll(HabitationModule.getInstance().getOccupationAreas().values());
            case "Scientific" -> objects.addAll(ScientificModule.getInstance().getOccupationAreas().values());
            case "Maintenance" -> objects.addAll(MaintenanceModule.getInstance().getOccupationAreas().values());
        }
        return FXCollections.observableArrayList(objects);
    }
    public static ObservableList<AstronautIntern> objectsWithLowEnergy() {
        Stream<Integer> energyStream = astronauts.stream().map(AstronautIntern::getEnergy).filter(e -> e < 30);
        return FXCollections.observableArrayList(energyStream.map(e -> astronauts.stream()
                        .filter(a -> a.getEnergy() == e)
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }
    public static ObservableList<AstronautIntern> allObjectsInWorld(){
        return FXCollections.observableArrayList(astronauts);
    }
    public static ObservableList<AstronautIntern> filterObjectsBy(String text, int type){
        switch(type){
            case 1 -> {Stream<AstronautIntern> experienceFilter = astronauts.stream().filter(astronaut -> astronaut.getExperience() == Integer.parseInt(text));
                return FXCollections.observableArrayList(experienceFilter.toList());}
            case 2 -> {
                Stream<AstronautIntern> nameFilter = astronauts.stream().filter(astronaut -> astronaut.getName().contains(text));
                return FXCollections.observableArrayList(nameFilter.toList());}
            case 3 -> {Stream<AstronautIntern> spaceWalksFilter = astronauts.stream().filter(astronaut -> astronaut.getQuantityOfSpaceWalks() == Integer.parseInt(text));
                return FXCollections.observableArrayList(spaceWalksFilter.toList());}
        }
        return null;
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
            astronaut.setElect();
        }
        activeAstronauts.clear();
    }
    private static void writeConsoleToFile() throws FileNotFoundException {
        PrintStream consoleOut = System.out;
        File file = new File("logs.txt");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
    }
}
