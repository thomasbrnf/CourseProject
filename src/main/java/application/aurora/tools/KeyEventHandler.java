package application.aurora.tools;

import application.aurora.macro_objects.tools.ModuleTools;
import application.aurora.windows.Creations;
import application.aurora.windows.Tabulations;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

import static application.aurora.micro_objects.tools.AstronautTools.*;
import static application.aurora.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.getCamera;

public class KeyEventHandler {
    public static void setOnKeyPressed(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case F1 -> Creations.displayWindow();
                case F2 -> editElectedAstronaut();
                case F3 -> JSON.saveDataToFile();
                case F4 -> JSON.openDataFile();
                case C -> cloneElectedAstronauts();
                case B -> astronautsToCenter();
                case TAB -> Tabulations.displayWindow();
                case DELETE -> deleteElectedAstronauts();
                case ESCAPE -> clearActiveAndElectedAstronauts();
                case W, S, A, D -> moveScene(keyEvent.getCode());
                case UP,DOWN,LEFT,RIGHT -> updateAstronautsPosition(keyEvent.getCode());
                case DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5 -> ModuleTools.ejectFromModule(keyEvent.getCode());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void moveScene(KeyCode code) {
        switch (code) {
            case W -> {if (getCamera().getY() + MOVE_BY <= 0) getCamera().moveY(1);}
            case A -> {if (getCamera().getX() + MOVE_BY <= 0) getCamera().moveX(1);}
            case S -> {if (Math.abs(getCamera().getY() - MOVE_BY) <= (WORLD_HEIGHT - SCENE_HEIGHT)) getCamera().moveY(-1);}
            case D -> {if (Math.abs(getCamera().getX() - MOVE_BY) <= (WORLD_WIDTH - SCENE_WIDTH)) getCamera().moveX(-1);}
        }
    }
}
