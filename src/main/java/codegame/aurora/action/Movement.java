package codegame.aurora.action;

import codegame.aurora.Main;
import javafx.animation.TranslateTransition;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private static List<TranslateTransition> transitions = new ArrayList<>();
    private static boolean isRunning = false;
    public static void drift(){
        for (var object : Main.astros.keySet()) {
            transitions.add(object.drift());
        }
        objectStatus();
    }
    private static void objectStatus() {
        if(isRunning){
            animationStop();
        } else if (!isRunning) {
            animationPlay();
        }
    }
    private static void animationPlay() {
        for(var transition: transitions){
            transition.play();
        }
        isRunning = false;
    }
    private static void animationStop() {
        for(var transition: transitions){
            transition.pause();
        }
        isRunning = true;
    }
    public static void driftStop(){
        try {animationStop();}catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void driftResume(){
        try {animationPlay();}catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
//    public static void driftTo(){
//        driftStop();
//        for(var object: Main.astros){object.driftToSleepModule();}
//    }
}
