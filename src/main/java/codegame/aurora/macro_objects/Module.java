package codegame.aurora.macro_objects;

import codegame.aurora.micro_objects.AstronautIntern;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    int X;
    int Y;
    Map<Container,AstronautIntern> occupationAreas;
    ImageView moduleImage;
    Group group;
    Label naming;
    public Module(){
        setImageView();
        setNaming();
        setGroup();
        setOccupationAreas();
    }
    public Map<Container,AstronautIntern> getOccupationAreas(){
        return occupationAreas;
    }
    private void setGroup() {
        group = new Group();
        group.getChildren().addAll(moduleImage,naming);
    }
    private void setNaming() {
        naming = new Label(null);
        naming.setOpacity(0.2);
        naming.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        naming.setTextFill(Color.BLACK);
        naming.setStyle("-fx-font-smoothing-type: lcd; -fx-text-antialiasing: on;");
    }
    private void setImageView() {
        moduleImage = new ImageView();
        moduleImage.setX(X);
        moduleImage.setY(Y);
    }
    public Group getGroup(){return group;}
    private void setOccupationAreas(){
        occupationAreas = new HashMap<>();
    }
    public void setAstronaut(AstronautIntern astronautIntern) {
        for (Map.Entry<Container, AstronautIntern> entry : occupationAreas.entrySet()) {
            if (entry.getValue() == null) {
                occupationAreas.put(entry.getKey(), astronautIntern);
                astronautIntern.setXY(entry.getKey().getX(), entry.getKey().getY());
                astronautIntern.setActive();
                break;
            }
        }
    }
    public void removeAstronaut(AstronautIntern astronautIntern) {
        for (Map.Entry<Container, AstronautIntern> entry : occupationAreas.entrySet()) {
            if (entry.getValue() == astronautIntern) {
                occupationAreas.put(entry.getKey(), null);
                break;
            }
        }
    }
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
