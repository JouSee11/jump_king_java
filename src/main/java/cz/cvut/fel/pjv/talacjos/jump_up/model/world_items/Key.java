package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

/**
 * The Key class represents a collectible key in the game.
 * It extends the Entity class and includes functionality for animations and unique key identification.
 */
public class Key extends Entity {
    /**
     * The unique identifier for the key.
     */
    private int keyId;

    /**
     * Constructs a Key object with the specified position and identifier.
     *
     * @param x  The x-coordinate of the key's position.
     * @param y  The y-coordinate of the key's position.
     * @param id The unique identifier for the key.
     */
    public Key(double x, double y, int id) {
        super(x, y, Constants.COLLECTABLE_SIZE, Constants.COLLECTABLE_SIZE);
        this.keyId = id;

        loadAnimation();
    }

    /**
     * Returns the default image path for the key.
     *
     * @return The path to the default image as a String.
     */
    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/keys/keyDefault.png").toExternalForm();
    }


    /**
     * Loads the animation frames for the key.
     * The animation consists of frames located in the specified directory.
     */
    private void loadAnimation() {
        Image[] frames = loadFrames("/keys/key", 5);
        currentAnimation = new SpriteAnimation(frames, 1.5, true);
    }

    /**
     * Updates the animation of the key based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    @Override
    public void updateAnimation(double deltaTime) {
        currentAnimation.update(deltaTime);
    }


    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public int getKeyId() {
        return keyId;
    }
}
