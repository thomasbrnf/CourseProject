package application.aurora.micro_objects.tools;

public class CONSTANTS {
    // SPAWN COORDINATES
    public static final int ON_CREATED_VALUE_X = 0;
    public static final int ON_CREATED_VALUE_Y = 0;
    public static final int SPAWN_VALUE_X = 1050;
    public static final int SPAWN_VALUE_Y = 650;
    // MARGINS
    public static final int IMAGE_MARGIN = 17;
    public static final int MAIN_PANE_MARGIN = 3;
    public static final int ACTIVE_PANE_MARGIN_X = 22;
    public static final int ACTIVE_PANE_MARGIN_Y = 73;
    // NAME LABEL
    public static final double NAME_DEFAULT_OPACITY = 0.5;
    public static final int NAME_ACTIVE_OPACITY = 1;

    // ENERGY AND ENERGY LINE
    public static final double ENERGY_SCALE_FACTOR = 1.56;
    public static final int ENERGY_OFFSET = 1;
    public static final int ENERGY_DECREMENT = 1;
    public static final int ENERGY_DECREMENT_ON_MAINTENANCE = 3;
    public static final int ENERGY_LINE_ANIMATION_DURATION = 1;
    public static final int ENERGY_THRESHOLD = 20;
    public static final int ENERGY_MINIMUM_VALUE = 0;
    public static final int ENERGY_LINE_INVISIBLE = 0;
    public static final int ENERGY_LINE_VISIBLE = 1;

    // EXPERIENCE VALUE
    public static final int EXPERIENCE_THRESHOLD = 30;
    public static final int HIGH_ENERGY_THRESHOLD = 40;
    public static final int EXPERIENCE_AFTER_EXPEDITION = 4;
    public static final int EXPERIENCE_MINIMUM_VALUE = 0;
    public static final double EXPERIENCE_DECREASES_ON_FAIL = 0.4;
    public static final double EXPERIENCE_INCREASES_ON_SUCCESS = 1;
    public static final double ASTRONAUT_INTERN_EXP = 5;
    public static final double ASTRONAUT_EXP = 8;
    // SPACE WALKS
    public static final int QUANTITY_OF_SPACE_WALKS_DEFAULT= 0;
    public static final int QUANTITY_OF_SPACE_WALKS_AFTER_EXPEDITION = 1;
    // ON MAINTENANCE
    public static final int FAIL_INDEX = 0;
    // ARRAYS OF ASTRONAUTS
    public static final int LAST_ELEMENT_INDEX = 1;
    public static final int MINIMUM_SIZE_OF_ARRAY = 1;
    // TYPE INDEX
    public static final int ASTRONAUT_INTERN = 0;
    public static final int ASTRONAUT = 1;
    public static final int MANAGING_ASTRONAUT = 2;
    // MOVEMENT
    public static final double MOVE_STEP = 10;
    public static final double MOVE_DISTANCE = 1;
    // TO CENTER MOVE COORDINATES
    public static final int TO_CENTER_X = 1080;
    public static final int TO_CENTER_Y = 650;
    // SUCCESS CHASES
    public static final double ASTRONAUT_INTERN_SUCCESS_CHANCE = 0.5;
    public static final double ASTRONAUT_SUCCESS_CHANCE = 0.3;
    public static final double MANAGING_ASTRONAUT_SUCCESS_CHANCE = 0.1;
}
