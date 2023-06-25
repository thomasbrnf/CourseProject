package application.aurora;

import application.aurora.tools.Tools;

public class Camera {
    private double x;
    private double y;
    public void moveCameraX(double x) {
        this.x = x;

        Main.root.setTranslateX(x);
        Tools.miniMap.getFocusArea().setX(getMiniMapPositionX(x));
    }
    public void moveCameraY(double y) {
        this.y = y;

        Main.root.setTranslateY(y);
        Tools.miniMap.getFocusArea().setY(getMiniMapPositionY(y));
    }
    public void moveX(int deltaX) {
        x += 20 *deltaX;
        moveCameraX(x);
    }
    public void moveY(int deltaY) {
        y += 20 * deltaY;
        moveCameraY(y);
    }
    public double calculate(double input, int dimension) {
        double scaled = input * 6;
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
            return 2237 - 1280;
        } else {
            return 1644 - 720;
        }
    }
    private double getCenterOfDimension(int dimension) {
        if (dimension == 0) {
            return (double) 1280 / 2;
        } else {
            return (double) 720 / 2;
        }
    }
    private double getMiniMapPositionX(double positionX) {
        return Math.abs(positionX / 6);
    }
    private double getMiniMapPositionY(double positionY) {
        return Math.abs(positionY / 6);
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
