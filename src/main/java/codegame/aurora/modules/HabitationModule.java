package codegame.aurora.modules;

import codegame.aurora.Main;
import codegame.aurora.astros.AstronautIntern;
import javafx.scene.image.Image;

import java.util.HashMap;

public class HabitationModule extends Module{
    private static HabitationModule instance = null;
    private HabitationModule() {
        super();

        setXY();
        setImageView();
        setNaming();
        initializeOccupationAreas();
        setGroup();

        Main.root.getChildren().add(getGroup());
    }
    private void setXY() {
        X = 100;
        Y = 97;
    }
    public static HabitationModule getInstance() {
        if (instance == null) {
            instance = new HabitationModule();
        }
        return instance;
    }
    private void initializeOccupationAreas() {
        occupationAreas = new HashMap<>();
        occupationAreas.put(new Container(151, -421),null);
        occupationAreas.put(new Container(151, -193),null);
        occupationAreas.put(new Container(151, 47),null);
        occupationAreas.put(new Container(151, 278),null);
        occupationAreas.put(new Container(151, 536),null);
        occupationAreas.put(new Container(151, 746),null);
    }
    public void setAstro(AstronautIntern astronautIntern) {
        for(var container: occupationAreas.keySet()){
            occupationAreas.putIfAbsent(container, astronautIntern);
            astronautIntern.setXY(container.getX(),container.getY());
            astronautIntern.setActive();
        }
    }
    private void setNaming() {
        naming.setText("Habitation Module");
        naming.setLayoutX(130);
        naming.setLayoutY(820);
        naming.setRotate(-90);
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\habitation_module.png");
        moduleImage.setImage(image);
    }
    private void setGroup() {
        group.setLayoutX(X);
        group.setLayoutY(Y);
    }

}