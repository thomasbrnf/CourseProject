package application.aurora.tools.tools_micro_objects;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class ToolsForAstronaut {
    public static int totalObjectsCreated = 0;
    public static void setLineOpacity(Line line, int energy) {
        double opacity = (energy == 0) ? 0 : 1;

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), new KeyValue(line.opacityProperty(), opacity));

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }
    public static void setActivePaneLayout(Pane paneLayout, double x, double y){
        paneLayout.setLayoutX(x - 22);
        paneLayout.setLayoutY(y - 73);
    }
    public static Label createLabel(Node node, String text){
        Label label = (Label) node;
        label.setText(text);
        return label;
    }
    public static Line createLine(Node node, Integer endPosition, Integer startPosition, int energy){
        Line line = (Line) node;
        if(endPosition != null){
            line.setEndX(line.getStartX() + endPosition);
            line.toFront();
            setLineOpacity(line, energy);
        }else{
            line.setStartX(startPosition);
        }
        return line;
    }
}
