package com.xebialabs.craftsmanship.helper;

public class ConstantValues {

    public static final int GRID_WIDTH=16;

    public static final int GRID_HEIGHT=16;

    public static final String GAME_ID="match-";

    public static final String POST_EXTENSION="/xl-spaceship/protocol/game/new";

    public static final String GET_EXTENSION="/xl-spaceship/user/game/";

    public static final String SELF="self";

    public static final String OPPONENT="opponent";

    /*BOARD Constants*/

    //An empty or unknown quadrant
    public static final String EMPTY_QUADRANT=".";

    //A quadrant taken by part of a ship which has not been hit yet.
    public static final String NOT_HIT_QUADRANT="*";

    //A quadrant taken by part of a ship which was hit by a shot.
    public static final String HIT_QUADRANT="X";

    //A quadrant that contains a missed shot
    public static final String MISSED_QUADRANT="-";

    /*Spaceship Constants*/

    public static final String A_CLASS_SPACESHIP = "A Class Spaceship";

    public static final String B_CLASS_SPACESHIP = "B Class Spaceship";

    public static final String S_CLASS_SPACESHIP = "S Class Spaceship";

    public static final String ANGLE_SPACESHIP = "Angle Spaceship";

    public static final String WINGER_SPACESHIP = "Winger Spaceship";

}


