package application.aurora.micro_objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Astronaut extends AstronautIntern {
    public Astronaut(String name, int i, int i1) throws IOException {
        super(name, i, i1);
    }
    @Override
    protected void setImageView() throws FileNotFoundException {
        Image imageObjectMain = new Image(new FileInputStream("src/images/astronaut.png"));

        super.imageView = new ImageView(imageObjectMain);
        super.imageView.setLayoutX(getX());
        super.imageView.setLayoutY(getY() + 17);
    }
    @Override
    public String getAstronautClass() {
        return "Astronaut";
    }
}
