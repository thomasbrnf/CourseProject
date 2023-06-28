package application.aurora.windows;

import application.aurora.micro_objects.Astronaut;
import application.aurora.micro_objects.tools.AstronautType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static application.aurora.micro_objects.tools.AstronautTools.*;
import static application.aurora.tools.Tools.getLogo;

public class Parameters {
    @FXML
    private Pane activePane;
    @FXML
    private TextField nameField;
    @FXML
    private RadioButton astronautInternButton;
    @FXML
    private RadioButton astronautButton;
    @FXML
    private RadioButton managingAstronautButton;
    @FXML
    private Slider energySlider;
    @FXML
    private Slider experienceSlider;
    @FXML
    private CheckBox defaultCheckBox;
    @FXML
    private Label fillWarning;
    private static Stage window;
    static String astronautName;
    static int energy;
    static int experience;
    public static void displayWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Parameters.class.getResource("Parameters.fxml"));
        AnchorPane editorPane = fxmlLoader.load();
        Scene scene = new Scene(editorPane);

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.getIcons().add(getLogo());
        window.setScene(scene);

        ToggleGroup group = new ToggleGroup();
        setToggleGroup(editorPane, group);
        setInformation(editorPane, group);

        window.showAndWait();
    }
    private AstronautType getSelectedClass() {
        if (astronautInternButton.isSelected()) {
            return AstronautType.ASTRONAUT_INTERN;
        } else if (astronautButton.isSelected()) {
            return AstronautType.ASTRONAUT;
        } else if (managingAstronautButton.isSelected()) {
            return AstronautType.MANAGING_ASTRONAUT;
        }
        return null;
    }
    public static void setInformation(AnchorPane editorPane, ToggleGroup group) {
        TextField nameField = (TextField) editorPane.lookup("#nameField");
        nameField.setText(getElectedAstronaut().getName());

        Slider energySlider = (Slider) editorPane.lookup("#energySlider");
        energySlider.setValue(getElectedAstronaut().getEnergy());

        Slider experienceSlider = (Slider) editorPane.lookup("#experienceSlider");
        experienceSlider.setValue(getElectedAstronaut().getExperience());

        for (Toggle toggle : group.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;
            if (getElectedAstronaut().getAstronautClass().equals(radioButton.getText())) {
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
        if (defaultCheckBox.isSelected()) {
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
    private void applyButtonClicked() throws IOException {
        if (isInputValid()) {
            fillWarning.setOpacity(0);
            astronautName = nameField.getText();
            energy = (int) energySlider.getValue();
            experience = (int) experienceSlider.getValue();
            getElectedAstronaut().setName(astronautName);
            getElectedAstronaut().setEnergy(energy);
            getElectedAstronaut().setExperience(experience);
            if(getElectedAstronaut().getType() != getSelectedClass())
                upgradeAstronaut(getElectedAstronaut(), Objects.requireNonNull(getSelectedClass()));
            window.close();
        } else {
            fillWarning.setOpacity(1);
        }

    }
    private boolean isClassSelected() {
        return astronautInternButton.isSelected() || astronautButton.isSelected() || managingAstronautButton.isSelected();
    }
    private void fillDefaultParameters() {
        nameField.setText("Leonid Kadeniuk");
        managingAstronautButton.setSelected(true);
        energySlider.setValue(100);
        experienceSlider.setValue(20);
    }
    private boolean isInputValid() {
        return !nameField.getText().isEmpty() && isClassSelected();
    }
    private void clearFields() {
        nameField.clear();
        astronautInternButton.setSelected(false);
        astronautButton.setSelected(false);
        managingAstronautButton.setSelected(false);
        energySlider.setValue(0);
        experienceSlider.setValue(0);
    }
}