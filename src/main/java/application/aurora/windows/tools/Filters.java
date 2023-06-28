package application.aurora.windows.tools;

import application.aurora.macro_objects.HabitationModule;
import application.aurora.macro_objects.MaintenanceModule;
import application.aurora.macro_objects.Module;
import application.aurora.macro_objects.ScientificModule;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static application.aurora.world.Main.astronauts;
import static application.aurora.tools.Tools.getModules;

public class Filters {
    public static ObservableList<AstronautIntern> driftingObjects() {
        List<AstronautIntern> objects = new ArrayList<>();
        Map<AstronautIntern, Set<Module>> astronautModules = new HashMap<>();
        for (var module : getModules()) {
            for (var astronaut : module.getOccupationAreas().values()) {
                astronautModules.computeIfAbsent(astronaut, k -> new HashSet<>()).add(module);
            }
        }
        for (var astronaut : astronauts) {
            if (!astronautModules.containsKey(astronaut)) {
                objects.add(astronaut);
            }
        }
        return FXCollections.observableArrayList(objects);
    }
    public static ObservableList<AstronautIntern> objectsInModule(String module) throws FileNotFoundException {
        List<AstronautIntern> objects = new ArrayList<>();
        switch (module) {
            case "Habitation" -> objects.addAll(HabitationModule.getInstance().getOccupationAreas().values());
            case "Scientific" -> objects.addAll(ScientificModule.getInstance().getOccupationAreas().values());
            case "Maintenance" -> objects.addAll(MaintenanceModule.getInstance().getOccupationAreas().values());
        }
        return FXCollections.observableArrayList(objects);
    }
    public static ObservableList<AstronautIntern> objectsWithLowEnergy() {
        Stream<Integer> energyStream = astronauts.stream().map(AstronautIntern::getEnergy).filter(e -> e < 30);
        return FXCollections.observableArrayList(energyStream.map(e -> astronauts.stream()
                        .filter(a -> a.getEnergy() == e)
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }
    public static ObservableList<AstronautIntern> allObjectsInWorld(){
        return FXCollections.observableArrayList(astronauts);
    }
    public static ObservableList<AstronautIntern> filterObjectsBy(String text, int type){
        switch(type){
            case 1 -> {Stream<AstronautIntern> experienceFilter = astronauts.stream().filter(astronaut -> astronaut.getExperience() == Integer.parseInt(text));
                return FXCollections.observableArrayList(experienceFilter.toList());}
            case 2 -> {
                Stream<AstronautIntern> nameFilter = astronauts.stream().filter(astronaut -> astronaut.getName().contains(text));
                return FXCollections.observableArrayList(nameFilter.toList());}
            case 3 -> {Stream<AstronautIntern> spaceWalksFilter = astronauts.stream().filter(astronaut -> ((ManagingAstronaut)astronaut).getQuantityOfSpaceWalks() == Integer.parseInt(text));
                return FXCollections.observableArrayList(spaceWalksFilter.toList());}
        }
        return null;
    }
}
