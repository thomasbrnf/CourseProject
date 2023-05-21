package application.aurora.macro_objects;

import application.aurora.Main;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HabitationModule extends Module{
    private static HabitationModule instance = null;
    private HabitationModule() throws FileNotFoundException {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();
        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static HabitationModule getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new HabitationModule();
        }
        return instance;
    }
    private void setXY() {
        x = 100;
        y = 97;
    }
    private void setNaming() {
        naming.setText("Habitation Module");
        naming.setLayoutX(215);
        naming.setLayoutY(660);
        naming.setRotate(-90);
    }
    private void setImageView() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/habitation_module.png"));
        moduleImage.setImage(image);
    }
    private void setGroup(){
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(270, 174),null);
        occupationAreas.put(new Container(270, 522),null);
        occupationAreas.put(new Container(270, 867),null);
        occupationAreas.put(new Container(270, 1209),null);
    }
}