package application.aurora.micro_objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static application.aurora.micro_objects.tools.CONSTANTS.IMAGE_MARGIN;

public class Astronaut extends AstronautIntern {
    public Astronaut(String name, int energy, double experience) throws IOException {
        super(name, energy, experience);
    }
    @Override
    protected void setImageView() throws FileNotFoundException {
        Image imageObjectMain = new Image(new FileInputStream("src/images/astronaut.png"));

        super.imageView = new ImageView(imageObjectMain);
        super.imageView.setLayoutX(getX());
        super.imageView.setLayoutY(getY() + IMAGE_MARGIN);
    }
    @Override
    public String getAstronautClass() {
        return "Astronaut";
    }
}
