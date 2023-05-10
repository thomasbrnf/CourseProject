package codegame.aurora.dialogs;

import codegame.aurora.Main;
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

public class AstroCreatorController {
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
    public static void showDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AstroCreatorController.class.getResource("Creator.fxml"));
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
    private static void setToggleGroup(AnchorPane astroPane) {
        ToggleGroup group = new ToggleGroup();
        RadioButton astroInternButton = (RadioButton) astroPane.lookup("#astroInternButton");
        astroInternButton.setToggleGroup(group);
        RadioButton astroButton = (RadioButton) astroPane.lookup("#astroButton");
        astroButton.setToggleGroup(group);
        RadioButton managingAstroButton = (RadioButton) astroPane.lookup("#managingAstroButton");
        managingAstroButton.setToggleGroup(group);
    }
    @FXML
    private void onCancelButtonClicked() {
        window.close();
    }
    @FXML
    private void onCreateButtonClicked() {
        if (isInputValid()) {
            astroFillWarning.setOpacity(0);
            String name = astroNameField.getText();
            int astroClass = getSelectedClass();
            int experience = (int) astroExperienceSlider.getValue();
            int energy = (int) astroEnergySlider.getValue();
            try {
                Tools.registerAstronaut(name, astroClass, experience, energy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            astroFillWarning.setOpacity(1);
        }
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
    private boolean isInputValid() {
        return !astroNameField.getText().isEmpty() && isClassSelected();
    }
    private boolean isClassSelected() {
        return astroInternButton.isSelected() || astroButton.isSelected() || managingAstroButton.isSelected();
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
    private void fillDefaultParameters() {
        astroNameField.setText("Leonid Kadeniuk");
        managingAstroButton.setSelected(true);
        astroEnergySlider.setValue(100);
        astroExperienceSlider.setValue(20);
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
