package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import javafx.scene.image.Image;

public class SpriteAnimation {
    private Image[] frames;
    private double duration;
    private double curTime;
    private int currentFrame;
    private boolean loop;
    private boolean playing;

    public SpriteAnimation(Image[] frames, double duration, boolean loop) {
        this.frames = frames;
        this.duration = duration;
        this.loop = loop;
        this.curTime = 0;
        this.currentFrame = 0;
        this.playing = true;
    }

    public void update(double deltaTime) {
        if (!playing) return;

        curTime += deltaTime;
        double frameTime = duration / frames.length; // get how log should be one frame displayed

        //check if we need to update the frame
        if (curTime >= frameTime) {
            currentFrame++;
            curTime = 0;

            //check if we are at the end of the animation
            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    playing = false;
                }
            }
        }
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }

    public void play() {
        playing = true;
    }

    public void reset() {
        currentFrame = 0;
        curTime = 0;
        playing = true;
    }
}
