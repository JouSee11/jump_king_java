package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * The Entity class serves as an abstract base class for all game entities.
 * It extends the Rectangle class to represent the entity's position and size
 * and provides functionality for animations and default image handling.
 */
abstract public class Entity extends Rectangle {
    /**
     * The current animation associated with the entity.
     */
    protected SpriteAnimation currentAnimation;

    /**
     * Constructs an Entity object with the specified position and size.
     *
     * @param x      The x-coordinate of the entity.
     * @param y      The y-coordinate of the entity.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(double x, double y, double width, double height) {
        super(x, y, width, height);
    }


    /**
     * Loads animation frames from the specified path.
     *
     * @param path The path to the directory containing the animation frames.
     * @param frameCount The number of frames to load.
     * @return An array of Image objects representing the animation frames.
     */
    protected Image[] loadFrames(String path, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            //load image and check if it was loaded
            String imageLink = getClass().getResource("/images" + path + (i+1) + ".png").toExternalForm();

            if (imageLink == null) {
                System.out.println("Image not found: " + path + i + ".png");
                frames[i] = new Image(getDefaultImage());
            } else {
                frames[i] = new Image(imageLink);;
            }
        }
        return frames;
    }

    public SpriteAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    abstract protected String getDefaultImage();

    /**
     * Updates the animation of the entity based on the elapsed time.
     * This method must be implemented by subclasses.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    abstract public void updateAnimation(double deltaTime);
}
