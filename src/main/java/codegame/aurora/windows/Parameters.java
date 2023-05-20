package codegame.aurora.windows;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static codegame.aurora.tools.Tools.astronautToEdit;

public class Parameters {
    @FXML
    private Pane activePane;
    @FXML
    private TextField NameField;
    @FXML
    private RadioButton astronautInternButton;
    @FXML
    private RadioButton astronautButton;
    @FXML
    private RadioButton managingAstronautButton;
    @FXML
    private Slider EnergySlider;
    @FXML
    private Slider ExperienceSlider;
    @FXML
    private CheckBox DefaultCheckBox;
    @FXML
    private Label FillWarning;
    private static Stage window;
    int astronautClass;
    static String astronautName;
    static int Energy;
    static int Experience;
    public static void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Parameters.class.getResource("Parameters.fxml"));
        AnchorPane editorPane = fxmlLoader.load();
        Scene scene = new Scene(editorPane);
        ToggleGroup group = new ToggleGroup();
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Astronaut");
        window.setResizable(false);
        window.setScene(scene);
        setToggleGroup(editorPane, group);
        setInformation(editorPane, group);
        window.showAndWait();
    }
    private int getSelectedClass() {
        if (astronautInternButton.isSelected()) {
            return 0;
        } else if (astronautButton.isSelected()) {
            return 1;
        } else if (managingAstronautButton.isSelected()) {
            return 2;
        }
        return -1;
    }
    public static void setInformation(AnchorPane editorPane, ToggleGroup group) {
        TextField NameField = (TextField) editorPane.lookup("#NameField");
        NameField.setText(astronautToEdit.getName());
        Slider EnergySlider = (Slider) editorPane.lookup("#EnergySlider");
        EnergySlider.setValue(astronautToEdit.getEnergy());
        Slider ExperienceSlider = (Slider) editorPane.lookup("#ExperienceSlider");
        ExperienceSlider.setValue(astronautToEdit.getExperience());
        for (Toggle toggle : group.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;
            if (astronautToEdit.getAstronautClass().equals(radioButton.getText())) {
                radioButton.setSelected(true);
                break;
            }
        }
    }
    public static void setToggleGroup(AnchorPane anchorPane, ToggleGroup group){
        RadioButton astronautInternButton = (RadioButton) anchorPane.lookup("#astronautInternButton");
        astronautInternButton.setToggleGroup(group);
        RadioButton astronautButton = (RadioButton) anchorPane.lookup("#astronautButton");
        astronautButton.setToggleGroup(group);
        RadioButton managingAstronautButton = (RadioButton) anchorPane.lookup("#managingAstronautButton");
        managingAstronautButton.setToggleGroup(group);
    }
    @FXML
    private void onDefaultBoxSelected() {
        if (DefaultCheckBox.isSelected()) {
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
            astronautClass = getSelectedClass();
            FillWarning.setOpacity(0);
            astronautName = NameField.getText();
            Energy = (int) EnergySlider.getValue();
            Experience = (int) ExperienceSlider.getValue();
            astronautToEdit.setName(astronautName);
            astronautToEdit.setEnergy(Energy);
            astronautToEdit.setExperience(Experience);
            astronautToEdit.setActive();
            window.close();
        } else {
            FillWarning.setOpacity(1);
        }

    }
    private boolean isClassSelected() {
        return astronautInternButton.isSelected() || astronautButton.isSelected() || managingAstronautButton.isSelected();
    }
    private void fillDefaultParameters() {
        NameField.setText("Leonid Kadeniuk");
        managingAstronautButton.setSelected(true);
        EnergySlider.setValue(100);
        ExperienceSlider.setValue(20);
    }
    private boolean isInputValid() {
        return !NameField.getText().isEmpty() && isClassSelected();
    }
    private void clearFields() {
        NameField.clear();
        astronautInternButton.setSelected(false);
        astronautButton.setSelected(false);
        managingAstronautButton.setSelected(false);
        EnergySlider.setValue(0);
        ExperienceSlider.setValue(0);
    }
}