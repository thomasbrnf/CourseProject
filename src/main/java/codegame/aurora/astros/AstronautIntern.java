package codegame.aurora.astros;

import codegame.aurora.Main;
import codegame.aurora.action.Movement;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class AstronautIntern extends Humans implements Action {
    private int ID = 0;
    private static int count = 0;
    private int quantityOfSpaceWalks = 0;
    public boolean isActive = false;
    private AnchorPane mainPane;
    private AnchorPane activePane;

    public AstronautIntern(String name, int energy, int experience) {
        super();
        setXY();
        setName(name);
        this.ID = ++count;
        super.energy = energy;
        super.experience = experience;
        setImageView();
        setGroup();

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Astro.fxml"));
        FXMLLoader activeLoader = new FXMLLoader(getClass().getResource("/Info.fxml"));

        try {
            mainPane = mainLoader.load();
            activePane = activeLoader.load();
            setAstroPane(mainPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.root.getChildren().add(getGroup());
        getGroup().setOnMouseClicked(event -> {
            try {
                astroActive();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

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

    public static int getCount() {
        return count;
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

    private void astroActive() throws IOException {
        isActive = !isActive;
        if (isActive) {
            Main.astros.put(this, true);
            Main.activeAstro = this;
            setActiveAstroPane();
            super.imageView.setImage(new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astroActive.png"));
        } else {
            Main.astros.put(this, false);
            Main.activeAstro = null;
            super.imageView.setImage(imageObject);
            setMainAstroPane();
        }
    }

    private double energyLineEndX(int energy, Line astroEnergyLine) {
        double lineLength = astroEnergyLine.getEndX() - astroEnergyLine.getStartX();
        return astroEnergyLine.getStartX() + (lineLength * energy / 100);
    }

    public void setExperience(int experience) {
        super.experience = experience;
    }

    private void setActiveAstroPane() {
        activePane.setLayoutX(getX()-28);
        activePane.setLayoutY(getY()-70);
        Label astroName = (Label) activePane.lookup("#astroNameActive");
        Label classInfo = (Label) activePane.lookup("#classInfo");
        Label experienceLabel = (Label) activePane.lookup("#experienceLabel");
        Label spaceWalksLabel = (Label) activePane.lookup("#spaceWalksLabel");
        Line astroEnergyLine = (Line) activePane.lookup("#energyLine");
        classInfo.setText(getAstroClass());
        experienceLabel.setText(String.valueOf(getExperience()));
        spaceWalksLabel.setText(String.valueOf(getQuantityOfSpaceWalks()));
        astroName.setText(name);
        astroEnergyLine.setEndX(energyLineEndX(energy, astroEnergyLine));
        if (!getGroup().getChildren().contains(activePane)) {
            getGroup().getChildren().add(activePane);
        }
        if (getGroup().getChildren().contains(mainPane)) {
            getGroup().getChildren().remove(mainPane);
        }
    }

    private void setMainAstroPane() {
        mainPane.setLayoutX(getX());
        mainPane.setLayoutY(getY());
        Label astroName = (Label) mainPane.lookup("#astroName");
        Line astroEnergyLine = (Line) mainPane.lookup("#astroEnergyLine");
        astroName.setText(name);
        astroEnergyLine.setEndX(energyLineEndX(energy, astroEnergyLine));
        if (!getGroup().getChildren().contains(mainPane)) {
            getGroup().getChildren().add(mainPane);
        }
        if (getGroup().getChildren().contains(activePane)) {
            getGroup().getChildren().remove(activePane);
        }
    }

    private void setAstroPane(AnchorPane pane) {
        pane.setLayoutX(getX());
        pane.setLayoutY(getY());
        Label astroName = (Label) pane.lookup("#astroName");
        Line astroEnergyLine = (Line) pane.lookup("#astroEnergyLine");
        astroName.setText(name);
        astroEnergyLine.setEndX(energyLineEndX(energy, astroEnergyLine));
        if (!getGroup().getChildren().contains(pane)) {
            getGroup().getChildren().add(pane);
        }
    }

    private void setGroup() {
        super.group = new Group();
        super.group.getChildren().add(super.imageView);
    }

    public void setName(String name) {
        super.name = name;
    }

    private void setImageView() {
        imageObject = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astro.png");
        super.imageView = new ImageView(imageObject);
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
    public TranslateTransition drift() {
        return null;
    }

    @Override
    public void delete() {
        Main.root.getChildren().remove(super.group);
        Main.astros.remove(this);
    }

    public static void driftStop() {
        Movement.driftStop();
    }

    public static void driftResume() {
        Movement.driftResume();
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