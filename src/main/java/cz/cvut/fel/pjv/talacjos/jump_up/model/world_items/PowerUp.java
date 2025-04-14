package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

/**
 * The PowerUp class represents a collectible power-up in the game.
 * It extends the Entity class and includes functionality for animations and unique power-up identification.
 */
public class PowerUp extends Entity {
    private int powerUpId;

    /**
     * Constructs a PowerUp object with the specified position and identifier.
     *
     * @param x  The x-coordinate of the power-up's position.
     * @param y  The y-coordinate of the power-up's position.
     * @param id The unique identifier for the power-up.
     */
    public PowerUp(double x, double y, int id) {
        super(x, y, Constants.COLLECTABLE_SIZE, Constants.COLLECTABLE_SIZE);
        this.powerUpId = id;

        loadAnimation();
    }

    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/powerup/powerupDefault.png").toExternalForm();
    }

    /**
     * Loads the animation frames for the power-up.
     * The animation consists of frames located in the specified directory.
     */
    private void loadAnimation() {
        Image[] frames = loadFrames("/powerup/powerup", 3);
        currentAnimation = new SpriteAnimation(frames, 1.5, true);
    }

    /**
     * Updates the animation of the power-up based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    @Override
    public void updateAnimation(double deltaTime) {
        currentAnimation.update(deltaTime);
    }

    public void setPowerUpId(int powerUpId) {
        this.powerUpId = powerUpId;
    }

    public int getPowerUpId() {
        return powerUpId;
    }
}
