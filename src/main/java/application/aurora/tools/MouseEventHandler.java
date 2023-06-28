package application.aurora.tools;

import application.aurora.macro_objects.Module;
import application.aurora.micro_objects.AstronautIntern;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.List;

import static application.aurora.world.Main.astronauts;
import static application.aurora.tools.Tools.*;

public class MouseEventHandler {
    private static boolean isMiniMapClicked = false;
    public static void setOnMouseClicked(MouseEvent mouseEvent)  {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            handlePrimaryClick(mouseEvent);
        }
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            handleSecondaryClick(mouseEvent);
        }
    }
    private static void handlePrimaryClick(MouseEvent mouseEvent) {
        isMiniMapClicked = false;

        handleMiniMapClick(mouseEvent);
        if(isMiniMapClicked)return;

        handleAstronautClick(mouseEvent);
        handleModuleClick(mouseEvent);
    }
    private static void handleSecondaryClick(MouseEvent mouseEvent) {
        AstronautIntern astronaut = checkClickAstronaut(astronauts.stream().toList(),
                mouseEvent.getX(), mouseEvent.getY());
        if (astronaut != null)astronaut.toggleElect();
    }
    public static void setOnMouseEntered() {
        getUserInterface().getMiniMap().getMapGroup().setOpacity(1);
    }
    public static void setOnMouseExited() {
        getUserInterface().getMiniMap().getMapGroup().setOpacity(0.5);
    }
    private static void handleMiniMapClick(MouseEvent mouseEvent){
        if(getUserInterface().getMiniMap().getView().contains(mouseEvent.getX(), mouseEvent.getY())) {
            double x = getCamera().calculate(mouseEvent.getX(), 0);
            double y = getCamera().calculate(mouseEvent.getY(), 1);
            getCamera().moveCameraX(-x);
            getCamera().moveCameraY(-y);

            isMiniMapClicked = true;
        }
    }
    private static void handleAstronautClick(MouseEvent mouseEvent) {
        AstronautIntern astronaut = checkClickAstronaut(astronauts.stream().toList(),
                mouseEvent.getX(), mouseEvent.getY());
        if (astronaut != null && !astronaut.isElect())astronaut.toggleActive();
    }
    private static void handleModuleClick(MouseEvent mouseEvent) {
        Module module = checkClickModule(getModules().stream().toList(), mouseEvent.getX(), mouseEvent.getY());
        if(module != null)module.toggleActive();
    }
    private static Module checkClickModule(List<Module> list, double x, double y) {
        for(var module: list)
            if(module.getGroup().getBoundsInParent().contains(
                    x - getRoot().getTranslateX(),y - getRoot().getTranslateY())
                    && module.getOccupationAreas() != null)
                return module;
        return null;
    }
    public static AstronautIntern checkClickAstronaut(List<AstronautIntern> list, double x, double y) {
        for (var astronaut : list)
            if (astronaut.getGroup().getBoundsInParent().contains(
                    x - getRoot().getTranslateX(),
                    y - getRoot().getTranslateY()) && !astronaut.inModule())
                return astronaut;
        return null;
    }
}
