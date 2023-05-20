package codegame.aurora.micro_objects;

import codegame.aurora.Main;
import codegame.aurora.macro_objects.HabitationModule;
import codegame.aurora.tools.Tools;
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
    private static int count = 0;
    private int quantityOfSpaceWalks = 0;
    private double x, y;
    private boolean isActive = false;
    private AnchorPane mainPane;
    private AnchorPane activePane;
    private String name;
    private Group group;
    private ImageView imageView;
    private Image imageObjectMain, imageObjectActive;
    public AstronautIntern(String name, int energy, int experience) throws FileNotFoundException {
        this();

        setInitialValues(name, energy, experience);
        setImageView();
        setGroup();
        loadFXML();
        Main.root.getChildren().add(getGroup());

        setGroupOnClickHandler();
    }
    public AstronautIntern(){System.out.println("Base constructor was called");}
    static  {System.out.println("Static block was called");}
    {System.out.println("Non-static block was called");}
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
    private void setInitialValues(String name, int energy, int experience) {
        setXY();
        setName(name);
        this.ID = ++count;
        this.energy = energy;
        this.experience = experience;
    }
    public void setGroupOnClickHandler() {
        getGroup().setOnMouseClicked(event -> setActive());
    }
    private void setImage(Image image) {
        this.imageView.setImage(image);
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    private void setActivePane() {
        activePane.setLayoutX(getX() - 22);
        activePane.setLayoutY(getY() - 73);

        Label astronautName = (Label) activePane.lookup("#astroNameActive");
        astronautName.setText(name);

        Label classInfo = (Label) activePane.lookup("#classInfo");
        classInfo.setText(getAstronautClass());

        Label experienceLabel = (Label) activePane.lookup("#experienceLabel");
        experienceLabel.setText(String.valueOf(getExperience()));

        Label spaceWalksLabel = (Label) activePane.lookup("#spaceWalksLabel");
        spaceWalksLabel.setText(String.valueOf(getQuantityOfSpaceWalks()));

        Line energyLine = (Line) activePane.lookup("#energyLine");
        energyLine.setEndX(energyLine.getStartX() + (int) (energy * 1.56));
        energyLine.toFront();
        setEnergyLineOpacity(energyLine);

        Line energyLineBackground = (Line) activePane.lookup("#energyLineBackground");
        energyLineBackground.setStartX(energyLine.getEndX());

        addChild(activePane);
        removeChild(mainPane);
    }
    private void setMainPane() {
        mainPane.setLayoutX(getX()+2);
        mainPane.setLayoutY(getY());

        Label astronautName = (Label) mainPane.lookup("#astroName");
        astronautName.setText(name);

        Line astronautEnergyLine = (Line) mainPane.lookup("#astroEnergyLine");
        astronautEnergyLine.setEndX(astronautEnergyLine.getStartX() + energy - 1);
        astronautEnergyLine.toFront();
        setEnergyLineOpacity(astronautEnergyLine);

        Line astronautEnergyLineBackground = (Line) mainPane.lookup("#astroEnergyLineBackground");
        astronautEnergyLineBackground.setStartX(astronautEnergyLine.getEndX());

        addChild(mainPane);
        removeChild(activePane);
    }
    private void setEnergyLineOpacity(Line energyLine){
        if(energy == 0)energyLine.setOpacity(0);
        else {energyLine.setOpacity(1);}
    }
    private void setGroup() {
        this.group = new Group();
        this.group.getChildren().add(this.imageView);
        this.group.setLayoutY(getY());
        this.group.setLayoutX(getX());
    }
    public void setXY(double x, double y){
        this.group.setLayoutX(x);
        this.group.setLayoutY(y);
    }
    public void setName(String name) {
        this.name = name;
    }
    private void setImageView() throws FileNotFoundException {
        imageObjectMain = new Image(new FileInputStream("src/images/astronaut.png"));
        imageObjectActive = new Image(new FileInputStream("src/images/astronautActive.png"));

        this.imageView = new ImageView(imageObjectMain);
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y + 17);
    }
    public void setActive() {
        isActive = !isActive;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    private void setXY() {
        this.x = 0;
        this.y = 0;
    }
    private void loadFXML() {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/AstronautTopBar.fxml"));
        FXMLLoader activeLoader = new FXMLLoader(getClass().getResource("/AstronautOnActiveBar.fxml"));
        try {
            mainPane = mainLoader.load();
            activePane = activeLoader.load();
            setMainPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public int hashCode() {
        return ID;
    }
    public void delete() {
        Main.root.getChildren().remove(getGroup());
        Main.astronauts.remove(this);
        HabitationModule.getInstance().removeAstronaut(this);
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

            copy.setInitialValues(this.name,this.energy,this.experience);
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
}