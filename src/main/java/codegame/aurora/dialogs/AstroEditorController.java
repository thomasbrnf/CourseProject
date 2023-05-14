package codegame.aurora.dialogs;

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
    private TextField astroNameField;
    @FXML
    private RadioButton astroInternButton;
    @FXML
    private RadioButton astroButton;
    @FXML
    private RadioButton managingAstroButton;
    @FXML
    private Slider astroEnergySlider;
    @FXML
    private Slider astroExperienceSlider;
    @FXML
    private CheckBox astroDefaultCheckBox;
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
    public static void setActiveAstroInformation(AnchorPane editorPane, ToggleGroup group) {
        TextField editorNameField = (TextField) editorPane.lookup("#astroNameField");
        editorNameField.setText(astroToEdit.getName());
        Slider editorEnergySlider = (Slider) editorPane.lookup("#astroEnergySlider");
        editorEnergySlider.setValue(astroToEdit.getEnergy());
        Slider editorExperienceSlider = (Slider) editorPane.lookup("#astroExperienceSlider");
        editorExperienceSlider.setValue(astroToEdit.getExperience());
        for (Toggle toggle : group.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;
            if (astroToEdit.getAstroClass().equals(radioButton.getText())) {
                radioButton.setSelected(true);
                break;
            }
        }
    }
    public static void setToggleGroup(AnchorPane anchorPane, ToggleGroup group){
        RadioButton astroInternButton = (RadioButton) anchorPane.lookup("#astroInternButton");
        astroInternButton.setToggleGroup(group);
        RadioButton astroButton = (RadioButton) anchorPane.lookup("#astroButton");
        astroButton.setToggleGroup(group);
        RadioButton managingAstroButton = (RadioButton) anchorPane.lookup("#managingAstroButton");
        managingAstroButton.setToggleGroup(group);
    }
    @FXML
    private void onDefaultBoxSelected() {
        if (astroDefaultCheckBox.isSelected()) {
            fillDefaultParameters();
            activePane.setDisable(true);
        } else {
            clearFields();
            activePane.setDisable(false);
        }
    }
    @FXML
    private void cancelButtonClicked() {window.close();}
    @FXML
    private void applyButtonClicked() {
        if (isInputValid()) {
            astroClass = getSelectedClass();
            astroFillWarning.setOpacity(0);
            astroName = astroNameField.getText();
            astroEnergy = (int) astroEnergySlider.getValue();
            astroExperience = (int) astroExperienceSlider.getValue();
            astroToEdit.setName(astroName);
            astroToEdit.setEnergy(astroEnergy);
            astroToEdit.setExperience(astroExperience);
            astroToEdit.setActive();
            window.close();
        } else {
            astroFillWarning.setOpacity(1);
        }

    }
    private boolean isClassSelected() {
        return astroInternButton.isSelected() || astroButton.isSelected() || managingAstroButton.isSelected();
    }
    private void fillDefaultParameters() {
        astroNameField.setText("Leonid Kadeniuk");
        managingAstroButton.setSelected(true);
        astroEnergySlider.setValue(100);
        astroExperienceSlider.setValue(20);
    }
    private boolean isInputValid() {
        return !astroNameField.getText().isEmpty() && isClassSelected();
    }
    private void clearFields() {
        astroNameField.clear();
        astroInternButton.setSelected(false);
        astroButton.setSelected(false);
        managingAstroButton.setSelected(false);
        astroEnergySlider.setValue(0);
        astroExperienceSlider.setValue(0);
    }
}