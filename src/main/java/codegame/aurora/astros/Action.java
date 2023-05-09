package codegame.aurora.astros;

import javafx.animation.TranslateTransition;

public interface Action {
    public void toDoResearching();
    public void toGoToSpace();
    public void rest();
    public void toDoMaintenance();

    void delete();
}
