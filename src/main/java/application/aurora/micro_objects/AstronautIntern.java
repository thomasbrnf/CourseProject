package application.aurora.micro_objects;

import application.aurora.Main;
import application.aurora.macro_objects.HabitationModule;
import application.aurora.tools.Tools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AstronautIntern implements Cloneable, Comparable<AstronautIntern> {
    private int ID;
    private int energy;
    private int experience;
    private int quantityOfSpaceWalks = 0;
    private static int totalObjectsCreated = 0;
    private double x, y;
    private boolean isActive = false;
    private String name;
    private AnchorPane mainPane;
    private AnchorPane activePane;
    private Group group;
    private ImageView imageView;
    private Image imageObjectMain, imageObjectActive;
    public AstronautIntern(String name, int energy, int experience) throws FileNotFoundException {

        setInitialValues(name, energy, experience);
        setImageView();
        setGroup();
        loadFXML();
        Main.root.getChildren().add(getGroup());

        setGroupOnClickHandler();

        System.out.println(this);
    }
    public AstronautIntern(){System.out.println("Base constructor was called");}
    static  {System.out.println("Static block was called");}
    {System.out.println("Non-static block was called");}
    private void setInitialValues(String name, int energy, int experience) {
        setXY();
        setName(name);

        this.ID = ++totalObjectsCreated;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    private void setImage(Image image) {
        this.imageView.setImage(image);
    }
    private void setImageView() throws FileNotFoundException {
        imageObjectMain = new Image(new FileInputStream("src/images/astronaut.png"));
        imageObjectActive = new Image(new FileInputStream("src/images/astronautActive.png"));

        this.imageView = new ImageView(imageObjectMain);
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y + 17);
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
    private void setActivePane() {
        activePane.setLayoutX(getX() - 22);
        activePane.setLayoutY(getY() - 73);

        createLabel(activePane.lookup("#nameLabel"), name);
        createLabel(activePane.lookup("#classObject"),getAstronautClass());
        createLabel(activePane.lookup("#experienceLabel"),String.valueOf(getExperience()));
        createLabel(activePane.lookup("#spaceWalksLabel"),String.valueOf(getQuantityOfSpaceWalks()));

        Line energyLine = createLine(activePane.lookup("#energyLine"),(int) (energy * 1.56), null);
        createLine(activePane.lookup("#energyLineBackground"), null, (int) energyLine.getStartX());

        addChild(activePane);
        removeChild(mainPane);
    }
    private void setMainPane() {
        mainPane.setLayoutX(getX()+2);
        mainPane.setLayoutY(getY());

        createLabel(mainPane.lookup("#nameLabel"), name);

        Line energyLine = createLine(mainPane.lookup("#energyLine"),energy - 1, null);
        createLine(mainPane.lookup("#energyLineBackground"),null, (int) energyLine.getStartX());

        addChild(mainPane);
        removeChild(activePane);
    }
    private void createLabel(Node node, String text){
        Label label = (Label) node;
        label.setText(text);
    }
    private Line createLine(Node node, Integer endPosition, Integer startPosition){
        Line line = (Line) node;
        if(endPosition != null){
            line.setEndX(line.getStartX() + endPosition);
            line.toFront();
            setLineOpacity(line);
        }else{
            line.setStartX(startPosition);
        }
        return line;
    }
    private void setLineOpacity(Line line){
        if(energy == 0) line.setOpacity(0);
        else {
            line.setOpacity(1);}
    }
    private void setGroup() {
        this.group = new Group();
        this.group.getChildren().add(this.imageView);
        this.group.setLayoutY(getY());
        this.group.setLayoutX(getX());
    }
    public void setActive() {
        isActive = !isActive;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
    }
    private void activate() {
        Tools.activeAstronauts.add(this);
        Tools.astronautToEdit = this;
        setActivePane();
        setImage(imageObjectActive);
    }
    private void deactivate() {
        Tools.activeAstronauts.remove(this);
        Tools.astronautToEdit = null;
        setMainPane();
        setImage(imageObjectMain);
    }
    private void addChild(Node node) {
        if (!getGroup().getChildren().contains(node)) {
            getGroup().getChildren().add(node);
        }
    }
    private void removeChild(Node node) {
        getGroup().getChildren().remove(node);
    }
    public void delete() throws FileNotFoundException {
        Main.root.getChildren().remove(getGroup());
        Main.astronauts.remove(this);
        HabitationModule.getInstance().removeAstronaut(this);

    }
    public void setGroupOnClickHandler() {
        getGroup().setOnMouseClicked(event -> setActive());
    }
    public void toDoResearching() {
        this.experience++;
    }
    public void toGoToSpace() {
        this.quantityOfSpaceWalks++;
        this.experience++;
    }
    public void rest() {
        this.energy++;
    }
    public void toDoMaintenance() {
        this.experience++;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstronautIntern astronaut) {
            return this.name.equals(astronaut.name) && this.experience == astronaut.experience &&
                    this.energy == astronaut.energy && this.quantityOfSpaceWalks == astronaut.quantityOfSpaceWalks
                    && this.ID == astronaut.ID;
        }
        return false;
    }
    @Override
    public String toString() {
        return ID + '\t' + name + '\t' + "AstronautIntern " + '\t' + experience + '\t' + energy + '\t' + quantityOfSpaceWalks;
    }
    @Override
    public AstronautIntern clone() {
        try {
            AstronautIntern copy = (AstronautIntern) super.clone();

            copy.setInitialValues(this.name+"*",this.energy,this.experience);
            copy.setImageView();
            copy.setGroup();
            copy.loadFXML();
            copy.setGroupOnClickHandler();
            copy.setActive();

            Main.root.getChildren().add(copy.getGroup());
            Main.astronauts.add(copy);
            return copy;
        } catch (CloneNotSupportedException | FileNotFoundException e) {
            throw new AssertionError();
        }
    }
    @Override
    public int compareTo(@NotNull AstronautIntern o) {
        return 0;
    }
    public int getExperience() {
        return this.experience;
    }
    public String getName() {
        return this.name;
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
    public int getQuantityOfSpaceWalks() {
        return this.quantityOfSpaceWalks;
    }
    public int getEnergy() {
        return this.energy;
    }
    public Group getGroup() {
        return this.group;
    }
}