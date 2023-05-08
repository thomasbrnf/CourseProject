package codegame.aurora.dialogs;

import codegame.aurora.Main;
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
    public static void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AstroCreatorController.class.getResource("Creator.fxml"));
        AnchorPane astroPane = fxmlLoader.load();
        Scene scene = new Scene(astroPane);
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create Astronaut");
        window.setResizable(false);
        window.setScene(scene);
        ToggleGroup group = new ToggleGroup();
        RadioButton astroInternButton = (RadioButton) astroPane.lookup("#astroInternButton");
        astroInternButton.setToggleGroup(group);
        RadioButton astroButton = (RadioButton) astroPane.lookup("#astroButton");
        astroButton.setToggleGroup(group);
        RadioButton managingAstroButton = (RadioButton) astroPane.lookup("#managingAstroButton");
        managingAstroButton.setToggleGroup(group);
        window.showAndWait();
    }
    @FXML
    private void cancelButtonClicked() {
        window.close();
    }
    @FXML
    private void createButtonClicked() throws IOException {
        if(!astroNameField.getText().isEmpty() && (astroInternButton.isSelected() || astroButton.isSelected() || managingAstroButton.isSelected())){
            astroFillWarning.setOpacity(0);
            String name = astroNameField.getText();
            int Class = -1;
            int experience = (int)astroExperienceSlider.getValue();
            int energy = (int)astroEnergySlider.getValue();
            if(astroInternButton.isSelected())Class = 0;
            else if (astroButton.isSelected())Class = 1;
            else if (managingAstroButton.isSelected())Class = 2;
            Main.createAstro(name,Class, experience, energy);
        }else astroFillWarning.setOpacity(1);
    }
    @FXML
    private void defaultBoxActive() {
        if (astroDefaultCheckBox.isSelected()) {
            astroNameField.setText("Leonid Kadeniuk");
            managingAstroButton.setSelected(true);
            astroEnergySlider.setValue(100);
            astroExperienceSlider.setValue(20);
            activePane.setDisable(true);
        } else {
            astroNameField.clear();
            managingAstroButton.setSelected(false);
            astroEnergySlider.setValue(0);    
            astroExperienceSlider.setValue(0);
            activePane.setDisable(false);
        }
    }

}
