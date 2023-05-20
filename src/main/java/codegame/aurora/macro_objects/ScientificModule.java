package codegame.aurora.macro_objects;

import codegame.aurora.Main;
import javafx.scene.image.Image;

public class ScientificModule extends Module{
    private static ScientificModule instance = null;
    private ScientificModule() {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();
        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static ScientificModule getInstance() {
        if (instance == null) {
            instance = new ScientificModule();
        }
        return instance;
    }
    private void setXY() {
        X = 942;
        Y = 1180;
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\scientific_module.png");
        moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Scientific Module");
        naming.setLayoutX(390);
        naming.setLayoutY(-45);

    }
    private void setGroup() {
        group.setLayoutX(X);
        group.setLayoutY(Y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(1025, 1220),null);
        occupationAreas.put(new Container(1235, 1220),null);
        occupationAreas.put(new Container(1445, 1220),null);
        occupationAreas.put(new Container(1665, 1220),null);
        occupationAreas.put(new Container(1875, 1220),null);
    }
}