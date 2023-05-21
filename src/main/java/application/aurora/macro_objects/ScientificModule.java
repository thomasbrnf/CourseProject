package application.aurora.macro_objects;

import application.aurora.Main;
import javafx.scene.image.Image;

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
        Image image = new Image(new FileInputStream("src/images/maintenance_module.png"));
        moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Scientific Module");
        naming.setLayoutX(390);
        naming.setLayoutY(-45);

    }
    private void setGroup() {
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(1025, 1220),null);
        occupationAreas.put(new Container(1235, 1220),null);
        occupationAreas.put(new Container(1445, 1220),null);
        occupationAreas.put(new Container(1665, 1220),null);
        occupationAreas.put(new Container(1875, 1220),null);
    }
}