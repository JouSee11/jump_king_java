package cz.cvut.fel.pjv.talacjos.jump_up;

public class Constants {
    //game window
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 900;

    //game physics
    public static final double GRAVITY = 2400;
    public static final double JUMP_POWER = 1300;
    public static final double MOVE_SPEED = 250;


    public static final double MAX_JUMP_WAIT = 0.5; // how long (in seconds) is max wait before max jump charge
    public static final double MIN_JUMP_COEFFICIENT = 0.1; // what is the minimum jump length multiplier
    public static final double JUMP_LENGTH_COEFFICIENT = 2.0; //how much is jump longer compared to run movement
    public static final double BOUNCE_COEFFICIENT = 0.6; // how bouncy will the objects be

    public static final double TERMINAL_VELOCITY = 1500;

    //player stats
    public static final double PLAYER_WIDTH = 90;
    public static final double PLAYER_HEIGHT = 100;

    //collectables stats
    public static final int COLLECTABLE_SIZE = 50;
    public static final int POWERUP_DURATION = 4;

    private Constants() {}

}
