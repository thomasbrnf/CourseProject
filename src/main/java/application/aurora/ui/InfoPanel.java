package application.aurora.ui;

import application.aurora.micro_objects.AstronautIntern;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static application.aurora.tools.CONSTANTS.*;
public class InfoPanel {
    private Group infoGroup;
    private Rectangle rectangle;
    private Label labelHeader;
    private int count;
    public InfoPanel(double x, double y){
        setRectanglePanel();
        setHeader();
        setGroup(x,y);

    }
    public void setAstronautInfo(AstronautIntern astronaut) {
        if (astronaut.isActive() || astronaut.isElect()) {
            count++;
        } else {
            count--;
        }
        labelHeader.setText(count + " ASTRONAUT(S) \nACTIVE/ELECTED");
        checkVisibilityCondition();
    }
    private void checkVisibilityCondition() {
        infoGroup.setVisible(count != 0);
    }
    private void setHeader() {
        labelHeader = new Label();
        Font font = Font.font("Arial", FontWeight.BOLD, 14);
        labelHeader.setFont(font);
        labelHeader.setAlignment(Pos.CENTER);
        labelHeader.setTextFill(Color.WHITE);
        labelHeader.setLayoutX(HEADER_X);
        labelHeader.setLayoutY(HEADER_Y);
    }
    private void setGroup(double x, double y) {
        infoGroup = new Group(rectangle, labelHeader);
        infoGroup.setTranslateX(x);
        infoGroup.setTranslateY(y);
        infoGroup.setVisible(false);
    }
    private void setRectanglePanel() {
        rectangle = new Rectangle(PANEL_WIDTH,PANEL_HEIGHT);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.6);
        rectangle.setArcWidth(60);
        rectangle.setArcHeight(60);
    }
    public Group getGroupInfoPanel(){
        return infoGroup;
    }
}
