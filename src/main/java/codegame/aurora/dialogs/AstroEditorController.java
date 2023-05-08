package codegame.aurora.dialogs;

import codegame.aurora.astros.AstronautIntern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static codegame.aurora.Main.activeAstro;

public class AstroEditorController {
    @FXML
    private Pane activePane;
    @FXML
    private TextField editorNameField;
    @FXML
    private RadioButton astroInternButton;
    @FXML
    private RadioButton astroButton;
    @FXML
    private RadioButton managingAstroButton;
    @FXML
    private Slider editorEnergySlider;
    @FXML
    private Slider editorExperienceSlider;
    @FXML
    private CheckBox editorDefaultCheckBox;
    private static Stage window;
    int astroClass;
    static String astroName;
    static int astroEnergy;
    static int astroExperience;

    public static void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AstroEditorController.class.getResource("Editor.fxml"));
        AnchorPane editorPane = fxmlLoader.load();
        Scene scene = new Scene(editorPane);
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Astronaut");
        window.setResizable(false);
        window.setScene(scene);
        ToggleGroup group = new ToggleGroup();
        RadioButton astroInternButton = (RadioButton) editorPane.lookup("#astroInternButton");
        astroInternButton.setToggleGroup(group);
        RadioButton astroButton = (RadioButton) editorPane.lookup("#astroButton");
        astroButton.setToggleGroup(group);
        RadioButton managingAstroButton = (RadioButton) editorPane.lookup("#managingAstroButton");
        managingAstroButton.setToggleGroup(group);
        AstronautIntern editedAstronaut;
        if (window.getModality() == Modality.APPLICATION_MODAL) {
            editedAstronaut = activeAstro;
        } else {
            System.out.println("Done");
            editedAstronaut = new AstronautIntern("", 0, 0);
        }
        switch (editedAstronaut.getAstroClass()) {
            case "Astronaut-Intern" -> astroInternButton.setSelected(true);
            case "Astronaut" -> astroButton.setSelected(true);
            case "ManagingAstronaut" -> managingAstroButton.setSelected(true);
        }
        TextField editorNameField = (TextField) editorPane.lookup("#editorNameField");
        editorNameField.setText(editedAstronaut.getName());
        Slider editorEnergySlider = (Slider) editorPane.lookup("#editorEnergySlider");
        editorEnergySlider.setValue(editedAstronaut.getEnergy());
        Slider editorExperienceSlider = (Slider) editorPane.lookup("#editorExperienceSlider");
        editorExperienceSlider.setValue(editedAstronaut.getExperience());
        window.showAndWait();
        if (window.getModality() == Modality.APPLICATION_MODAL) {
            activeAstro.setName(astroName);
            activeAstro.setEnergy(astroEnergy);
            activeAstro.setExperience(astroExperience);
        }
    }
    @FXML
    private void defaultBoxActive() {
        if (editorDefaultCheckBox.isSelected()) {
            editorNameField.setText("Leonid Kadeniuk");
            managingAstroButton.setSelected(true);
            editorEnergySlider.setValue(100);
            editorExperienceSlider.setValue(20);
            activePane.setDisable(true);
        } else {
            editorNameField.clear();
            managingAstroButton.setSelected(false);
            editorEnergySlider.setValue(0);
            editorExperienceSlider.setValue(0);
            activePane.setDisable(false);
        }
    }
    @FXML
    private void cancelButtonClicked() {window.close();}
    @FXML
    private void applyButtonClicked() {
        if(astroInternButton.isSelected())astroClass = 0;
        if(astroButton.isSelected())astroClass = 1;
        if(managingAstroButton.isSelected())astroClass = 2;
        astroName = editorNameField.getText();
        astroEnergy = (int) editorEnergySlider.getValue();
        astroExperience = (int) editorExperienceSlider.getValue();
        window.close();
    }
}