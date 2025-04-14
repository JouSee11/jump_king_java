package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

/**
 * The End class represents the end point of a level in the game.
 * It extends the Entity class and includes animation functionality.
 */
public class End extends Entity{

    /**
     * Constructs an End object with the specified position.
     *
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    public End(double x, double y) {
        super(x, y, Constants.END_WIDTH, Constants.END_HEIGHT);
        loadAnimation();
    }


    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/map_end/end.png").toExternalForm();
    }

    /**
     * Loads the animation frames for the end point.
     * The animation consists of frames located in the specified directory.
     */
    private void loadAnimation() {
        Image[] frames = loadFrames("/map_end/end", 3);
        currentAnimation = new SpriteAnimation(frames, 1, true);
    }

    /**
     * Updates the animation of the end point based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    @Override
    public void updateAnimation(double deltaTime) {
        currentAnimation.update(deltaTime);
    }

}
