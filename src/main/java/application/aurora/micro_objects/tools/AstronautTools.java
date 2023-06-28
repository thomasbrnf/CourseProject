package application.aurora.micro_objects.tools;

import application.aurora.macro_objects.HabitationModule;
import application.aurora.macro_objects.MaintenanceModule;
import application.aurora.macro_objects.ScientificModule;
import application.aurora.micro_objects.Astronaut;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;
import application.aurora.windows.Parameters;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application.aurora.world.Main.astronauts;
import static application.aurora.macro_objects.tools.ModuleTools.ejectFromModule;
import static application.aurora.micro_objects.tools.CONSTANTS.*;
import static application.aurora.tools.Tools.*;

public class AstronautTools {
    private static AstronautIntern electedAstronaut = null;
    private static final List<AstronautIntern> electedAstronauts = new ArrayList<>();
    private static final List<AstronautIntern> activeAstronauts = new ArrayList<>();
    public static int totalObjectsCreated = 0;
    public static void setLineOpacity(Line line, int energy) {
        double opacity = (energy == ENERGY_MINIMUM_VALUE) ? ENERGY_LINE_INVISIBLE : ENERGY_LINE_VISIBLE;

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(ENERGY_LINE_ANIMATION_DURATION),
                new KeyValue(line.opacityProperty(), opacity));

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }
    public static void setActivePaneLayout(Pane paneLayout, double x, double y){
        paneLayout.setLayoutX(x - ACTIVE_PANE_MARGIN_X);
        paneLayout.setLayoutY(y - ACTIVE_PANE_MARGIN_Y);
    }
    private static void setUpgradedAstronaut(AstronautIntern upgraded, AstronautIntern astronaut){
        if(astronaut.inModule()) ejectFromModule(astronaut);

        AstronautTools.totalObjectsCreated--;

        upgraded.setXY(astronaut.getGroup().getLayoutX(), astronaut.getGroup().getLayoutY());
        upgraded.setID(astronaut.getID());

        astronaut.delete();

        astronauts.add(upgraded);
    }
    public static void astronautsToCenter() throws FileNotFoundException {
        for(AstronautIntern astronaut: astronauts){
            if(astronaut.inModule()) ejectFromModule(astronaut);
            astronaut.setOnMission(true);
            astronaut.setDestination(AstronautDestination.TO_CENTER);
        }
    }
    private static void assignOrder(AstronautIntern otherAstronaut, ManagingAstronaut astronaut) {
        otherAstronaut.setDestination(astronaut.getOrder());
        otherAstronaut.setOnMission(true);
        System.out.println(astronaut.getName() + " assigned order to " + otherAstronaut.getName());
    }
    private static void assignOrdersToAstronauts(ManagingAstronaut managingAstronaut) {
        for (var otherAstronaut: astronauts) {
            if (canAssignOrder(otherAstronaut) &&
                    managingAstronaut.intersects(otherAstronaut)) {
                assignOrder(otherAstronaut, managingAstronaut);
            }
        }
    }
    public static void deleteElectedAstronauts() throws FileNotFoundException {
        if(electedAstronauts.size() < MINIMUM_SIZE_OF_ARRAY)return;
        List<AstronautIntern> copy = List.copyOf(electedAstronauts);
        for(var astronaut: copy){
            astronaut.delete();
        }
    }
    public static void editElectedAstronaut() throws IOException {
        if(electedAstronauts.size() < MINIMUM_SIZE_OF_ARRAY)return;
        electedAstronaut = electedAstronauts.get(electedAstronauts.size() - LAST_ELEMENT_INDEX);
        Parameters.displayWindow();
    }
    public static void removeChild(AstronautIntern astronautIntern) {
        if(astronautIntern.inModule()){
            for(var module: getModules()){
                if(module.getOccupationAreas().containsValue(astronautIntern))module.ejectAstronaut(astronautIntern);
            }
        }
        getRoot().getChildren().remove(astronautIntern.getGroup());
    }
    public static void registerAstronaut(String name, int type, int experience, int energy) throws IOException {
        if(type == ASTRONAUT_INTERN) astronauts.add(new AstronautIntern(name, energy, experience));
        else if(type == ASTRONAUT) astronauts.add(new Astronaut(name, energy, experience));
        else if(type == MANAGING_ASTRONAUT) astronauts.add(new ManagingAstronaut(name, energy, experience));
    }
    private static void determineDestination(AstronautIntern astronaut) {
        if (astronaut.onMission()) return;

        AstronautDestination destination = getDestinationBasedOnEnergy(astronaut);
        astronaut.setDestination(destination);
    }
    public static void promoteEligibleAstronauts() throws IOException{
        List<AstronautIntern> copy = List.copyOf(astronauts);
        for (AstronautIntern astronaut: copy) {
            if(isEligibleForPromotion(astronaut)){
                upgradeAstronaut(astronaut);
            }
        }
    }
    private static boolean canAssignOrder(AstronautIntern otherAstronaut) {
        return !otherAstronaut.onMission() && otherAstronaut.getEnergy() >= ENERGY_THRESHOLD
                && otherAstronaut.getType() != AstronautType.MANAGING_ASTRONAUT;
    }
    public static Label createLabel(Node node, String text){
        Label label = (Label) node;
        label.setText(text);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    public static Line createLine(Node node, Integer endPosition, Integer startPosition, int energy){
        Line line = (Line) node;
        if(endPosition != null){
            line.setEndX(line.getStartX() + endPosition);
            line.toFront();
            setLineOpacity(line, energy);
        }else{
            line.setStartX(startPosition);
        }
        return line;
    }
    private static AstronautIntern createUpgradedAstronautIntern(AstronautIntern astronaut, double exp) throws FileNotFoundException {
        return new AstronautIntern(astronaut.getName(),astronaut.getEnergy(), exp);
    }
    private static Astronaut createUpgradedAstronaut(AstronautIntern astronaut, double exp) throws IOException {
        return new Astronaut(astronaut.getName(), astronaut.getEnergy(), exp);
    }
    private static ManagingAstronaut createUpgradedManagingAstronaut(AstronautIntern astronaut, double exp) throws IOException {
        return new ManagingAstronaut(astronaut.getName(), astronaut.getEnergy(), exp);
    }
    public static void cloneElectedAstronauts() {
        if(electedAstronauts.size() < MINIMUM_SIZE_OF_ARRAY)return;
        for(var astronaut: electedAstronauts){
            astronaut.clone();
        }
    }
    public static void clearActiveAndElectedAstronauts() throws FileNotFoundException {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            astronaut.toggleActive();
        }

        activeAstronauts.clear();
    }
    private static void moveAstronaut(Group group, KeyCode code) {
        double x = group.getLayoutX();
        double y = group.getLayoutY();
        switch (code) {
            case UP -> y -= MOVE_STEP;
            case DOWN -> y += MOVE_STEP;
            case LEFT -> x -= MOVE_STEP;
            case RIGHT -> x += MOVE_STEP;
        }
        group.setLayoutX(x);
        group.setLayoutY(y);
    }
    private static void moveAstronaut(AstronautIntern astronaut, double x, double y) throws FileNotFoundException {
        double currentX = astronaut.getGroup().getLayoutX();
        double currentY = astronaut.getGroup().getLayoutY();
        double deltaX = 0;
        double deltaY = 0;
        if(currentX == x && currentY == y)astronaut.setOnMission(false);

        if (x != currentX) {
            double xDirection = Math.signum(x - currentX);
            deltaX = Math.min(MOVE_DISTANCE, Math.abs(x - currentX)) * xDirection;
        }

        if (y != currentY) {
            double yDirection = Math.signum(y - currentY);
            deltaY = Math.min(MOVE_DISTANCE, Math.abs(x - currentY)) * yDirection;
        }
        astronaut.setXY(currentX+ deltaX, currentY + deltaY);

        updateCollisionStatus(astronaut);
    }
    private static void upgradeAstronaut(AstronautIntern astronaut) throws IOException {
        if (isAstronaut(astronaut)) {
            ManagingAstronaut upgraded = createUpgradedManagingAstronaut(astronaut, EXPERIENCE_MINIMUM_VALUE);
            setUpgradedAstronaut(upgraded, astronaut);
        }if(isIntern(astronaut)){
            Astronaut upgraded = createUpgradedAstronaut(astronaut, EXPERIENCE_MINIMUM_VALUE);
            setUpgradedAstronaut(upgraded, astronaut);
        }
    }
    public static void upgradeAstronaut(AstronautIntern astronaut, AstronautType type) throws IOException {
        switch (type){
            case ASTRONAUT_INTERN -> {
                AstronautIntern upgraded = createUpgradedAstronautIntern(astronaut, astronaut.getExperience());
                setUpgradedAstronaut(upgraded ,astronaut);
            }
            case ASTRONAUT -> {
                Astronaut upgraded = createUpgradedAstronaut(astronaut, astronaut.getExperience());
                setUpgradedAstronaut(upgraded ,astronaut);
            }
            case MANAGING_ASTRONAUT -> {
                ManagingAstronaut upgraded = createUpgradedManagingAstronaut(astronaut, astronaut.getExperience());
                setUpgradedAstronaut(upgraded ,astronaut);
            }
        }
    }

    public static void updateAstronautsDestination() {
        for (var astronaut: astronauts) {
            determineDestination(astronaut);
        }
    }
    public static void updateManagedAstronauts() {
        for (var managingAstronaut: astronauts) {
            if (managingAstronaut.getType() == AstronautType.MANAGING_ASTRONAUT && !managingAstronaut.inModule()) {
                assignOrdersToAstronauts((ManagingAstronaut) managingAstronaut);
            }
        }
    }
    public static void updateAstronautsPosition(KeyCode code) throws FileNotFoundException {
        List<AstronautIntern> copy = List.copyOf(activeAstronauts);
        for (var astronaut : copy) {
            moveAstronaut(astronaut.getGroup(), code);
            updateCollisionStatus(astronaut);
        }
    }
    public static void updateAstronautsPosition() throws FileNotFoundException {
        for (AstronautIntern astronaut : astronauts) {
            if (astronaut.isActive() || astronaut.inModule() || astronaut.isElect()) {
                continue;
            }
            if (astronaut.getDestination() == AstronautDestination.HABITATION_MODULE) {
                moveAstronaut(astronaut, HabitationModule.getInstance().getGroup().getLayoutX(), HabitationModule.getInstance().getGroup().getLayoutY());
            } else if (astronaut.getDestination() == AstronautDestination.SCIENTIFIC_MODULE) {
                moveAstronaut(astronaut, ScientificModule.getInstance().getGroup().getLayoutX(), ScientificModule.getInstance().getGroup().getLayoutY());
            } else if(astronaut.getDestination() == AstronautDestination.MAINTENANCE_MODULE){
                moveAstronaut(astronaut, MaintenanceModule.getInstance().getGroup().getLayoutX(), MaintenanceModule.getInstance().getGroup().getLayoutY());
            }else {
                moveAstronaut(astronaut,TO_CENTER_X, TO_CENTER_Y);}
        }
    }
    public static void updateAstronautStatus(AstronautIntern astronautIntern) {
        getUserInterface().getInfoPanel().setAstronautInfo(astronautIntern);
    }
    public static void deleteAllAstronauts() {
        List<AstronautIntern> copy = List.copyOf(astronauts);
        for(var astronaut: copy){
            astronaut.delete();
        }
    }
    private static boolean isEligibleForPromotion(AstronautIntern astronaut) {
        return astronaut.getExperience() >= EXPERIENCE_THRESHOLD && astronaut.getType() != AstronautType.MANAGING_ASTRONAUT;
    }
    private static boolean isIntern(AstronautIntern astronaut) {
        return astronaut.getType() == AstronautType.ASTRONAUT_INTERN;
    }
    private static boolean isAstronaut(AstronautIntern astronaut) {
        return astronaut.getType() == AstronautType.ASTRONAUT;
    }
    private static AstronautDestination getDestinationBasedOnEnergy(AstronautIntern astronaut) {
        if (astronaut.getEnergy() < ENERGY_THRESHOLD) {
            return AstronautDestination.HABITATION_MODULE;
        } else if (astronaut.getEnergy() > HIGH_ENERGY_THRESHOLD) {
            return AstronautDestination.SCIENTIFIC_MODULE;
        } else {
            return AstronautDestination.MAINTENANCE_MODULE;
        }
    }
    public static double getSuccessChance(AstronautIntern astronaut){
        if(astronaut instanceof ManagingAstronaut){
            return MANAGING_ASTRONAUT_SUCCESS_CHANCE;
        }
        if(astronaut instanceof Astronaut){
            return ASTRONAUT_SUCCESS_CHANCE;
        }
        return ASTRONAUT_INTERN_SUCCESS_CHANCE;
    }
    public static double getAmountOfExperience(AstronautIntern astronaut){
        if(astronaut instanceof Astronaut){
            return ASTRONAUT_EXP;
        }
        return ASTRONAUT_INTERN_EXP;
    }
    public static int getQuantityOfSpaceWalks(AstronautIntern astronautIntern) {
        if(astronautIntern instanceof ManagingAstronaut)return ((ManagingAstronaut) astronautIntern).getQuantityOfSpaceWalks();
        return -1;
    }
    public static int getRandomSpawnNumber(int num){
        int min = num - 150;
        int max = num + 150;
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
    public static AstronautIntern getElectedAstronaut(){
        return electedAstronaut;
    }
    public static List<AstronautIntern> getElectedAstronauts(){
        return electedAstronauts;
    }
    public static List<AstronautIntern> getActiveAstronauts(){
        return activeAstronauts;
    }
}
