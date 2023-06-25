package application.aurora.micro_objects;

import application.aurora.tools.tools_micro_objects.AstronautDestination;
import application.aurora.tools.tools_micro_objects.ToolsForAstronaut;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class ManagingAstronaut extends Astronaut {
    private int quantityOfSpaceWalks = 0;
    public ManagingAstronaut(String name, int i, int i1) throws IOException {
        super(name, i, i1);
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
        ToolsForAstronaut.setActivePaneLayout(activePane, getX(),getY());

        ToolsForAstronaut.createLabel(activePane.lookup("#nameLabel"), name);
        ToolsForAstronaut.createLabel(activePane.lookup("#classObject"),getAstronautClass());
        ToolsForAstronaut.createLabel(activePane.lookup("#experienceLabel"),String.valueOf(getExperience()));
        ToolsForAstronaut.createLabel(activePane.lookup("#spaceWalksLabel"),String.valueOf(getQuantityOfSpaceWalks()));

        Line energyLine = ToolsForAstronaut.createLine(activePane.lookup("#energyLine"),
                (int) (energy * 1.56), null, energy);
        ToolsForAstronaut.createLine(activePane.lookup("#energyLineBackground"), null,
                (int) energyLine.getStartX(), energy);

        addChild(activePane);
        removeChild(mainPane);
    }
    private void setQuantityOfSpaceWalks(int i) {
        this.quantityOfSpaceWalks = i;
    }
    public void updateOnExpedition(double exp) {
        setEnergy(getEnergy() - 1);
        setExperience(getExperience() + exp);
        updateEnergyLine(getEnergy(),1);
    }
    public void updateAfterExpedition(){
        setQuantityOfSpaceWalks(getQuantityOfSpaceWalks()+1);
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
