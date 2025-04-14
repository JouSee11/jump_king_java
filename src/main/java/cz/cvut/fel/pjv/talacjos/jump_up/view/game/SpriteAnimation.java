package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import javafx.scene.image.Image;

/**
 * Represents a sprite animation, which cycles through a series of frames over a specified duration.
 */
public class SpriteAnimation {
    private Image[] frames;
    private double duration;
    private double curTime;
    private int currentFrame;
    private boolean loop;
    private boolean playing;

    /**
     * Constructs a new SpriteAnimation.
     *
     * @param frames   The array of frames (images) for the animation.
     * @param duration The total duration of the animation in seconds.
     * @param loop     Whether the animation should loop.
     */
    public SpriteAnimation(Image[] frames, double duration, boolean loop) {
        this.frames = frames;
        this.duration = duration;
        this.loop = loop;
        this.curTime = 0;
        this.currentFrame = 0;
        this.playing = true;
    }

    /**
     * Updates the animation based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
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

    /**
     * Starts or resumes the animation.
     */
    public void play() {
        playing = true;
    }

    /**
     * Resets the animation to the first frame and starts playing.
     */
    public void reset() {
        currentFrame = 0;
        curTime = 0;
        playing = true;
    }
}
