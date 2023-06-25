package application.aurora.macro_objects;

import application.aurora.Main;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;
import application.aurora.tools.Tools;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ScientificModule extends Module{
    private static ScientificModule instance = null;
    private ScientificModule() throws FileNotFoundException {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();
        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static ScientificModule getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ScientificModule();
        }
        return instance;
    }
    private void setXY() {
        x = 942;
        y = 1180;
    }
    private void setImageView() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/scientific_module.png"));
        moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Scientific Module");
        naming.setLayoutX(390);
        naming.setLayoutY(-45);

    }
    protected void setGroup() {
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    @Override
    protected void setCoordinatesOnEjection(AstronautIntern astronaut) {
        int newX = (int) (Math.random() * (1800 - 1100 + 1) + 1100);
        astronaut.setXY(newX, 800);
    }
    private void setTaskImage(Container container, int variant) throws FileNotFoundException {
        Image image;
        if(variant == 1) {
            image = new Image(new FileInputStream("src/images/status_bar.png"));
        }else{
            image = new Image(new FileInputStream("src/images/in_space_bar.png"));
        }
        container.getBar().setImage(image);
    }
    private void initializeOccupationAreas() throws FileNotFoundException {
        int yAlignment = 5;
        Image container = new Image(new FileInputStream("src/images/scientific_container.png"));
        Image bar = new Image(new FileInputStream("src/images/status_bar.png"));
        occupationAreas.put(new Container(50, yAlignment,container,bar),null);
        occupationAreas.put(new Container(255, yAlignment,container,bar),null);
        occupationAreas.put(new Container(460, yAlignment,container,bar),null);
        occupationAreas.put(new Container(665, yAlignment,container,bar),null);
        occupationAreas.put(new Container(870, yAlignment,container,bar),null);
    }
    @Override
    public void initializeInteraction(Container container, AstronautIntern astronautIntern) throws FileNotFoundException {
        if (astronautIntern instanceof ManagingAstronaut){
            initializeSpaceExpedition(container,astronautIntern);
        }else {
            initializeExperiment(container, astronautIntern);
        }
    }
    public void initializeExperiment(Container container, AstronautIntern astronautIntern) throws FileNotFoundException {
        setTaskImage(container,1);

        container.setTimeLine(new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            checkEnergyLevel(astronautIntern);
            fadeInStatusBar(container);
            astronautIntern.updateExperienceAfterExperiments(Tools.getAmountOfExperience(astronautIntern));
        })));
        container.getTimeLine().setCycleCount(10);
        container.getTimeLine().play();
        container.getTimeLine().setOnFinished(e -> ejectAstronaut(astronautIntern));
    }
    private void initializeSpaceExpedition(Container container, AstronautIntern astronautIntern) throws FileNotFoundException {
        setTaskImage(container,2);
        fadeInStatusBar(container);

        container.setTimeLine(new Timeline(new KeyFrame(Duration.seconds(1), event ->
                ((ManagingAstronaut)astronautIntern).updateOnExpedition(0.5))));
        container.getTimeLine().setCycleCount(15);
        container.getTimeLine().play();
        container.getTimeLine().setOnFinished(e -> {
            ejectAstronaut(astronautIntern);
            ((ManagingAstronaut)astronautIntern).updateAfterExpedition();
        });
    }
    private void fadeInStatusBar(Container container){
        container.setFadeTransition(new FadeTransition(Duration.seconds(1), container.getBar()));
        container.getFadeTransition().setFromValue(container.getBar().getOpacity());
        container.getFadeTransition().setToValue(1);
        container.getFadeTransition().play();
    }
    private void checkEnergyLevel(AstronautIntern astronautIntern){
        if(astronautIntern.getEnergy() < 10)ejectAstronaut(astronautIntern);
    }
    @Override
    protected AstronautIntern getAstronautFromContainer(int digit) {
        int index = digit - 1;
        return (AstronautIntern) occupationAreas.values().toArray()[index];
    }
}