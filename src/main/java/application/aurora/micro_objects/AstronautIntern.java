package application.aurora.micro_objects;

import application.aurora.Main;
import application.aurora.tools.Tools;
import application.aurora.tools.tools_micro_objects.AstronautDestination;
import application.aurora.tools.tools_micro_objects.AstronautType;
import application.aurora.tools.tools_micro_objects.ToolsForAstronaut;
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

public class AstronautIntern implements Cloneable, Comparable<AstronautIntern> {
    private int ID;
    int energy;
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
    public AstronautIntern(String name, int energy, int experience) throws FileNotFoundException {

        setInitialValues(name, energy, experience);
        setImageView();
        setImageBackground();
        setGroup();
        loadFXML();
        setInModule(false);
        setXY(700,700);
        Main.root.getChildren().add(getGroup());

        System.out.println(this);
    }
    public AstronautIntern(){System.out.println("Base constructor was called");}
    static  {System.out.println("Static block was called");}
    {System.out.println("Non-static block was called");}
    private void setInitialValues(String name, int energy, double experience) {
        setXY();
        setName(name);

        this.ID = ++ToolsForAstronaut.totalObjectsCreated;
        this.energy = energy;
        this.experience = experience;
    }
    public void setXY(double x, double y){
        this.group.setLayoutX(x);
        this.group.setLayoutY(y);
    }
    private void setXY() {
        this.x = 0;
        this.y = 0;
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
        backgroundView.setLayoutY(y + 17);
    }
    protected void setImageView() throws FileNotFoundException {
        Image imageObjectMain = new Image(new FileInputStream("src/images/astronautIntern.png"));

        imageView = new ImageView(imageObjectMain);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y + 17);
    }
    protected void setElectedPane() {
        ToolsForAstronaut.setActivePaneLayout(activePane, getX(),getY());

        ToolsForAstronaut.createLabel(activePane.lookup("#nameLabel"), name);
        ToolsForAstronaut.createLabel(activePane.lookup("#classObject"),getAstronautClass());
        ToolsForAstronaut.createLabel(activePane.lookup("#experienceLabel"),String.valueOf(getExperience()));
        ToolsForAstronaut.createLabel(activePane.lookup("#spaceWalksLabel"),String.valueOf(0));

        Line energyLine = ToolsForAstronaut.createLine(activePane.lookup("#energyLine"),
                (int) (energy * 1.56), null,energy);
        ToolsForAstronaut.createLine(activePane.lookup("#energyLineBackground"), null,
                (int) energyLine.getStartX(),energy);

        addChild(activePane);
        removeChild(mainPane);
    }
    public void setOnMission(boolean b){
        this.onMission = b;
    }
    private void setMainPane() {
        mainPane.setLayoutX(getX()+3);
        mainPane.setLayoutY(getY());

        nameLabel = ToolsForAstronaut.createLabel(mainPane.lookup("#nameLabel"), name);
        nameLabel.setOpacity(0.5);


        energyLine = ToolsForAstronaut.createLine(mainPane.lookup("#energyLine"),
                energy - 1, null, energy);
        ToolsForAstronaut.createLine(mainPane.lookup("#energyLineBackground"),null,
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
        elect = !elect;
        if (elect) {
            elect();
        } else {
            deselect();
        }
    }
    private void elect(){
        if(Tools.electedAstronaut != null)Tools.electedAstronaut.toggleElect();
        Tools.electedAstronaut = this;
        Tools.electedAstronaut.setElectedPane();
        Tools.electedAstronaut.addChild(backgroundView);
        backgroundView.toBack();
    }
    public void deselect() {
        if (Tools.electedAstronaut != null) {
            Tools.electedAstronaut.removeChild(backgroundView);
            Tools.electedAstronaut.setMainPane();
            Tools.electedAstronaut = null;
        }
    }
    void addChild(Node node) {
        if (!getGroup().getChildren().contains(node)) {
            getGroup().getChildren().add(node);
        }
    }
    void removeChild(Node node) {
        getGroup().getChildren().remove(node);
    }
    public void delete() throws FileNotFoundException {
        Main.root.getChildren().remove(this.getGroup());
        Main.astronauts.remove(this);
        Tools.activeAstronauts.remove(this);
        Tools.electedAstronaut = null;
    }
    public void toggleActive() {
        if(elect)return;
        isActive = !isActive;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
    }
    private void activate() {
        Tools.activeAstronauts.add(this);
        nameLabel.setOpacity(1);
    }
    private void deactivate() {
        Tools.activeAstronauts.remove(this);
        nameLabel.setOpacity(0.5);
    }
    public void updateExperienceAfterExperiments(double amountOfExperience) {
        if (getExperience() >= 30) return;
        setExperience(getExperience() + amountOfExperience);
        setEnergy(getEnergy() - 1);
        updateEnergyLine(getEnergy(),1);
    }
    public void rest(int energyIncreaseAmount) {
        setEnergy(getEnergy() + energyIncreaseAmount);
        updateEnergyLine(getEnergy(), 0.5);
        ToolsForAstronaut.setLineOpacity(energyLine, energy);
    }
    public boolean intersects(AstronautIntern other) {
        return this.getGroup().getBoundsInParent().intersects(other.getGroup().getBoundsInParent());
    }
    public void updateExperienceAfterMaintenance(int variant) {
        if (getExperience() >= 30) return;
        if (variant == 0) {
            double newExperience = getExperience() - 2;
            setExperience(Math.max(newExperience, 0));
            int newEnergy = getEnergy() - 5;
            if (newEnergy >= 0) {
                setEnergy(newEnergy);
            }
            updateEnergyLine(getEnergy(), 0.5);
        } else {
            setExperience(getExperience() + 1);
            int newEnergy = getEnergy() - 3;
            if (newEnergy >= 0) {
                setEnergy(newEnergy);
            }
            updateEnergyLine(getEnergy(), 0.5);
        }
    }
    protected void updateEnergyLine(double energy, double duration) {
        double newEndX = energyLine.getStartX() + energy - 1;

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration),
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
            copy.setGroup();
            copy.loadFXML();
            copy.toggleActive();
            copy.setXY(getGroup().getLayoutX(),getGroup().getLayoutY());
            copy.elect = false;
            Main.root.getChildren().add(copy.getGroup());
            Main.astronauts.add(copy);
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