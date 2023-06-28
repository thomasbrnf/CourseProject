package application.aurora.macro_objects;

import application.aurora.micro_objects.AstronautIntern;
import application.aurora.tools.Tools;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static application.aurora.macro_objects.tools.CONSTANTS.*;
import static application.aurora.macro_objects.tools.ModuleTools.*;
import static application.aurora.tools.Tools.getRoot;

public abstract class Module {
    protected int x;
    protected int y;
    protected ImageView moduleImage;
    protected Group group;
    protected Label naming;
    protected HashMap<Container, AstronautIntern> occupationAreas;
    private boolean isActive;
    public Module(){
        setImageView();
        setNaming();
        setGroup();
        setOccupationAreas();
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
        moduleImage.setX(x);
        moduleImage.setY(y);
    }
    private void setDefaultContainer() {
        for(var container: occupationAreas.keySet()){
            container.setDefaultImage();
        }
    }
    protected void setActiveContainer(){
        for(var container: occupationAreas.keySet()){
            container.setActiveImage();
        }
    }
    protected void setCoordinatesOnEjection(AstronautIntern astronaut){
        // OVERRIDES IN SUPERCLASSES
    }
    protected void setOccupationAreas(){
        this.occupationAreas = new LinkedHashMap<>();
    }
    public void setAstronaut(AstronautIntern astronaut) throws FileNotFoundException {
        if(astronaut.onMission())astronaut.setOnMission(false);

        Container container = getAvailableContainer();
        if (container != null) {
            addAstronautToContainer(container, astronaut);
            initializeInteraction(container,astronaut);
            if(astronaut.getActive()){
            astronaut.toggleActive();}
            if (astronaut.getElect()) {
                astronaut.toggleElect();
            }
        }
    }
    public void ejectAstronaut(AstronautIntern astronaut) {
        Container container = getContainerByAstronaut(astronaut);
        if (container != null) {
            container.getBar().setVisible(false);
            container.stopAnimations();
            ejectAstronautFromContainer(container, astronaut);
        }
    }
    public void ejectAstronaut(KeyCode code) {
        if(!isActive)return;
        int digit = Tools.getDigitFromKeyCode(code);
        AstronautIntern astronaut = getAstronautFromContainer(digit);
        if(astronaut != null){
            ejectAstronaut(astronaut);
            astronaut.toggleActive();
        }
    }
    public void initializeInteraction(Container container, AstronautIntern astronautIntern) throws FileNotFoundException {
        System.out.println("Interacted");
    }
    private void addAstronautToContainer(Container container, AstronautIntern astronautIntern){
        occupationAreas.replace(container,null,astronautIntern);

        getRoot().getChildren().remove(astronautIntern.getGroup());
        getGroup().getChildren().add(astronautIntern.getGroup());

        astronautIntern.getGroup().relocate(container.getX(), container.getY());
    }
    private void ejectAstronautFromContainer(Container container, AstronautIntern astronaut) {
        container.getBar().setOpacity(0);
        container.stopAnimations();

        occupationAreas.replace(container, astronaut, null);

        getGroup().getChildren().remove(astronaut.getGroup());
        getRoot().getChildren().add(astronaut.getGroup());

        astronaut.setInModule(false);

        setCoordinatesOnEjection(astronaut);
    }
    public void toggleActive() {
        isActive = !isActive;
        if(isActive){
            activate();
        }else{
            deactivate();
        }
    }
    private void deactivate() {
        if(getActiveModule() != null) {
            moduleImage.setEffect(null);
            setActiveModule(null);
            setDefaultContainer();
        }
    }
    private void activate() {
        if(getActiveModule() != null)getActiveModule().toggleActive();
        setActiveModule(this);
        setActiveContainer();
    }
    public boolean isActive(){
        return isActive;
    }
    private Container getAvailableContainer(){
        for (Container container: occupationAreas.keySet()){
            if(occupationAreas.get(container) == null){
                return container;
            }
        }
        return null;
    }
    private Container getContainerByAstronaut(AstronautIntern astronaut) {
        for (Map.Entry<Container, AstronautIntern> entry : occupationAreas.entrySet()) {
            if (entry.getValue() == astronaut) {
                return entry.getKey();
            }
        }
        return null;
    }
    public Map<Container,AstronautIntern> getOccupationAreas(){
        return this.occupationAreas;
    }
    protected AstronautIntern getAstronautFromContainer(int digit) {
        return null;
    }
    public Group getGroup(){return group;}
    class Container {
        private int x;
        private int y;
        private ImageView container, bar, activeContainer;
        private Timeline timeLine;
        private FadeTransition fadeTransition;
        private final Image activeImage = new Image(new FileInputStream("src/images/active_container.png"));
        public Container(int x,int y, Image container, Image bar) throws FileNotFoundException {
            setXY(x,y);
            setContainer(container);
            setActiveContainer();
            setBar(bar);
            setObjectCoordinates();
        }
        private void setActiveContainer() {
            activeContainer = new ImageView(activeImage);
            activeContainer.setLayoutX(x);
            activeContainer.setLayoutY(y);
        }
        private void setObjectCoordinates() {
            x += X_STEP_CONTAINER;
            y -= Y_STEP_CONTAINER;
        }
        private void setContainer(Image image) {
            container = new ImageView(image);
            container.setLayoutX(x);
            container.setLayoutY(y);

            getGroup().getChildren().add(container);
        }
        protected void setBar(Image image) {
            bar = new ImageView(image);
            bar.setLayoutX(x+BAR_OFFSET);
            bar.setLayoutY(y+BAR_OFFSET);
            bar.setOpacity(0);

            getGroup().getChildren().add(bar);
        }
        public void setXY(int x,int y) {
            this.x = x;
            this.y = y;
        }
        public void setActiveImage(){
            getGroup().getChildren().remove(container);
            getGroup().getChildren().add(1,activeContainer);
        }
        public void setDefaultImage(){
            getGroup().getChildren().remove(activeContainer);
            getGroup().getChildren().add(1, container);
        }
        public void setTimeLine(Timeline timeLine){
            this.timeLine = timeLine;
        }
        public void setFadeTransition(FadeTransition fadeTransition){
            this.fadeTransition = fadeTransition;
        }
        public void stopAnimations() {
            if(this.timeLine != null) this.timeLine.stop();
            if(this.fadeTransition != null)this.fadeTransition.stop();
            bar.setOpacity(0);
        }
        public Timeline getTimeLine(){
            return timeLine;
        }
        public FadeTransition getFadeTransition(){
            return fadeTransition;
        }
        public ImageView getBar(){
            return bar;
        }
        public int getX() {
            return x+OFFSET_FOR_ASTRONAUT;
        }
        public int getY() {
            return y+OFFSET_FOR_ASTRONAUT;
        }
    }
}
