package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

public class Key extends Entity {
    private int keyId;

    public Key(double x, double y, int id) {
        super(x, y, Constants.COLLECTABLE_SIZE, Constants.COLLECTABLE_SIZE);
        this.keyId = id;

        loadAnimation();
    }

    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/keys/keyDefault.png").toExternalForm();
    }

    private void loadAnimation() {
        Image[] frames = loadFrames("/keys/key", 5);
        currentAnimation = new SpriteAnimation(frames, 1.5, true);
    }

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
