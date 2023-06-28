package application.aurora.world;

import static application.aurora.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.getRoot;
import static application.aurora.tools.Tools.getUserInterface;

public class Camera {
    private double x;
    private double y;
    public void moveCameraX(double x) {
        this.x = x;

        getRoot().setTranslateX(x);
        getUserInterface().getMiniMap().getFocusArea().setX(getMiniMapPositionX(x));
    }
    public void moveCameraY(double y) {
        this.y = y;

        getRoot().setTranslateY(y);
        getUserInterface().getMiniMap().getFocusArea().setY(getMiniMapPositionY(y));
    }
    public void moveX(int deltaX) {
        x += MOVE_BY * deltaX;
        moveCameraX(x);
    }
    public void moveY(int deltaY) {
        y += MOVE_BY * deltaY;
        moveCameraY(y);
    }
    public double calculate(double input, int dimension) {
        double scaled = input * DIVISOR;
        double centered = scaled - getCenterOfDimension(dimension);
        return checkBounds(centered, dimension);
    }
    private double checkBounds(double value, int dimension) {
        if (value < 0) return 0;
        double max = getMaxDimension(dimension);
        return Math.min(value, max);
    }
    private double getMaxDimension(int dimension) {
        if (dimension == 0) {
            return WORLD_WIDTH - SCENE_WIDTH;
        } else {
            return WORLD_HEIGHT - SCENE_HEIGHT;
        }
    }
    private double getCenterOfDimension(int dimension) {
        if (dimension == 0) {
            return (double) SCENE_WIDTH / 2;
        } else {
            return (double) SCENE_HEIGHT / 2;
        }
    }
    private double getMiniMapPositionX(double positionX) {
        return Math.abs(positionX / DIVISOR);
    }
    private double getMiniMapPositionY(double positionY) {
        return Math.abs(positionY / DIVISOR);
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
