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

import static codegame.aurora.tools.Tools.astroToEdit;

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
    @FXML
    private Label astroFillWarning;
    private static Stage window;
    int astroClass;
    static String astroName;
    static int astroEnergy;
    static int astroExperience;
    public static void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AstroEditorController.class.getResource("Editor.fxml"));
        AnchorPane editorPane = fxmlLoader.load();
        Scene scene = new Scene(editorPane);
        ToggleGroup group = new ToggleGroup();
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Astronaut");
        window.setResizable(false);
        window.setScene(scene);
        setToggleGroup(editorPane, group);
        setActiveAstroInformation(editorPane, group);
        window.showAndWait();
    }
    private boolean isClassSelected() {
        return astroInternButton.isSelected() || astroButton.isSelected() || managingAstroButton.isSelected();
    }
    public static void setActiveAstroInformation(AnchorPane editorPane, ToggleGroup group) {
        TextField editorNameField = (TextField) editorPane.lookup("#editorNameField");
        editorNameField.setText(astroToEdit.getName());
        Slider editorEnergySlider = (Slider) editorPane.lookup("#editorEnergySlider");
        editorEnergySlider.setValue(astroToEdit.getEnergy());
        Slider editorExperienceSlider = (Slider) editorPane.lookup("#editorExperienceSlider");
        editorExperienceSlider.setValue(astroToEdit.getExperience());
        for (Toggle toggle : group.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;
            if (astroToEdit.getAstroClass().equals(radioButton.getText())) {
                radioButton.setSelected(true);
                break;
            }
        }
    }
    private int getSelectedClass() {
        if (astroInternButton.isSelected()) {
            return 0;
        } else if (astroButton.isSelected()) {
            return 1;
        } else if (managingAstroButton.isSelected()) {
            return 2;
        }
        return -1;
    }
    public static void setToggleGroup(AnchorPane editorPane, ToggleGroup group){
        RadioButton astroInternButton = (RadioButton) editorPane.lookup("#astroInternButton");
        astroInternButton.setToggleGroup(group);
        RadioButton astroButton = (RadioButton) editorPane.lookup("#astroButton");
        astroButton.setToggleGroup(group);
        RadioButton managingAstroButton = (RadioButton) editorPane.lookup("#managingAstroButton");
        managingAstroButton.setToggleGroup(group);
    }
    @FXML
    private void onDefaultBoxSelected() {
        if (editorDefaultCheckBox.isSelected()) {
            fillDefaultParameters();
            activePane.setDisable(true);
        } else {
            clearFields();
            activePane.setDisable(false);
        }
    }
    private void fillDefaultParameters() {
        editorNameField.setText("Leonid Kadeniuk");
        managingAstroButton.setSelected(true);
        editorEnergySlider.setValue(100);
        editorExperienceSlider.setValue(20);
    }
    @FXML
    private void cancelButtonClicked() {window.close();}
    @FXML
    private void applyButtonClicked() {
        if (isInputValid()) {
            astroClass = getSelectedClass();
            astroFillWarning.setOpacity(0);
            astroName = editorNameField.getText();
            astroEnergy = (int) editorEnergySlider.getValue();
            astroExperience = (int) editorExperienceSlider.getValue();
            astroToEdit.setName(astroName);
            astroToEdit.setEnergy(astroEnergy);
            astroToEdit.setExperience(astroExperience);
            astroToEdit.deactivateAstro();
            window.close();
        } else {
            astroFillWarning.setOpacity(1);
        }

    }
    private boolean isInputValid() {
        return !editorNameField.getText().isEmpty() && isClassSelected();
    }
    private void clearFields() {
        editorNameField.clear();
        astroInternButton.setSelected(false);
        astroButton.setSelected(false);
        managingAstroButton.setSelected(false);
        editorEnergySlider.setValue(0);
        editorExperienceSlider.setValue(0);
    }
}