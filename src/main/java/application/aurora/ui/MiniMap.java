package application.aurora.ui;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.aurora.tools.CONSTANTS.*;
import static application.aurora.tools.MouseEventHandler.setOnMouseEntered;
import static application.aurora.tools.MouseEventHandler.setOnMouseExited;

public class MiniMap {
    private final Group mapGroup;
    private final Rectangle focusArea;
    private final ImageView worldMapImage;
    public MiniMap(){
        worldMapImage = createWorldMapImage();
        focusArea = createFocusArea();
        mapGroup = createMapGroup(worldMapImage,focusArea);
    }
    private Group createMapGroup(ImageView worldMapImage, Rectangle focusArea) {
        Rectangle outline = createOutlineForMap();
        Rectangle outerShadow = createOuterShadow();

        Group mapGroup = new Group(outerShadow, worldMapImage, outline, focusArea);
        mapGroup.setOpacity(0.5);
        applyPosition(mapGroup);
        mapGroup.setOnMouseEntered(mouseEvent1 -> setOnMouseEntered());
        mapGroup.setOnMouseExited(mouseEvent -> setOnMouseExited());
        return mapGroup;
    }
    private void setRoundedCorners(ImageView imageView) {
        Rectangle clipRect = new Rectangle(MINI_MAP_WIDTH, MINI_MAP_HEIGHT);
        clipRect.setArcWidth(100);
        clipRect.setArcHeight(100);

        imageView.setClip(clipRect);
    }
    private Rectangle createOuterShadow() {
        Rectangle rectangle = new Rectangle(372, 274);
        rectangle.setOpacity(0.9);
        rectangle.setFill(Color.BLACK);
        rectangle.setArcWidth(100);
        rectangle.setArcHeight(100);

        applyDropShadow(rectangle);

        return rectangle;
    }
    private Rectangle createFocusArea(){
        Rectangle rectangle = new Rectangle(FOCUS_AREA_WIDTH,FOCUS_AREA_HEIGHT);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.rgb(253,183,0));
        rectangle.setStrokeWidth(3);
        rectangle.setArcWidth(100);
        rectangle.setArcHeight(100);

        applyDropShadow(rectangle);

        return rectangle;
    }
    private DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setColor(Color.BLACK);
        return dropShadow;
    }
    private Rectangle createOutlineForMap(){
        Rectangle rectangle = new Rectangle( 372,274);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStrokeWidth(4);
        rectangle.setStroke(Color.WHITE);
        rectangle.setArcWidth(100);
        rectangle.setArcHeight(100);

        return rectangle;
    }
    private ImageView createWorldMapImage(){
       ImageView imageView = new ImageView();
       imageView.setFitWidth(372);
       imageView.setFitHeight(274);

       setRoundedCorners(imageView);

        return imageView;
    }
    private void applyPosition(Group group) {
        group.setTranslateX(20);
        group.setTranslateY(20);
    }
    private void applyDropShadow(Rectangle rectangle) {
        DropShadow dropShadow = createDropShadow();
        rectangle.setEffect(dropShadow);
    }
    public Rectangle getFocusArea() {
        return focusArea;
    }
    public ImageView getView() {
        return worldMapImage;
    }
    public Group getMapGroup() {
        return mapGroup;
    }
}
