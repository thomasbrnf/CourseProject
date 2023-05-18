package codegame.aurora.astros;

import codegame.aurora.Main;
import codegame.aurora.modules.HabitationModule;
import codegame.aurora.modules.MaintenanceModule;
import codegame.aurora.modules.ScientificModule;
import codegame.aurora.tools.Tools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AstronautIntern extends Humans implements Action, Cloneable, Comparable<AstronautIntern> {
    private int ID;
    private static int count = 0;
    private int quantityOfSpaceWalks = 0;
    public boolean isActive = false;
    public AnchorPane mainPane;
    public AnchorPane activePane;
    public AstronautIntern(String name, int energy, int experience) {
        super();

        setInitialValues(name, energy, experience);
        setImageView();
        setGroup();
        System.out.println(getGroup().getLayoutX());
        System.out.println(getGroup().getLayoutY());
        loadFXML();
        Main.root.getChildren().add(getGroup());

        setGroupOnClickHandler();
//        checkCollision();
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
    public ImageView getImageView(){return super.imageView;}
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
    public void setGroupOnClickHandler() {
        getGroup().setOnMouseClicked(event -> setActive());
    }
    private void setImage(Image image) {
        super.imageView.setImage(image);
    }
    public void setExperience(int experience) {
        super.experience = experience;
    }
    private void setActiveAstroPane() {
        activePane.setLayoutX(getX() - 22);
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

        setEnergyLineOpacity(energyLine);

        energyLineBackground.setStartX(energyLine.getEndX());

        addChild(activePane);
        removeChild(mainPane);
    }
    private void setMainAstroPane() {
        mainPane.setLayoutX(getX()+2);
        mainPane.setLayoutY(getY());

        Label astroName = (Label) mainPane.lookup("#astroName");
        Line astroEnergyLine = (Line) mainPane.lookup("#astroEnergyLine");
        Line astroEnergyLineBackground = (Line) mainPane.lookup("#astroEnergyLineBackground");

        astroName.setText(name);

        astroEnergyLine.setEndX(astroEnergyLine.getStartX() + energy - 1);

        astroEnergyLine.toFront();

        setEnergyLineOpacity(astroEnergyLine);

        astroEnergyLineBackground.setStartX(astroEnergyLine.getEndX());

        addChild(mainPane);
        removeChild(activePane);
    }
    private void setEnergyLineOpacity(Line energyLine){
        if(energy == 0)energyLine.setOpacity(0);
        else {energyLine.setOpacity(1);}
    }
    private void setGroup() {
        super.group = new Group();
        super.group.getChildren().add(super.imageView);
        super.group.setLayoutY(getY());
        super.group.setLayoutX(getX());
    }
    public void setXY(double x, double y){
        super.group.setLayoutX(x);
        super.group.setLayoutY(y);
    }
    public void setName(String name) {
        super.name = name;
    }
    private void setImageView() {
        imageObjectMain = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astro2.png");
        imageObjectActive = new Image("C:\\Users\\Artem\\IdeaProjects\\Aurora\\src\\images\\astroActive.png");

        super.imageView = new ImageView(imageObjectMain);
        super.imageView.setLayoutX(x);
        super.imageView.setLayoutY(y + 17);
    }
    public void setActive() {
        isActive = !isActive;
        if (isActive) {
            activateAstro();
        } else {
            deactivateAstro();
        }
    }
    public void setEnergy(int energy) {
        super.energy = energy;
    }
    private void setXY() {
        /*Rectangle collisionBox = Tools.initializeCollisionBox();
        double x;
        double y;
        do {
            x = Math.floor(Math.random() * collisionBox.getWidth() + collisionBox.getX());
            y = Math.floor(Math.random() * collisionBox.getHeight() + collisionBox.getY());
        } while (!collisionBox.contains(x, y));*/
        super.x = 400;
        super.y = 530;
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
    private void activateAstro() {
        Tools.activeAstros.add(this);
        Tools.astroToEdit = this;
        setActiveAstroPane();
        setImage(imageObjectActive);
    }
    private void deactivateAstro() {
        Tools.activeAstros.remove(this);
        Tools.astroToEdit = null;
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
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstronautIntern astro) {
            return this.name.equals(astro.name) && this.experience == astro.experience &&
                    this.energy == astro.energy && this.quantityOfSpaceWalks == astro.quantityOfSpaceWalks
                    && this.ID == astro.ID;
        }
        return false;
    }
    @Override
    public String toString() {
        return ID + '\t' + name + '\t' + "AstronautIntern " + '\t' + experience + '\t' + energy + '\t' + quantityOfSpaceWalks;
    }
    public void checkCollision(){
        if(this.getGroup().getBoundsInParent().intersects(MaintenanceModule.getInstance().getGroup().getBoundsInParent())) {
            this.getGroup().setOpacity(0.5);
            MaintenanceModule.getInstance().setAstro(this);
        } else if(this.getGroup().getBoundsInParent().intersects(ScientificModule.getInstance().getGroup().getBoundsInParent())) {
            ScientificModule.getInstance().setAstro(this);
            this.getGroup().setOpacity(0.5);
        } else if(this.getGroup().getBoundsInParent().intersects(HabitationModule.getInstance().getGroup().getBoundsInParent())) {
//            if(isActive) this.setActive();
            HabitationModule.getInstance().setAstro(this);
            this.getGroup().setOpacity(0.5);
        }else{
            this.getGroup().setOpacity(1);
        }
    }
    public void toMove(KeyCode code) {
        checkCollision();
        switch (code){
            case UP -> {getGroup().setLayoutY(getGroup().getLayoutY()-3);
            System.out.println(getGroup().getLayoutY());}
            case DOWN -> {getGroup().setLayoutY(getGroup().getLayoutY()+3);
            System.out.println(getGroup().getLayoutY());}
            case LEFT -> {getGroup().setLayoutX(getGroup().getLayoutX()-3);
            System.out.println(getGroup().getLayoutX());}
            case RIGHT -> {getGroup().setLayoutX(getGroup().getLayoutX()+3);
            System.out.println(getGroup().getLayoutX());}
        }
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
            Main.astros.add(copy);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    @Override
    public int compareTo(@NotNull AstronautIntern o) {
        return 0;
    }
}