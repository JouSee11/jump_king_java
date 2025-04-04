package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SpriteAnimation;
import javafx.scene.image.Image;

public class End extends Entity{

    public End(double x, double y) {
        super(x, y, Constants.END_WIDTH, Constants.END_HEIGHT);
        loadAnimation();
    }

    @Override
    protected String getDefaultImage() {
        return getClass().getResource("/images/map_end/end.png").toExternalForm();
    }

    private void loadAnimation() {
        Image[] frames = loadFrames("/map_end/end", 3);
        currentAnimation = new SpriteAnimation(frames, 1, true);
    }

    @Override
    public void updateAnimation(double deltaTime) {
        currentAnimation.update(deltaTime);
    }

}
