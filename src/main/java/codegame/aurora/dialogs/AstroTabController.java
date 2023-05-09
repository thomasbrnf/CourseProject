package codegame.aurora.dialogs;

import codegame.aurora.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AstroTabController {
    public static void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AstroCreatorController.class.getResource("Tab.fxml"));
        Pane tabPane = fxmlLoader.load();
        Scene scene = new Scene(tabPane);
        scene.setFill(Color.TRANSPARENT);
        addAstronautData(tabPane);
        Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.setResizable(false);
        window.setScene(scene);
        window.setOnShown(e -> {
            tabPane.setOpacity(1);
            Stage mainStage = (Stage) tabPane.getScene().getWindow();
            window.setX(mainStage.getX() + (mainStage.getWidth() - window.getWidth()) / 2);
            window.setY(mainStage.getY() + (mainStage.getHeight() - window.getHeight()) / 2);
        });
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                window.hide();
            }
        });
        scene.setOnKeyPressed(e -> {
            switch(e.getCode()){
                case TAB, ESCAPE -> window.hide();
            }
        });
        window.showAndWait();
    }
    private static void addAstronautData(Pane tabPane) {
        int y = 42;
        for (var astro: Main.astros.keySet()) {
            Label id = createLabel(String.valueOf(astro.getID()), 16, y);
            Label name = createLabel(astro.getName(), 36, y);
            Label astroClass = createLabel(String.valueOf(astro.getAstroClass()), 155, y);
            Label experience = createLabel(String.valueOf(astro.getExperience()), 324, y);
            Label energy = createLabel(String.valueOf(astro.getEnergy()), 405, y);
            Label spaceWalks = createLabel(String.valueOf(astro.getQuantityOfSpaceWalks()), 494, y);
            tabPane.getChildren().addAll(id, name, astroClass, experience, energy, spaceWalks);
            y += 20;
        }
    }
    private static Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setTextFill(Color.WHITE);
        return label;
    }
}