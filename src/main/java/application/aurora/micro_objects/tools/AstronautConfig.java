package application.aurora.micro_objects.tools;

import application.aurora.world.Main;
import application.aurora.macro_objects.tools.ModuleTools;
import application.aurora.micro_objects.Astronaut;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.ManagingAstronaut;

import java.io.IOException;
import java.io.Serializable;

public record AstronautConfig(
        int ID,
        int energy,
        int quantityOfSpaceWalks,
        double experience,
        double x,
        double y,
        boolean isActive,
        boolean elect,
        boolean inModule,
        boolean onMission,
        String name,
        String type,
        String moduleClass
) implements Serializable {
    public static AstronautConfig fromAstronaut(AstronautIntern astronautIntern){
        return new AstronautConfig(
                astronautIntern.getID(),
                astronautIntern.getEnergy(),
                AstronautTools.getQuantityOfSpaceWalks(astronautIntern),
                astronautIntern.getExperience(),
                astronautIntern.getGroup().getLayoutX(),
                astronautIntern.getGroup().getLayoutY(),
                astronautIntern.getActive(),
                astronautIntern.getElect(),
                astronautIntern.getInModule(),
                astronautIntern.onMission(),
                astronautIntern.getName(),
                astronautIntern.getClass().getSimpleName(),
                ModuleTools.getModuleName(astronautIntern));
    }
    public void toAstronaut() throws IOException {
        var result = switch (type){
            case "AstronautIntern" -> new AstronautIntern(name,energy, experience);
            case "Astronaut" -> new Astronaut(name,energy, experience);
            case "ManagingAstronaut" -> new ManagingAstronaut(name,energy, experience);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        result.setID(ID);
        if(inModule) ModuleTools.assignToModule(moduleClass,result);
        else{result.setXY(x,y);}
        if(result instanceof ManagingAstronaut)
            ((ManagingAstronaut) result).setQuantityOfSpaceWalks(quantityOfSpaceWalks);
        result.setOnMission(onMission);
        if(elect)result.toggleElect();
        if(isActive)result.toggleActive();
        Main.astronauts.add(result);
    }
}
