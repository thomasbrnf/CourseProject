package codegame.aurora.modules;

import codegame.aurora.astros.AstronautIntern;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;

public abstract class Module {
    static int X;
    static int Y;
    Map<Container,AstronautIntern> occupationAreas;
    ImageView moduleImage;
    Group group;
    Label naming;
    public Module(){
        setImageView();
        setNaming();
        setGroup();
    }

    private void setGroup() {
        group = new Group();
        group.getChildren().addAll(moduleImage,naming);
    }

    private void setNaming() {
        naming = new Label(null);
        naming.setOpacity(0.2);
        naming.setFont(Font.font("Arial", FontWeight.BOLD, 62));
        naming.setTextFill(Color.BLACK);
        naming.setStyle("-fx-font-smoothing-type: lcd; -fx-text-antialiasing: on;");
        naming.toFront();
    }
    private void setImageView() {
        moduleImage = new ImageView();
        moduleImage.setX(X);
        moduleImage.setY(Y);
    }
    public Group getGroup(){return group;}
    static class Container {
        int x;
        int y;
        public Container(int x,int y){
            setXY(x,y);
        }
        public void setXY(int x,int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
}
