package codegame.aurora.modules;

import codegame.aurora.Main;
import codegame.aurora.astros.AstronautIntern;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HabitationModule {
    private static final int X = 100;
    private static final int Y = 97;
    private static HabitationModule instance = null;
    private ImageView moduleImage;
    private Group group;
    private Label naming;
    private HabitationModule() {
        setImageView();
        setNaming();
        setGroup();
        Main.root.getChildren().add(getGroup());
    }
    public static HabitationModule getInstance() {
        if (instance == null) {
            instance = new HabitationModule();
        }
        return instance;
    }
    private void setNaming() {
        this.naming = new Label("Habitation Module");
        this.naming.setLayoutX(130);
        this.naming.setLayoutY(820);
        this.naming.setOpacity(0.2);
        this.naming.setFont(Font.font("Arial", FontWeight.BOLD, 62));
        this.naming.setRotate(-90);
        this.naming.setTextFill(Color.BLACK);
        this.naming.setStyle("-fx-font-smoothing-type: lcd; -fx-text-antialiasing: on;");
        this.naming.toFront();
    }
    private void setImageView() {
        Image image = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\habitation_module.png");
        moduleImage = new ImageView(image);
        moduleImage.setX(X);
        moduleImage.setY(Y);
    }
    private void setGroup() {
        group = new Group();
        group.getChildren().addAll(this.moduleImage, this.naming);
    }
    public Group getGroup() {
        return this.group;
    }
    public void setAstro(AstronautIntern astronautIntern) {

    }

}