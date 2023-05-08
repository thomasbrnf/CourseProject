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

        int y = 42;
        for (var astro: Main.astros.keySet()) {
            Label id = new Label(String.valueOf(astro.getID()));
            id.setFont(new Font("Arial", 14));
            id.setLayoutY(y);
            id.setTextFill(Color.WHITE);
            id.setLayoutX(16);

            Label name = new Label(String.valueOf(astro.getName()));
            name.setFont(new Font("Arial", 14));
            name.setLayoutY(y);
            name.setTextFill(Color.WHITE);
            name.setLayoutX(36);

            Label Class = new Label(String.valueOf(astro.getAstroClass()));
            Class.setFont(new Font("Arial", 14));
            Class.setLayoutY(y);
            Class.setTextFill(Color.WHITE);
            Class.setLayoutX(155);

            Label exp = new Label(String.valueOf(astro.getExperience()));
            exp.setFont(new Font("Arial", 14));
            exp.setLayoutY(y);
            exp.setTextFill(Color.WHITE);
            exp.setLayoutX(324);

            Label energy = new Label(String.valueOf(astro.getEnergy()));
            energy.setFont(new Font("Arial", 14));
            energy.setLayoutY(y);
            energy.setTextFill(Color.WHITE);
            energy.setLayoutX(405);

            Label sw = new Label(String.valueOf(astro.getQuantityOfSpaceWalks()));
            sw.setFont(new Font("Arial", 14));
            sw.setLayoutY(y);
            sw.setTextFill(Color.WHITE);
            sw.setLayoutX(494);

            tabPane.getChildren().add(id);
            tabPane.getChildren().add(name);
            tabPane.getChildren().add(Class);
            tabPane.getChildren().add(exp);
            tabPane.getChildren().add(energy);
            tabPane.getChildren().add(sw);
            y += 20;
        }
        tabPane.setOpacity(0);
        Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                window.hide();
            }
        });
        window.setResizable(false);
        window.setScene(scene);
        window.setOnShown(e -> tabPane.setOpacity(1));
        window.setOnShown(e -> {
            tabPane.setOpacity(1);
            Stage mainStage = (Stage) tabPane.getScene().getWindow();
            window.setX(mainStage.getX() + (mainStage.getWidth() - window.getWidth()) / 2);
            window.setY(mainStage.getY() + (mainStage.getHeight() - window.getHeight()) / 2);
        });
        scene.setOnKeyPressed(e -> {
            switch(e.getCode()){case TAB, ESCAPE -> tabPane.getScene().getWindow().hide();}
        });
        window.showAndWait();
    }
}