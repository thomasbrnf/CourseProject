package application.aurora;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
public class MiniMap {
    private final Pane mapPane;
    private final Rectangle focusArea;
    private final ImageView worldMapImage;
    public MiniMap(){
        worldMapImage = createWorldMapImage();
        focusArea = createFocusArea();
        mapPane = createMapPane(worldMapImage,focusArea);
    }
    private Pane createMapPane(ImageView worldMapImage, Rectangle focusArea) {
        Rectangle outline = createOutlineForMap();
        Rectangle outerShadow = createOuterShadow();

        Pane mapPane = new Pane(outerShadow, worldMapImage, outline, focusArea);

        applyPosition(mapPane);

        return mapPane;
    }
    private void setRoundedCorners(ImageView imageView) {
        Rectangle clipRect = new Rectangle(372, 274);
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
        Rectangle rectangle = new Rectangle(213,120);
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
    private void applyPosition(Pane pane) {
        pane.setTranslateX(20);
        pane.setTranslateY(20);
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
    public Pane getMapPane() {
        return mapPane;
    }

}
