package application.aurora.macro_objects;

import application.aurora.Main;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.tools.Tools;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MaintenanceModule extends Module{
    private static MaintenanceModule instance = null;
    private MaintenanceModule() throws FileNotFoundException {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();

        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static MaintenanceModule getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new MaintenanceModule();
        }
        return instance;
    }
    private void setXY() {
        x = 1363;
        y = 86;
    }
    private void setImageView() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/maintenance_module.png"));
        this.moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Maintenance Module");
        naming.setLayoutX(130);
        naming.setLayoutY(335);
    }
    private void setGroup(){
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    @Override
    protected void setCoordinatesOnEjection(AstronautIntern astronaut) {
        astronaut.setXY(1000, 510);
    }
    private void initializeOccupationAreas() throws FileNotFoundException {
        int yAlignment = 2;
        Image container = new Image(new FileInputStream("src/images/maintenance_container.png"));
        occupationAreas.put(new Container(50, yAlignment, container, null), null);
        occupationAreas.put(new Container(255, yAlignment, container, null), null);
        occupationAreas.put(new Container(460, yAlignment, container, null), null);
    }
    @Override
    public void initializeInteraction(Container container, AstronautIntern astronautIntern){
        container.setTimeLine(new Timeline(new KeyFrame(Duration.seconds(1), event -> fadeInStatusBar(astronautIntern, container))));
        container.getTimeLine().setCycleCount(10);
        container.getTimeLine().play();
        container.getTimeLine().setOnFinished(e -> ejectAstronaut(astronautIntern));
    }
    private void fadeInStatusBar(AstronautIntern astronautIntern, Container container){
        checkEnergyLevel(astronautIntern);

        container.setFadeTransition(new FadeTransition(Duration.seconds(0.5), container.getBar()));
        container.getFadeTransition().setToValue(0.0);
        container.getFadeTransition().setOnFinished(e1 -> {try {
            updateAndFadeInStatusBar(astronautIntern,container);}
        catch (FileNotFoundException e) {throw new RuntimeException(e);}});
        container.getFadeTransition().play();
    }
    private void fadeInStatusBar(ImageView bar) {
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), bar);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.play();
    }
    private void checkEnergyLevel(AstronautIntern astronautIntern){
        if(astronautIntern.getEnergy() < 10) {
            ejectAstronaut(astronautIntern);
        }
    }
    private void updateAndFadeInStatusBar(AstronautIntern astronautIntern, Container container) throws FileNotFoundException {
        int chance = Tools.getRandomNumber(astronautIntern);
        container.getBar().setImage(getStatusImage(chance));
        fadeInStatusBar(container.getBar());
        astronautIntern.updateExperienceAfterMaintenance(chance);
    }
    private Image getStatusImage(int chance) throws FileNotFoundException {
        if(chance == 0)return new Image(new FileInputStream("src/images/status_bar_fail.png"));
        return new Image(new FileInputStream("src/images/status_bar_success.png"));
    }
    @Override
    protected AstronautIntern getAstronautFromContainer(int digit) {
        int index = digit - 1;
        return (AstronautIntern) occupationAreas.values().toArray()[index];
    }
}