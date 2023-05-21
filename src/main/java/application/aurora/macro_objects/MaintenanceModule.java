package application.aurora.macro_objects;

import application.aurora.Main;
import javafx.scene.image.Image;

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
        x = 1478;
        y = 58;
    }
    private void setImageView() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/images/maintenance_module.png"));
        this.moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Maintenance Module");
        naming.setLayoutX(80);
        naming.setLayoutY(285);
    }
    private void setGroup(){
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(1555, 70),null);
        occupationAreas.put(new Container(1725, 70),null);
        occupationAreas.put(new Container(1900, 70),null);
    }
}