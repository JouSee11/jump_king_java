package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

/**
 * The Player class represents the player character in the game.
 * It extends the Entity class and includes properties for movement, physics, and animations.
 */
public class Player extends Entity {
    //player stats
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean isOnGround = true;
    private boolean isJumping = false;
    private boolean isSquatting = false;
    private boolean isBounced = false;
    private int jumpDirection = 0;

    //physics stats - not a constant because it can be changed by power ups
    private double jumpPowerMultiplier = 1.0;
    private double moveSpeedMultiplier = 1.0;


    //animation properties
    private SpriteAnimation runAnimation;
    private SpriteAnimation idleAnimation;
    private SpriteAnimation jumpAnimation;
    private SpriteAnimation fallAnimation;
    private SpriteAnimation squatAnimation;
    private SpriteAnimation bounceAnimation;

    private boolean facingRight = true;


    /**
     * Constructs a Player object with the specified position and size.
     *
     * @param x      The x-coordinate of the player's position.
     * @param y      The y-coordinate of the player's position.
     * @param width  The width of the player.
     * @param height The height of the player.
     */
    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadAnimations();
    }

    //speed controls
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    //ground controls
    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public double getMoveSpeedMultiplier() {
        return moveSpeedMultiplier;
    }

    public void setMoveSpeedMultiplier(double moveSpeedMultiplier) {
        this.moveSpeedMultiplier = moveSpeedMultiplier;
    }

    public void setJumpPowerMultiplier(double jumpPowerMultiplier) {
        this.jumpPowerMultiplier = jumpPowerMultiplier;
    }

    /**
     * Activates a power-up, increasing the player's jump power and movement speed.
     */
    public void powerUpActivate() {
        this.jumpPowerMultiplier = 1.7;
        this.moveSpeedMultiplier = 2.0;
    }

    /**
     * Deactivates a power-up, resetting the player's jump power and movement speed to default values.
     */
    public void powerUpDeactivate() {
        this.jumpPowerMultiplier = 1.0;
        this.moveSpeedMultiplier = 1.0;
    }

    public double getJumpPowerMultiplier() {
        return jumpPowerMultiplier;
    }

    public void setJumpDirection(int jumpDirection) {
        this.jumpDirection = jumpDirection;
    }

    public int getJumpDirection() {
        return jumpDirection;
    }

    /**
     * Sets whether the player is jumping.
     *
     * @param jumping True if the player is jumping, false otherwise.
     */
    public void setJumping(boolean jumping) {
        if (isJumping && !jumping) {
            setVelocityX(0);
        }
        isJumping = jumping;
    }
    public boolean isJumping() {
        return isJumping;
    }

    public void setSquatting(boolean squatting) {
        isSquatting = squatting;
    }

    public boolean isSquatting() {
        return isSquatting;
    }

    public void setBounced(boolean bounced) {
        isBounced = bounced;
    }

    //animation controls
    /**
     * Loads the animations for the player, including idle, run, jump, fall, squat, and bounce animations.
     */
    private void loadAnimations() {
        Image[] idleFrames = loadFrames("/player/idle/idle", 3);
        Image[] runFrames = loadFrames("/player/run/run", 3);
        Image[] jumpFrames = loadFrames("/player/jump/jump", 1);
        Image[] fallFrames = loadFrames("/player/fall/fall", 1);
        Image[] squatFrames = loadFrames("/player/squat/squat", 1);
        Image[] bounceFrames = loadFrames("/player/bounce/bounce", 1);

        idleAnimation = new SpriteAnimation(idleFrames, 1, true);
        runAnimation = new SpriteAnimation(runFrames, 0.6, true);
        jumpAnimation = new SpriteAnimation(jumpFrames, 0.3, true);
        fallAnimation = new SpriteAnimation(fallFrames, 0.3, true);
        squatAnimation = new SpriteAnimation(squatFrames, 0.3, true);
        bounceAnimation = new SpriteAnimation(bounceFrames, 1, true);

        currentAnimation = idleAnimation;
    }

    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/player/playerDefault.png").toExternalForm();
    }

    /**
     * Updates the player's animation based on its current state and elapsed time.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    @Override
    public void updateAnimation(double deltaTime) {
        // Update animation based on player state
        if (isBounced) {
            currentAnimation = bounceAnimation;
        } else if (!isOnGround()) {
            if (velocityY < 0) {
                currentAnimation = jumpAnimation;
            } else {
                currentAnimation = fallAnimation;
            }
        } else if (Math.abs(velocityX) > 0) {
            currentAnimation = runAnimation;
        } else if (isSquatting) {
            currentAnimation = squatAnimation;
        } else {
            currentAnimation = idleAnimation;
        }

        // Update facing direction
        if (velocityX > 0) {
            facingRight = true;
        } else if (velocityX < 0) {
            facingRight = false;
        }

        currentAnimation.update(deltaTime);
    }

    public boolean isFacingRight() {
        return facingRight;
    }


}

