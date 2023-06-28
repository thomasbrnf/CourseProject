package application.aurora.windows;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static application.aurora.micro_objects.tools.AstronautTools.registerAstronaut;
import static application.aurora.tools.Tools.getLogo;

public class Creations {
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
    public static void displayWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Creations.class.getResource("Creations.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Scene scene = new Scene(anchorPane);

            window = new Stage();
            window.getIcons().add(getLogo());
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(scene);

            setToggleGroup(anchorPane);

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
            fillWarning.setOpacity(0);

            String name = nameField.getText();

            int objectClass = getSelectedClass();
            int experience = (int) experienceSlider.getValue();
            int energy = (int) energySlider.getValue();

            try {
                registerAstronaut(name, objectClass, experience, energy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            fillWarning.setOpacity(1);
        }
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
    private boolean isInputValid() {
        return !nameField.getText().isEmpty() && isClassSelected();
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
        nameField.setText("Leonid Kadeniuk");
        managingAstronautButton.setSelected(true);
        energySlider.setValue(100);
        experienceSlider.setValue(20);
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
