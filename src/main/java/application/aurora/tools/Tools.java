package application.aurora.tools;

import application.aurora.Camera;
import application.aurora.MiniMap;
import application.aurora.macro_objects.HabitationModule;
import application.aurora.macro_objects.MaintenanceModule;
import application.aurora.macro_objects.Module;
import application.aurora.micro_objects.*;
import application.aurora.tools.tools_micro_objects.AstronautDestination;
import application.aurora.tools.tools_micro_objects.AstronautType;
import application.aurora.tools.tools_micro_objects.ToolsForAstronaut;
import application.aurora.windows.Creations;
import application.aurora.windows.Parameters;
import application.aurora.windows.Tabulations;
import application.aurora.macro_objects.ScientificModule;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static application.aurora.Main.astronauts;
import static application.aurora.Main.root;
import static application.aurora.tools.Constants.EXPERIENCE_THRESHOLD;

public class Tools {
    public static AstronautIntern electedAstronaut = null;
    public static List<AstronautIntern> activeAstronauts = new ArrayList<>();
    public static List<AstronautIntern> astronautsInModules = new ArrayList<>();
    public static List<Module> modules = new ArrayList<>();
    public static final MiniMap miniMap = new MiniMap();
    public static final Camera camera = new Camera();
    public static Module activeModule;

    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> Creations.displayWindow();
                case F2 -> {if(electedAstronaut != null) Parameters.displayWindow();}
                case C -> {if(electedAstronaut != null)electedAstronaut.clone();}
                case B -> astronautsToCenter();
                case TAB -> Tabulations.displayWindow();
                case DELETE -> {if(electedAstronaut != null)electedAstronaut.delete();}
                case ESCAPE -> clearActiveObjects();
                case W, S, A, D -> moveScene(keyEvent.getCode());
                case UP,DOWN,LEFT,RIGHT -> updateObjectsPosition(keyEvent.getCode());
                case DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5 -> ejectAstronautFromModule(keyEvent.getCode());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ejectAstronautFromModule(KeyCode code) {
        for(var module: modules){
            if(module.isActive());module.ejectAstronaut(code);
        }
    }

    private static void astronautsToCenter() throws FileNotFoundException {
        for(AstronautIntern astronaut: astronauts){
            if(astronaut.inModule())ejectObjectFromModule(astronaut);
            astronaut.setOnMission(true);
            astronaut.setDestination(AstronautDestination.TO_CENTER);
        }
    }
    private static void moveScene(KeyCode code) {
        switch (code) {
            case W -> {if (camera.getY() + 20 <= 0) camera.moveY(1);}
            case A -> {if (camera.getX() + 20 <= 0) camera.moveX(1);}
            case S -> {if (Math.abs(camera.getY() - 20) <= (1644 - 720)) camera.moveY(-1);}
            case D -> {if (Math.abs(camera.getX() - 20) <= (2237 - 1280)) camera.moveX(-1);}
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

        registerModules();
        initializeTimer();

        writeConsoleToFile();


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            WritableImage screen = root.snapshot(new SnapshotParameters(), null);
            Image blurredImage = smoothImage(screen, 7);
            miniMap.getView().setImage(blurredImage);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
    private static Image smoothImage(WritableImage image, int blurRadius) {
        ImageView view = new ImageView(image);
        GaussianBlur blur = new GaussianBlur(blurRadius);
        view.setEffect(blur);
        return view.snapshot(null, null);
    }
    private static void initializeTimer(){
        AnimationTimer activeWorld = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    manageLifeCycle();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        activeWorld.start();
    }
    private static void manageLifeCycle() throws IOException {
        promoteEligibleAstronauts();
        updateAstronautsDestination();
        updateAstronautsPositions();
        updateOrders();
    }
    private static void updateOrders(){
        for(var managingAstronaut: astronauts) {
            if(managingAstronaut.getType() == AstronautType.MANAGING_ASTRONAUT) {
                for(var otherAstronaut: astronauts) {
                    if(otherAstronaut.getType() != AstronautType.MANAGING_ASTRONAUT &&
                            managingAstronaut.intersects(otherAstronaut)) {
                        if(otherAstronaut.onMission())continue;
                        if(otherAstronaut.getEnergy() < 30)continue;
                        ManagingAstronaut astronaut  = (ManagingAstronaut) managingAstronaut;
                        otherAstronaut.setDestination(astronaut.getOrder());
                        otherAstronaut.setOnMission(true);
                    }
                }
            }
        }
    }
    private static void updateAstronautsDestination() {
        for(var astronaut: astronauts){
            if(astronaut.onMission())continue;
            if(astronaut.getEnergy() < 20)astronaut.setDestination(AstronautDestination.HABITATION_MODULE);
            else if (astronaut.getEnergy() > 40)astronaut.setDestination(AstronautDestination.SCIENTIFIC_MODULE);
            else{astronaut.setDestination(AstronautDestination.MAINTENANCE_MODULE);}
        }
    }
    public static int getDigitFromKeyCode(KeyCode code) {
        return switch (code) {
            case DIGIT1 -> 1;
            case DIGIT2 -> 2;
            case DIGIT3 -> 3;
            case DIGIT4 -> 4;
            case DIGIT5 -> 5;
            default -> -1;
        };
    }
    private static void promoteEligibleAstronauts() throws IOException{
        List<AstronautIntern> copy = List.copyOf(astronauts);
        for (AstronautIntern astronaut: copy) {
            if(isEligibleForPromotion(astronaut)){
                upgradeAstronaut(astronaut);
            }
        }
    }
    private static boolean isEligibleForPromotion(AstronautIntern astronaut) {
        return astronaut.getExperience() >= EXPERIENCE_THRESHOLD && astronaut.getType() != AstronautType.MANAGING_ASTRONAUT;
    }
    private static void upgradeAstronaut(AstronautIntern astronaut) throws IOException {
        if (isAstronaut(astronaut)) {
            ManagingAstronaut upgraded = createUpgradedManagingAstronaut(astronaut);
            setUpgradedAstronaut(upgraded, astronaut);
        }if(isIntern(astronaut)){
            Astronaut upgraded = createUpgradedAstronaut(astronaut);
            setUpgradedAstronaut(upgraded, astronaut);
        }
    }
    private static Astronaut createUpgradedAstronaut(AstronautIntern astronaut) throws IOException {
        return new Astronaut(astronaut.getName(), astronaut.getEnergy(), 0);
    }
    private static ManagingAstronaut createUpgradedManagingAstronaut(AstronautIntern astronaut) throws IOException {
        return new ManagingAstronaut(astronaut.getName(), astronaut.getEnergy(), 0);
    }
    private static void setUpgradedAstronaut(Astronaut upgraded, AstronautIntern astronaut) throws FileNotFoundException {
        if(astronaut.inModule())ejectObjectFromModule(astronaut);

        ToolsForAstronaut.totalObjectsCreated--;

        upgraded.setXY(astronaut.getGroup().getLayoutX(), astronaut.getGroup().getLayoutY());
        upgraded.setID(astronaut.getID());

        astronaut.delete();

        astronauts.add(upgraded);
    }
    private static boolean isIntern(AstronautIntern astronaut) {
        return astronaut.getType() == AstronautType.ASTRONAUT_INTERN;
    }
    private static boolean isAstronaut(AstronautIntern astronaut) {
        return astronaut.getType() == AstronautType.ASTRONAUT;
    }
    private static void updateAstronautsPositions() throws FileNotFoundException {
        for (AstronautIntern astronaut : astronauts) {
            if (astronaut.isActive() || astronaut.inModule() || astronaut.isElect()) {
                continue;
            }
            if (astronaut.getDestination() == AstronautDestination.HABITATION_MODULE) {
                moveObject(astronaut, HabitationModule.getInstance().getGroup().getLayoutX(), HabitationModule.getInstance().getGroup().getLayoutY());
            } else if (astronaut.getDestination() == AstronautDestination.SCIENTIFIC_MODULE) {
                moveObject(astronaut, ScientificModule.getInstance().getGroup().getLayoutX(), ScientificModule.getInstance().getGroup().getLayoutY());
            } else if(astronaut.getDestination() == AstronautDestination.MAINTENANCE_MODULE){
                moveObject(astronaut, MaintenanceModule.getInstance().getGroup().getLayoutX(), MaintenanceModule.getInstance().getGroup().getLayoutY());
            }else {moveObject(astronaut,1080, 650);}
        }
    }
    private static void moveObject(AstronautIntern astronaut, double x, double y) throws FileNotFoundException {
        double moveDistance = 1;
        double currentX = astronaut.getGroup().getLayoutX();
        double currentY = astronaut.getGroup().getLayoutY();
        double deltaX = 0;
        double deltaY = 0;
        if(currentX == x && currentY == y)astronaut.setOnMission(false);

        if (x != currentX) {
            double xDirection = Math.signum(x - currentX);
            deltaX = Math.min(moveDistance, Math.abs(x - currentX)) * xDirection;
        }

        if (y != currentY) {
            double yDirection = Math.signum(y - currentY);
            deltaY = Math.min(moveDistance, Math.abs(x - currentY)) * yDirection;
        }
        astronaut.setXY(currentX+ deltaX, currentY + deltaY);
        updateCollisionStatus(astronaut);
    }
    private static void updateObjectsPosition(KeyCode code) throws FileNotFoundException {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            moveObject(astronaut.getGroup(), code, 10);
            updateCollisionStatus(astronaut);
        }
    }
    public static void ejectObjectFromModule(AstronautIntern astronaut) {
        for(var module: modules){
            if(module.getOccupationAreas() == null){continue;}
            if (module.getOccupationAreas().containsValue(astronaut)) {
                module.ejectAstronaut(astronaut);
                astronaut.setInModule(false);
                break;
            }
        }
    }
    private static void updateCollisionStatus(AstronautIntern astronaut) throws FileNotFoundException {
        for (var module : modules) {
            if (astronaut.getGroup().getBoundsInParent().intersects(module.getGroup().getBoundsInParent())) {
                if(!module.getOccupationAreas().containsValue(astronaut) && module.getOccupationAreas().containsValue(null)) {
                    module.setAstronaut(astronaut);
                    astronaut.setInModule(true);
                break;}
            }
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
            case 3 -> {Stream<AstronautIntern> spaceWalksFilter = astronauts.stream().filter(astronaut -> ((ManagingAstronaut)astronaut).getQuantityOfSpaceWalks() == Integer.parseInt(text));
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
    }
    private static void clearActiveObjects() throws FileNotFoundException {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            astronaut.toggleElect();
        }
        activeAstronauts.clear();
    }
    public static int getRandomNumber(AstronautIntern astronaut){
        double chance = getSuccessChance(astronaut);
        return Math.random() < chance ? 0 : 1;
    }
    public static double getSuccessChance(AstronautIntern astronaut){
        if(astronaut instanceof ManagingAstronaut){
            return 0.1;
        }
        if(astronaut instanceof Astronaut){
            return 0.3;
        }
        return 0.5;
    }
    public static double getAmountOfExperience(AstronautIntern astronaut){
        if(astronaut instanceof Astronaut){
            return 0.4;
        }
        return 0.2;
    }
    private static void writeConsoleToFile() throws FileNotFoundException {
//        PrintStream consoleOut = System.out;
//        File file = new File("logs.txt");
//        FileOutputStream fos = new FileOutputStream(file);
//        PrintStream ps = new PrintStream(fos);
//        System.setOut(ps);
    }
    public static void setOnMouseClicked(MouseEvent mouseEvent)  {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(miniMap.getView().contains(mouseEvent.getX(), mouseEvent.getY())) {
                double x = camera.calculate(mouseEvent.getX(), 0);
                double y = camera.calculate(mouseEvent.getY(), 1);
                camera.moveCameraX(-x);
                camera.moveCameraY(-y);
                return;
            }
        }
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            AstronautIntern astronaut = checkClickAstronaut(astronauts.stream().toList(),
                    mouseEvent.getX(), mouseEvent.getY());
            if (astronaut != null)astronaut.toggleActive();
        }
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          Module module = checkClickModule(modules.stream().toList(), mouseEvent.getX(), mouseEvent.getY());
          if(module != null)module.toggleActive();
        }
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            AstronautIntern astronaut = checkClickAstronaut(astronauts.stream().toList(),
                    mouseEvent.getX(), mouseEvent.getY());
            if (astronaut != null)astronaut.toggleElect();
        }

    }

    private static Module checkClickModule(List<Module> list, double x, double y) {
        for(var module: list)
            if(module.getGroup().getBoundsInParent().contains(
                    x - root.getTranslateX(),y - root.getTranslateY())
                    && module.getOccupationAreas() != null)
                return module;
        return null;
    }

    public static AstronautIntern checkClickAstronaut(List<AstronautIntern> list, double x, double y) {
        for (var astronaut : list)
            if (astronaut.getGroup().getBoundsInParent().contains(
                    x - root.getTranslateX(),
                    y - root.getTranslateY()) && !astronaut.inModule())
                return astronaut;
        return null;
    }

}
