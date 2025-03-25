package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.SpriteAnimation;
import javafx.scene.image.Image;

public class PowerUp extends Entity {
    private int powerUpId;

    public PowerUp(double x, double y, int id) {
        super(x, y, Constants.COLLECTABLE_SIZE, Constants.COLLECTABLE_SIZE);
        this.powerUpId = id;

        loadAnimation();
    }

    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/powerup/powerupDefault.png").toExternalForm();
    }

    private void loadAnimation() {
        Image[] frames = loadFrames("/powerup/powerup", 3);
        currentAnimation = new SpriteAnimation(frames, 1.5, true);
    }

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
