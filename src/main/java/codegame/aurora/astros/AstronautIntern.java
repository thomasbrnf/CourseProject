package codegame.aurora.astros;

import codegame.aurora.Main;
import codegame.aurora.action.Movement;
import codegame.aurora.tools.Tools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import static codegame.aurora.tools.Tools.astroToEdit;

public class AstronautIntern extends Humans implements Action {
    private int ID = 0;
    private static int count = 0;
    private int quantityOfSpaceWalks = 0;
    public boolean isActive = false;
    private AnchorPane mainPane;
    private AnchorPane activePane;
    public AstronautIntern(String name, int energy, int experience) {
        super();
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
        return super.experience;
    }
    public String getName() {
        return super.name;
    }
    public double getX() {
        return super.x;
    }
    public int getID() {
        return this.ID;
    }
    public String getAstroClass() {
        return "Astronaut-Intern";
    }
    public double getY() {
        return super.y;
    }
    public int getQuantityOfSpaceWalks() {
        return this.quantityOfSpaceWalks;
    }
    public int getEnergy() {
        return super.energy;
    }
    public Group getGroup() {
        return super.group;
    }
    private void setInitialValues(String name, int energy, int experience) {
        setXY();
        setName(name);
        this.ID = ++count;
        super.energy = energy;
        super.experience = experience;
    }
    private void setGroupOnClickHandler() {
        getGroup().setOnMouseClicked(event -> {
            astroActive();
        });
    }
    private void setImage(Image image) {
        super.imageView.setImage(image);
    }
    public void setExperience(int experience) {
        super.experience = experience;
    }
    private void setActiveAstroPane() {
        activePane.setLayoutX(getX() - 28);
        activePane.setLayoutY(getY() - 73);
        Label astroName = (Label) activePane.lookup("#astroNameActive");
        Label classInfo = (Label) activePane.lookup("#classInfo");
        Label experienceLabel = (Label) activePane.lookup("#experienceLabel");
        Label spaceWalksLabel = (Label) activePane.lookup("#spaceWalksLabel");
        Line energyLine = (Line) activePane.lookup("#energyLine");
        Line energyLineBackground = (Line) activePane.lookup("#energyLineBackground");
        classInfo.setText(getAstroClass());
        experienceLabel.setText(String.valueOf(getExperience()));
        spaceWalksLabel.setText(String.valueOf(getQuantityOfSpaceWalks()));
        astroName.setText(name);
        energyLine.setEndX(energyLine.getStartX() + (int) (energy * 1.56));
        energyLine.toFront();
        energyLineBackground.setStartX(energyLine.getEndX());
        addChild(activePane);
        removeChild(mainPane);
    }
    private void setMainAstroPane() {
        mainPane.setLayoutX(getX());
        mainPane.setLayoutY(getY());
        Label astroName = (Label) mainPane.lookup("#astroName");
        Line astroEnergyLine = (Line) mainPane.lookup("#astroEnergyLine");
        Line astroEnergyLineBackground = (Line) mainPane.lookup("#astroEnergyLineBackground");
        astroName.setText(name);
        astroEnergyLine.setEndX(astroEnergyLine.getStartX() + energy - 1);
        astroEnergyLine.toFront();
        astroEnergyLineBackground.setStartX(astroEnergyLine.getEndX());
        addChild(mainPane);
        removeChild(activePane);
    }
    private void setGroup() {
        super.group = new Group();
        super.group.getChildren().add(super.imageView);
    }
    public void setName(String name) {
        super.name = name;
    }
    private void setImageView() {
        imageObjectMain = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astro.png");
        imageObjectActive = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astroActive.png");
        super.imageView = new ImageView(imageObjectMain);
        super.imageView.setX(x);
        super.imageView.setY(y + 17);
    }
    public void setEnergy(int energy) {
        super.energy = energy;
    }
    private void setXY() {
        double x = Math.floor(Math.random() * (Main.sizeX - 100));
        double y = Math.floor(Math.random() * (Main.sizeY - 100));
        super.x = x;
        super.y = y;
    }
    private void loadFXML() {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Astro.fxml"));
        FXMLLoader activeLoader = new FXMLLoader(getClass().getResource("/Info.fxml"));
        try {
            mainPane = mainLoader.load();
            activePane = activeLoader.load();
            setMainAstroPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void astroActive() {
        isActive = !isActive;
        if (isActive) {
            activateAstro();
        } else {
            deactivateAstro();
        }
    }
    private void activateAstro() {
        Main.astros.put(this, true);
        Main.activeAstros.add(this);
        astroToEdit = this;
        setActiveAstroPane();
        setImage(imageObjectActive);
    }
    public void deactivateAstro() {
        Main.astros.put(this, false);
        Main.activeAstros.remove(this);
        astroToEdit = null;
        setMainAstroPane();
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
    public static void driftStop() {
        Movement.driftStop();
    }
    public static void driftResume() {
        Movement.driftResume();
    }
    @Override
    public void toDoResearching() {
        this.experience++;
    }
    @Override
    public void toGoToSpace() {
        this.quantityOfSpaceWalks++;
        this.experience++;
    }
    @Override
    public void rest() {
        this.energy++;
    }
    @Override
    public void toDoMaintenance() {
        this.experience++;
    }
    @Override
    public int hashCode() {
        return ID;
    }
    @Override
    public void delete() {
        Main.root.getChildren().remove(getGroup());
        Main.astros.remove(this);
        Main.activeAstros.remove(this);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstronautIntern astro) {
            return this.name.equals(astro.name) && this.experience == astro.experience;
        }
        return false;
    }
    @Override
    public String toString() {
        return ID + name + '\t' + "AstronautIntern " + '\t' + experience + '\t' + energy + '\t' + quantityOfSpaceWalks;
    }

}