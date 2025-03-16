package cz.cvut.fel.pjv.talacjos.jump_up;

public class Constants {
    //game window
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 900;

    //game physics
    public static final double GRAVITY = 2600;
    public static final double MAX_JUMP_WAIT = 0.5;
    public static final double MIN_JUMP_COEFFICIENT = 0.1;
    public static final double JUMP_LENGTH_COEFFICIENT = 1.4; //how jump is longer compared to run
    public static final double TERMINAL_VELOCITY = 2000;

    //player stats
    public static final double PLAYER_WIDTH = 90;
    public static final double PLAYER_HEIGHT = 100;

    private Constants() {}

}
