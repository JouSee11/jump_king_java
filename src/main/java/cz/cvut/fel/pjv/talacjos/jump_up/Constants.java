package cz.cvut.fel.pjv.talacjos.jump_up;

/**
 * A utility class that holds constant values used throughout the game.
 * These constants define game settings, physics parameters, player stats, and more.
 */
public class Constants {
    //game window
    /** The width of the game window in pixels. */
    public static final int GAME_WIDTH = 1200;
    /** The height of the game window in pixels. */
    public static final int GAME_HEIGHT = 900;

    //game physics
    /** The gravitational acceleration in the game */
    public static final double GRAVITY = 2400;
    /** The initial upward velocity of a jump. */
    public static final double JUMP_POWER = 1300;
    /** The horizontal movement speed of the player. */
    public static final double MOVE_SPEED = 250;

    /** The maximum time (in seconds) to charge a jump for maximum power. */
    public static final double MAX_JUMP_WAIT = 0.5;
    /** The minimum multiplier for jump force. */
    public static final double MIN_JUMP_COEFFICIENT = 0.1;
    /** The multiplier for jump length compared to running movement. */
    public static final double JUMP_LENGTH_COEFFICIENT = 2.0;
    /** The coefficient determining how bouncy objects are. */
    public static final double BOUNCE_COEFFICIENT = 0.6;

    /** The maximum downward velocity. */
    public static final double TERMINAL_VELOCITY = 1500;

    //player stats
    /** The width of the player character in pixels. */
    public static final double PLAYER_WIDTH = 90;
    /** The height of the player character in pixels. */
    public static final double PLAYER_HEIGHT = 100;

    //collectables stats
    /** The size of collectable items in pixels. */
    public static final int COLLECTABLE_SIZE = 50;
    /** The duration of power-ups in seconds. */
    public static final int POWERUP_DURATION = 10;

    //end stats
    /** The width of the end goal in pixels. */
    public static final int END_WIDTH = 90;
    /** The height of the end goal in pixels. */
    public static final int END_HEIGHT = 100;

    //sounds
    /** The default volume for background music (0.0 to 1.0). */
    public static final double DEFAULT_MUSIC_VOLUME = 0.3;

    private Constants() {}

}
