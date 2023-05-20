package codegame.aurora.windows;

import codegame.aurora.tools.Tools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Creations {
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
    public static void showDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Creations.class.getResource("Creations.fxml"));
            AnchorPane astroPane = fxmlLoader.load();
            Scene scene = new Scene(astroPane);
            window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Create Astronaut");
            window.setResizable(false);
            window.setScene(scene);
            setToggleGroup(astroPane);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void setToggleGroup(AnchorPane Pane) {
        ToggleGroup group = new ToggleGroup();
        RadioButton astronautInternButton = (RadioButton) Pane.lookup("#astronautInternButton");
        astronautInternButton.setToggleGroup(group);
        RadioButton astronautButton = (RadioButton) Pane.lookup("#astronautButton");
        astronautButton.setToggleGroup(group);
        RadioButton managingAstronautButton = (RadioButton) Pane.lookup("#managingAstronautButton");
        managingAstronautButton.setToggleGroup(group);
    }
    @FXML
    private void onCancelButtonClicked() {
        window.close();
    }
    @FXML
    private void onCreateButtonClicked() {
        if (isInputValid()) {
            FillWarning.setOpacity(0);
            String name = NameField.getText();
            int astroClass = getSelectedClass();
            int experience = (int) ExperienceSlider.getValue();
            int energy = (int) EnergySlider.getValue();
            try {
                Tools.registerAstronaut(name, astroClass, experience, energy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            FillWarning.setOpacity(1);
        }
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
    private boolean isInputValid() {
        return !NameField.getText().isEmpty() && isClassSelected();
    }
    private boolean isClassSelected() {
        return astronautInternButton.isSelected() || astronautButton.isSelected() || managingAstronautButton.isSelected();
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
    private void fillDefaultParameters() {
        NameField.setText("Leonid Kadeniuk");
        managingAstronautButton.setSelected(true);
        EnergySlider.setValue(100);
        ExperienceSlider.setValue(20);
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
