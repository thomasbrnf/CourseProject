package application.aurora.ui;

import javafx.scene.layout.Pane;

import static application.aurora.tools.CONSTANTS.*;
public class UserInterface {
    private final MiniMap miniMap = new MiniMap();
    private final InfoPanel infoPanel;
    private final Pane uiPane;
    public UserInterface() {
        infoPanel = new InfoPanel(INFO_PANEL_X, INFO_PANEL_Y);
        uiPane = createUIPane(miniMap, infoPanel);

    }
    private Pane createUIPane(MiniMap miniMap, InfoPanel infoPanel) {
        return new Pane(miniMap.getMapGroup(),
                infoPanel.getGroupInfoPanel());
    }
    public MiniMap getMiniMap(){
        return miniMap;
    }
    public Pane getUIPane(){
        return uiPane;
    }
    public InfoPanel getInfoPanel(){
        return infoPanel;
    }
}
