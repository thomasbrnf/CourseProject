package application.aurora.tools;

import application.aurora.world.Camera;
import application.aurora.macro_objects.HabitationModule;
import application.aurora.macro_objects.MaintenanceModule;
import application.aurora.macro_objects.Module;
import application.aurora.micro_objects.*;
import application.aurora.ui.UserInterface;
import application.aurora.macro_objects.ScientificModule;
import javafx.animation.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

import static application.aurora.micro_objects.tools.AstronautTools.*;

public class Tools {
    private static final Pane root = new Pane();
    private static final UserInterface userInterface = new UserInterface();
    private static final StackPane stackPane = new StackPane(root, Tools.userInterface.getUIPane());
    private static final List<Module> modules = new ArrayList<>();
    private static final Camera camera = new Camera();
    private static final Image logo;
    static {
        try {
            logo = new Image(new FileInputStream("src/images/logo.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void initializeEnvironment() throws FileNotFoundException {
        initializeShip();
        registerModules();
        initializeWorldTimer();
        startMiniMapUpdate();
    }
    public static void initializeShip() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/map.png"));
        ImageView ship = new ImageView(image);
        root.getChildren().add(ship);
    }
    public static void registerModules() throws FileNotFoundException {
        modules.add(MaintenanceModule.getInstance());
        modules.add(HabitationModule.getInstance());
        modules.add(ScientificModule.getInstance());
    }
    private static void initializeWorldTimer(){
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
        updateAstronautsPosition();
        updateOrders();
    }
    private static void updateOrders() {
        updateManagedAstronauts();
    }
    public static void startMiniMapUpdate() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            WritableImage screen = root.snapshot(new SnapshotParameters(), null);
            Image blurredImage = smoothImage(screen);
            userInterface.getMiniMap().getView().setImage(blurredImage);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private static Image smoothImage(WritableImage image) {
        ImageView view = new ImageView(image);
        GaussianBlur blur = new GaussianBlur(7);
        view.setEffect(blur);
        return view.snapshot(null, null);
    }
    public static void updateCollisionStatus(AstronautIntern astronaut) throws FileNotFoundException {
        for (var module : modules) {
            if (astronaut.getGroup().getBoundsInParent().intersects(module.getGroup().getBoundsInParent())) {
                if(!module.getOccupationAreas().containsValue(astronaut) && module.getOccupationAreas().containsValue(null)) {
                    module.setAstronaut(astronaut);
                    astronaut.setInModule(true);
                break;}
            }
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
    public static int getRandomNumber(AstronautIntern astronaut){
        double chance = getSuccessChance(astronaut);
        return Math.random() < chance ? 0 : 1;
    }
    public static Pane getRoot(){
        return root;
    }
    public static UserInterface getUserInterface(){
        return userInterface;
    }
    public static StackPane getStackPane(){
        return stackPane;
    }
    public static List<Module> getModules(){
        return modules;
    }
    public static Camera getCamera(){
        return camera;
    }
    public static Image getLogo(){
        return logo;
    }
}
