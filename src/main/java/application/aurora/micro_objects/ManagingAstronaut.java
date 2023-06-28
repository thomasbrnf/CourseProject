package application.aurora.micro_objects;

import application.aurora.micro_objects.tools.AstronautDestination;
import application.aurora.micro_objects.tools.AstronautTools;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import static application.aurora.micro_objects.tools.CONSTANTS.*;

public class ManagingAstronaut extends Astronaut {
    private int quantityOfSpaceWalks = QUANTITY_OF_SPACE_WALKS_DEFAULT;
    public ManagingAstronaut(String name, int energy, double experience) throws IOException {
        super(name, energy, experience);
    }
    @Override
    protected void setImageView() throws FileNotFoundException {
        Image imageObjectMain = new Image(new FileInputStream("src/images/managingAstronaut.png"));

        super.imageView = new ImageView(imageObjectMain);
        super.imageView.setLayoutX(getX());
        super.imageView.setLayoutY(getY() + 17);
    }
    @Override
    protected void setElectedPane() {
        AstronautTools.setActivePaneLayout(activePane, getX(),getY());

        AstronautTools.createLabel(activePane.lookup("#nameLabel"), name);
        AstronautTools.createLabel(activePane.lookup("#classObject"),getAstronautClass());
        AstronautTools.createLabel(activePane.lookup("#experienceLabel"),String.valueOf(getExperience()));
        AstronautTools.createLabel(activePane.lookup("#spaceWalksLabel"),String.valueOf(getQuantityOfSpaceWalks()));

        Line energyLine = AstronautTools.createLine(activePane.lookup("#energyLine"),
                (int) (getEnergy() * ENERGY_SCALE_FACTOR), null, getEnergy());
        AstronautTools.createLine(activePane.lookup("#energyLineBackground"), null,
                (int) energyLine.getStartX(), getEnergy());

        addChild(activePane);
        removeChild(mainPane);
    }
    public void setQuantityOfSpaceWalks(int i) {
        this.quantityOfSpaceWalks = i;
    }
    public void updateOnExpedition() {
        setEnergy(getEnergy() - ENERGY_OFFSET);
        updateEnergyLine(getEnergy());
    }
    public void updateAfterExpedition(){
        setExperience(getExperience() + EXPERIENCE_AFTER_EXPEDITION);
        setQuantityOfSpaceWalks(getQuantityOfSpaceWalks() + QUANTITY_OF_SPACE_WALKS_AFTER_EXPEDITION);
    }
    public int getQuantityOfSpaceWalks() {
        return quantityOfSpaceWalks;
    }
    public AstronautDestination getOrder(){
        Random random = new Random();
        int randomNumber = random.nextInt(2)+1;
        if(randomNumber == 1){
            return AstronautDestination.MAINTENANCE_MODULE;
        }else{return AstronautDestination.SCIENTIFIC_MODULE;}
    }
    @Override
    public String getAstronautClass() {
        return "Managing Astronaut";
    }
}
