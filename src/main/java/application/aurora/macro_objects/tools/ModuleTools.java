package application.aurora.macro_objects.tools;

import application.aurora.macro_objects.Module;
import application.aurora.micro_objects.AstronautIntern;
import javafx.scene.input.KeyCode;

import static application.aurora.tools.Tools.getModules;

public class ModuleTools {
    private static Module activeModule;
    public static void setActiveModule(Module module){
        activeModule = module;
    }
    public static void assignToModule(String moduleClass, AstronautIntern astronautIntern){
        for(var module: getModules()){
            if(module.getClass().getSimpleName().
                    equals(moduleClass))astronautIntern.setXY(module.getGroup().getLayoutX(), module.getGroup().getLayoutY());
        }
    }
    public static void ejectFromModule(KeyCode code) {
        for(var module: getModules()){
            if(module.isActive())module.ejectAstronaut(code);
        }
    }
    public static void ejectFromModule(AstronautIntern astronaut) {
        for(var module: getModules()){
            if(module.getOccupationAreas() == null){continue;}
            if (module.getOccupationAreas().containsValue(astronaut)) {
                module.ejectAstronaut(astronaut);
                astronaut.setInModule(false);
                break;
            }
        }
    }
    public static String getModuleName(AstronautIntern astronautIntern) {
        for(var module: getModules()){
            if(module.getOccupationAreas().containsValue(astronautIntern))return module.getClass().getSimpleName();
        }
        return null;
    }
    public static Module getActiveModule(){
        return activeModule;
    }
}
