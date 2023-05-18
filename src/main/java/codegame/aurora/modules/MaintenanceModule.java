package codegame.aurora.modules;

import codegame.aurora.Main;
import codegame.aurora.astros.AstronautIntern;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceModule extends Module{
    private static final int X = 1478;
    private static final int Y = 58;
    private static MaintenanceModule instance = null;
    private final Map<Rectangle,Boolean> occupationAreas = new HashMap<>();
    private ImageView moduleImage;
    private Group group;
    private Label naming;
    private MaintenanceModule() {
        setImageView();
        setNaming();
        initializeOccupationAreas();
        setGroup();
        Main.root.getChildren().add(getGroup());
    }
    public static MaintenanceModule getInstance() {
        if (instance == null) {
            instance = new MaintenanceModule();
        }
        return instance;
    }
    private void initializeOccupationAreas() {
        int x = 1530;
        int y = 125;
        int width = 110;
        int height = 200;
        for (int i = 0; i < 4; i++) {
            Rectangle area = new Rectangle(x+ i * (width + 20), y , width, height);
            area.setStroke(Color.YELLOW);
            area.setStrokeWidth(4);
            area.setFill(Color.TRANSPARENT);
            occupationAreas.put(area,false);
        }
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\maintenance_module.png");
        moduleImage = new ImageView(image);
        moduleImage.setX(X);
        moduleImage.setY(Y);
    }
    private void setNaming() {
        this.naming = new Label("Maintenance Module");
        this.naming.setLayoutX(1530);
        this.naming.setLayoutY(90);
        this.naming.setOpacity(0.2);
        this.naming.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        this.naming.setTextFill(Color.BLACK);
        this.naming.setStyle("-fx-font-smoothing-type: lcd; -fx-text-antialiasing: on;");
        this.naming.toFront();
    }
    private void setGroup() {
        group = new Group();
        group.getChildren().addAll(this.moduleImage, this.naming);
        for(var obj: occupationAreas.keySet()){
            group.getChildren().add(obj);
            obj.toFront();
        }
    }
    public Group getGroup() {
        return this.group;
    }
    public void setAstro(AstronautIntern astronautIntern) {
        System.out.println(astronautIntern);
    }
}