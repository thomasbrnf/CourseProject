package codegame.aurora.macro_objects;

import codegame.aurora.Main;
import javafx.scene.image.Image;

public class HabitationModule extends Module{
    private static HabitationModule instance = null;
    private HabitationModule() {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();
        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static HabitationModule getInstance() {
        if (instance == null) {
            instance = new HabitationModule();
        }
        return instance;
    }
    private void setXY() {
        X = 100;
        Y = 97;
    }
    private void setNaming() {
        naming.setText("Habitation Module");
        naming.setLayoutX(215);
        naming.setLayoutY(660);
        naming.setRotate(-90);
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\habitation_module.png");
        moduleImage.setImage(image);
    }
    private void setGroup(){
        group.setLayoutX(X);
        group.setLayoutY(Y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(270, 174),null);
        occupationAreas.put(new Container(270, 522),null);
        occupationAreas.put(new Container(270, 867),null);
        occupationAreas.put(new Container(270, 1209),null);
    }
}