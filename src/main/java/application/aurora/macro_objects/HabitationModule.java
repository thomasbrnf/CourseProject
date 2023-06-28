package application.aurora.macro_objects;

import application.aurora.micro_objects.AstronautIntern;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static application.aurora.macro_objects.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.getRoot;

public class HabitationModule extends Module{
    private static HabitationModule instance = null;
    private HabitationModule() throws FileNotFoundException {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();

        initializeOccupationAreas();

        getRoot().getChildren().add(getGroup());
    }
    public static HabitationModule getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new HabitationModule();
        }
        return instance;
    }
    private void setXY() {
        x = X_SPAWN_HABITATION;
        y = Y_SPAWN_HABITATION;
    }
    private void setNaming() {
        naming.setText("Habitation Module");
        naming.setLayoutX(TEXT_X_HABITATION);
        naming.setLayoutY(TEXT_Y_HABITATION);
        naming.setRotate(TEXT_ROTATE_HABITATION);
    }
    private void setImageView() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/habitation_module.png"));
        moduleImage.setImage(image);
    }
    protected void setGroup(){
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    @Override
    protected void setCoordinatesOnEjection(AstronautIntern astronaut) {
        int newY = (int) (Math.random() * (1000 - 200 + 1) + 200);
        astronaut.setXY(550, newY);
    }
    private void setInitialOpacity(AstronautIntern astronautIntern, Container container){
        double initialOpacity = (double) astronautIntern.getEnergy() / 100;
        container.getBar().setVisible(true);
        container.getBar().setOpacity(initialOpacity);
    }
    @Override
    public void initializeInteraction(Container container, AstronautIntern astronautIntern) {
        setInitialOpacity(astronautIntern,container);

        container.setTimeLine(new Timeline(new KeyFrame(Duration.millis(UPDATE_INTERVAL_MS), event ->
                updateEnergyLevel(container, astronautIntern))));
        container.getTimeLine().setCycleCount(100 - astronautIntern.getEnergy());
        container.getTimeLine().play();
        container.getTimeLine().setOnFinished(e -> {
            container.getBar().setVisible(false);
            ejectAstronaut(astronautIntern);});
        container.setFadeTransition(new FadeTransition(Duration.millis(FADE_DURATION_MS), container.getBar()));
        container.getFadeTransition().setAutoReverse(false);
    }
    private void initializeOccupationAreas() throws FileNotFoundException {
        int xAlignment = 122;
        Image container = new Image(new FileInputStream("src/images/habitation_container.png"));
        Image bar = new Image(new FileInputStream("src/images/energy_bar.png"));
        occupationAreas.put(new Container(xAlignment, 50, container,bar),null);
        occupationAreas.put(new Container(xAlignment, 390,container,bar),null);
        occupationAreas.put(new Container(xAlignment, 730,container,bar),null);
        occupationAreas.put(new Container(xAlignment, 1070,container,bar),null);
    }
    private void updateEnergyLevel(Container container, AstronautIntern astronautIntern) {
        if (astronautIntern.getInModule() && astronautIntern.getEnergy() < 100) {
            astronautIntern.rest(ENERGY_INCREASE_AMOUNT);
            container.getFadeTransition().setFromValue(container.getBar().getOpacity());
            container.getFadeTransition().setToValue((double) astronautIntern.getEnergy() / 100);
            container.getFadeTransition().play();
        }
    }
    @Override
    protected AstronautIntern getAstronautFromContainer(int digit) {
        int index = digit - 1;
        return (AstronautIntern) occupationAreas.values().toArray()[index];
    }
}