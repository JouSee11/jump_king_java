package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Platform class represents a platform in the game world.
 * It extends the Rectangle class to define the platform's position and size
 * and includes functionality for managing platform types and associated images.
 */
public class Platform extends Rectangle {
    /**
     * The type of the platform, which determines its appearance.
     */
    private PlatformTypes type;

    /**
     * The image associated with the platform.
     */
    private Image image;

    /**
     * Constructs a Platform object with the specified position, size, and type.
     *
     * @param x      The x-coordinate of the platform's position.
     * @param y      The y-coordinate of the platform's position.
     * @param width  The width of the platform.
     * @param height The height of the platform.
     * @param type   The type of the platform.
     */
    public Platform(int x, int y, int width, int height, PlatformTypes type) {
        super(x, y, width, height);
        this.type = type;
        loadImage(this.type.getType());
    }


    public void setType(PlatformTypes type) {
        this.type = type;
        loadImage(this.type.getType());
    }

    public PlatformTypes getType() {
        return type;
    }

    public Color getBorderColor() {
        return  type.getColor();
    }

    /**
     * Loads a random image for the platform based on its type.
     *
     * @param type The type of the platform, used to determine the image path.
     */
    private void loadImage(String type) {
        //get random image for the specified type
        int randomImg = (int) (Math.random() * 3) + 1;
        String imageLink = getClass().getResource("/images/platform/" + type + "/" + type  + randomImg + ".png").toExternalForm();
        image = new Image(imageLink);
    }

    public Image getImage() {
        return image;
    }
}
