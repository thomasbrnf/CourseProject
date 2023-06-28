package application.aurora.windows;

import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

import static application.aurora.tools.Tools.getLogo;
import static application.aurora.windows.tools.Filters.*;

public class Tabulations {
    @FXML
    private RadioButton byNameButton;
    @FXML
    private RadioButton byExperienceButton;
    @FXML
    private RadioButton bySpaceWalksButton;
    @FXML
    private TextField textField;
    private static AnchorPane search;
    public static void displayWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Tabulations.class.getResource("Tabulations.fxml"));
        TabPane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);

        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("allPane"), allObjectsInWorld(),42);
        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("driftPane"), driftingObjects(),42);
        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("habitationPane"),objectsInModule("Habitation"),42);
        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("scientificPane"),objectsInModule("Scientific"),42);
        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("maintenancePane"),objectsInModule("Maintenance"),42);
        addAstronautData((AnchorPane) fxmlLoader.getNamespace().get("energyPane"),objectsWithLowEnergy(),42);

        search = (AnchorPane) fxmlLoader.getNamespace().get("searchPane");
        setToggleGroup(search);

        Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.setResizable(false);
        window.getIcons().add(getLogo());
        window.setScene(scene);
        window.setOnShown(e -> {
            pane.setOpacity(1);
            Stage mainStage = (Stage) pane.getScene().getWindow();
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
    private static void addAstronautData(AnchorPane pane, ObservableList<AstronautIntern> list, int margin) {
        for (var astronaut : list) {
            if (astronaut == null) {continue;}

            Label id = createLabel(String.valueOf(astronaut.getID()), 16, margin);
            Label nameObject = createLabel(astronaut.getName(), 36, margin);
            Label objectClass = createLabel(String.valueOf(astronaut.getAstronautClass()), 155, margin);
            Label experienceObject = createLabel(String.valueOf(astronaut.getExperienceRounded()), 320, margin);
            Label energyObject = createLabel(String.valueOf(astronaut.getEnergy()), 385, margin);
            Label spaceWalks;

            if(astronaut instanceof ManagingAstronaut)spaceWalks = createLabel(String.valueOf(((ManagingAstronaut)astronaut).getQuantityOfSpaceWalks()), 474, margin);
            else{spaceWalks = createLabel(String.valueOf("0".toCharArray()), 474, margin);}

            Label coordinates = createLabel((int)astronaut.getGroup().getLayoutX() + ", " + (int)astronaut.getGroup().getLayoutY(), 550, margin);
            pane.getChildren().addAll(id, nameObject, objectClass, experienceObject, energyObject, spaceWalks, coordinates);

            margin += 20;
        }
    }
    private static void setToggleGroup(AnchorPane Pane) {
        ToggleGroup group = new ToggleGroup();
        RadioButton byExperienceButton = (RadioButton) Pane.lookup("#byExperienceButton");
        byExperienceButton.setToggleGroup(group);

        RadioButton byNameButton = (RadioButton) Pane.lookup("#byNameButton");
        byNameButton.setToggleGroup(group);

        RadioButton bySpaceWalksButton = (RadioButton) Pane.lookup("#bySpaceWalksButton");
        bySpaceWalksButton.setToggleGroup(group);
    }
    @FXML
    private void onSearchButtonClicked(){
        switch(getFilterOption()){
            case 1 -> addAstronautData(search, Objects.requireNonNull(filterObjectsBy(textField.getText(), 1)),72);
            case 2 -> addAstronautData(search, Objects.requireNonNull(filterObjectsBy(textField.getText(), 2)),72);
            case 3 -> addAstronautData(search, Objects.requireNonNull(filterObjectsBy(textField.getText(), 3)),72);
        }
    }
    private int getFilterOption(){
        if(byExperienceButton.isSelected())return 1;
        else if (byNameButton.isSelected()) return 2;
        else if (bySpaceWalksButton.isSelected())return 3;
        return 0;
    }
    private static Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.CENTER);

        return label;
    }
}