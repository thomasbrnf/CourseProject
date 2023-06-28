package application.aurora.micro_objects;

import application.aurora.micro_objects.tools.AstronautDestination;
import application.aurora.micro_objects.tools.AstronautType;
import application.aurora.micro_objects.tools.AstronautTools;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static application.aurora.world.Main.astronauts;
import static application.aurora.micro_objects.tools.AstronautTools.*;
import static application.aurora.micro_objects.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.getRoot;

public class AstronautIntern implements Cloneable, Comparable<AstronautIntern> {
    private int ID;
    private int energy;
    private double experience;
    private double x, y;
    private boolean isActive, elect, inModule, onMission;
    protected String name;
    protected AnchorPane mainPane;
    protected AnchorPane activePane;
    private Group group;
    protected ImageView imageView;
    protected AstronautDestination destination;
    private ImageView backgroundView;
    private Line energyLine;
    private Label nameLabel;
    public AstronautIntern(String name, int energy, double experience) throws FileNotFoundException {

        setInitialValues(name, energy, experience);
        setImageView();
        setImageBackground();
        setGroup();
        loadFXML();
        setInModule(false);
        setXY(getRandomSpawnNumber(SPAWN_VALUE_X),getRandomSpawnNumber(SPAWN_VALUE_Y));
        getRoot().getChildren().add(getGroup());

        System.out.println("Base constructor was called");
        System.out.println(this);
    }
    public AstronautIntern(){System.out.println("Base constructor was called");}
    static  {System.out.println("Static block was called");}
    {System.out.println("Non-static block was called");}
    private void setInitialValues(String name, int energy, double experience) {
        setXY();
        setName(name);

        this.ID = ++AstronautTools.totalObjectsCreated;
        this.energy = energy;
        this.experience = experience;
    }
    public void setXY(double x, double y){
        this.group.setLayoutX(x);
        this.group.setLayoutY(y);
    }
    private void setXY() {
        this.x = ON_CREATED_VALUE_X;
        this.y = ON_CREATED_VALUE_Y;
    }
    public void setID(int id){
        this.ID = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setExperience(double experience) {
        this.experience = experience;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    private void setImageBackground() throws FileNotFoundException {
        Image imageBackground = new Image(new FileInputStream("src/images/objectBackground.png"));

        backgroundView = new ImageView(imageBackground);
        backgroundView.setLayoutX(x);
        backgroundView.setLayoutY(y + IMAGE_MARGIN);
    }
    protected void setImageView() throws FileNotFoundException {
        Image imageObjectMain = new Image(new FileInputStream("src/images/astronautIntern.png"));

        imageView = new ImageView(imageObjectMain);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y + IMAGE_MARGIN);
    }
    protected void setElectedPane() {
        AstronautTools.setActivePaneLayout(activePane, getX(),getY());

        AstronautTools.createLabel(activePane.lookup("#nameLabel"), name);
        AstronautTools.createLabel(activePane.lookup("#classObject"),getAstronautClass());
        AstronautTools.createLabel(activePane.lookup("#experienceLabel"),String.valueOf((int)getExperience()));
        AstronautTools.createLabel(activePane.lookup("#spaceWalksLabel"),String.valueOf(0));

        Line energyLine = AstronautTools.createLine(activePane.lookup("#energyLine"),
                (int) (energy * ENERGY_SCALE_FACTOR), null,energy);
        AstronautTools.createLine(activePane.lookup("#energyLineBackground"), null,
                (int) energyLine.getStartX(),energy);

        addChild(activePane);
        removeChild(mainPane);
    }
    public void setOnMission(boolean b){
        this.onMission = b;
    }
    public void setElect(boolean elect) {
        this.elect = elect;
    }
    private void setMainPane() {
        mainPane.setLayoutX(getX() + MAIN_PANE_MARGIN);
        mainPane.setLayoutY(getY());

        nameLabel = AstronautTools.createLabel(mainPane.lookup("#nameLabel"), name);
        nameLabel.setOpacity(NAME_DEFAULT_OPACITY);


        energyLine = AstronautTools.createLine(mainPane.lookup("#energyLine"),
                energy - ENERGY_OFFSET, null, energy);
        AstronautTools.createLine(mainPane.lookup("#energyLineBackground"),null,
                (int) energyLine.getStartX(), energy);

        addChild(mainPane);
        removeChild(activePane);
    }
    public void setDestination(AstronautDestination destination){
        this.destination = destination;
    }
    private void setGroup() {
        this.group = new Group();
        this.group.getChildren().add(this.imageView);
        this.group.setLayoutY(getY());
        this.group.setLayoutX(getX());
    }
    public void setInModule(boolean inModule){
        this.inModule = inModule;
    }
    private void loadFXML() {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/AstronautPanel.fxml"));
        FXMLLoader activeLoader = new FXMLLoader(getClass().getResource("/AstronautDetailedPanel.fxml"));
        try {
            mainPane = mainLoader.load();
            activePane = activeLoader.load();
            setMainPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void toggleElect(){
        if(isActive)toggleActive();
        elect = !elect;
        if (elect) {
            elect();
        } else {
            deselect();
        }
    }
    private void elect(){
        getElectedAstronauts().add(this);
        updateAstronautStatus(this);
        setElectedPane();
        addChild(backgroundView);
        backgroundView.toBack();
    }
    public void deselect() {
        getElectedAstronauts().remove(this);
        updateAstronautStatus(this);
        setMainPane();
        removeChild(backgroundView);
    }
    void addChild(Node node) {
        if (!getGroup().getChildren().contains(node)) {
            getGroup().getChildren().add(node);
        }
    }
    void removeChild(Node node) {
        getGroup().getChildren().remove(node);
    }
    public void delete() {
        AstronautTools.removeChild(this);

        if(isActive())toggleActive();
        if(isElect())toggleElect();
        astronauts.remove(this);
        getActiveAstronauts().remove(this);
        AstronautTools.totalObjectsCreated--;
    }
    public void toggleActive() {
        isActive = !isActive;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
    }
    private void activate() {
        getActiveAstronauts().add(this);
        updateAstronautStatus(this);
        nameLabel.setOpacity(NAME_ACTIVE_OPACITY);
    }
    private void deactivate() {
        getActiveAstronauts().remove(this);
        updateAstronautStatus(this);
        nameLabel.setOpacity(NAME_DEFAULT_OPACITY);
    }
    public void updateOnExperiments(){
        setEnergy(getEnergy() - ENERGY_DECREMENT);
        updateEnergyLine(getEnergy());
    }
    public void updateExperienceAfterExperiments(double amountOfExperience) {
        if (getExperience() >= EXPERIENCE_THRESHOLD) return;
        setExperience(getExperience() + amountOfExperience);

    }
    public void rest(int energyIncreaseAmount) {
        setEnergy(getEnergy() + energyIncreaseAmount);
        updateEnergyLine(getEnergy());
        AstronautTools.setLineOpacity(energyLine, energy);
    }
    public boolean intersects(AstronautIntern other) {
        return this.getGroup().getBoundsInParent().intersects(other.getGroup().getBoundsInParent());
    }
    public void updateExperienceAfterMaintenance(int variant) {
        if (getExperience() >= EXPERIENCE_THRESHOLD) return;

        int newEnergy = getEnergy() - ENERGY_DECREMENT_ON_MAINTENANCE;
        if (newEnergy >= ENERGY_MINIMUM_VALUE) {
            setEnergy(newEnergy);
        }
        updateEnergyLine(getEnergy());

        if (variant == FAIL_INDEX) {
            decrementExperience();
        } else {
            incrementExperience();
        }
    }
    private void decrementExperience() {
        double newExperience = getExperience() - EXPERIENCE_DECREASES_ON_FAIL;
        setExperience(Math.max(newExperience, EXPERIENCE_MINIMUM_VALUE));
    }
    private void incrementExperience() {
        setExperience(getExperience() + EXPERIENCE_INCREASES_ON_SUCCESS);
    }
    protected void updateEnergyLine(double energy) {
        double newEndX = energyLine.getStartX() + energy - ENERGY_OFFSET;

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(ENERGY_LINE_ANIMATION_DURATION),
                new KeyValue(energyLine.endXProperty(), newEndX));

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }
    public boolean isActive() {
        return isActive;
    }
    public boolean inModule() {
        return inModule;
    }
    public boolean isElect() {
        return elect;
    }
    public boolean onMission() {
        return onMission;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstronautIntern astronaut) {
            return this.name.equals(astronaut.name) && this.experience == astronaut.experience &&
                    this.energy == astronaut.energy && this.ID == astronaut.ID;
        }
        return false;
    }
    @Override
    public String toString() {
        return ID + '\t' + name + '\t' + "AstronautIntern " + '\t' + experience + '\t' + energy;
    }
    @Override
    public AstronautIntern clone() {
        try {
            AstronautIntern copy = (AstronautIntern) super.clone();

            copy.setInitialValues(this.name+"*",this.energy,this.experience);

            copy.setImageView();
            copy.setImageBackground();
            copy.setGroup();
            copy.loadFXML();
            copy.setXY(getGroup().getLayoutX(),getGroup().getLayoutY());
            copy.setElect(false);
            getRoot().getChildren().add(copy.getGroup());
            astronauts.add(copy);
            return copy;
        } catch (CloneNotSupportedException | FileNotFoundException e) {
            throw new AssertionError();
        }
    }
    @Override
    public int compareTo(@NotNull AstronautIntern o) {
        int result = 0;
        result += Integer.compare(this.getEnergy(), o.getEnergy());
        result += Double.compare(this.getExperience(), o.getExperience());
        result += this.getName().compareTo(o.getName());
        return result;
    }
    public double getExperience() {
        return this.experience;
    }
    public double getExperienceRounded() {
        return Math.round(this.experience * 10.0) / 10.0;
    }
    public String getName() {
        return this.name;
    }
    public AstronautType getType() {
        if(this instanceof ManagingAstronaut){
            return AstronautType.MANAGING_ASTRONAUT;
        }
        if (this instanceof Astronaut) {
            return AstronautType.ASTRONAUT;
        }else{
            return AstronautType.ASTRONAUT_INTERN;
        }
    }
    public AstronautDestination getDestination(){
        return destination;
    }
    public double getX() {
        return this.x;
    }
    public int getID() {
        return this.ID;
    }
    public String getAstronautClass() {
        return "Astronaut-Intern";
    }
    public double getY() {
        return this.y;
    }
    public int getEnergy() {
        return this.energy;
    }
    public Group getGroup() {
        return this.group;
    }
    public boolean getInModule(){
        return this.inModule;
    }
    public boolean getElect(){
        return this.elect;
    }
    public boolean getActive() {
        return isActive;
    }
}