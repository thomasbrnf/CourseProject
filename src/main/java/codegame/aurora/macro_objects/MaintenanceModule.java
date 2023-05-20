package codegame.aurora.macro_objects;

import codegame.aurora.Main;
import javafx.scene.image.Image;

public class MaintenanceModule extends Module{
    private static MaintenanceModule instance = null;
    private MaintenanceModule() {
        super();

        setXY();
        setImageView();
        setNaming();
        setGroup();
        initializeOccupationAreas();

        Main.root.getChildren().add(getGroup());
    }
    public static MaintenanceModule getInstance() {
        if (instance == null) {
            instance = new MaintenanceModule();
        }
        return instance;
    }
    private void setXY() {
        X = 1478;
        Y = 58;
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\maintenance_module.png");
        this.moduleImage.setImage(image);
    }
    private void setNaming() {
        naming.setText("Maintenance Module");
        naming.setLayoutX(80);
        naming.setLayoutY(285);
    }
    private void setGroup(){
        group.setLayoutX(X);
        group.setLayoutY(Y);
    }
    private void initializeOccupationAreas() {
        occupationAreas.put(new Container(1555, 70),null);
        occupationAreas.put(new Container(1725, 70),null);
        occupationAreas.put(new Container(1900, 70),null);
    }
}