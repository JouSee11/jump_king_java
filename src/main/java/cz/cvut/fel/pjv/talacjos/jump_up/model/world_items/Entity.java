package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

abstract public class Entity extends Rectangle {
    public Entity(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    protected SpriteAnimation currentAnimation;

    //load animation frames
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

    abstract public void updateAnimation(double deltaTime);
}
