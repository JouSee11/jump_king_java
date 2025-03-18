package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.view.SpriteAnimation;
import javafx.scene.image.Image;

public class Player extends Entity{
    //player stats
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean isOnGround = true;
    private boolean isJumping = false;
    private boolean isSquatting = false;
    private int jumpDirection = 0;

    //phycics stats - not a constant because it can be changed by powerups
    private double jumpPower = 1500;
    private double moveSpeed = 300;



    //animation properties
    private SpriteAnimation runAnimation;
    private SpriteAnimation idleAnimation;
    private SpriteAnimation jumpAnimation;
    private SpriteAnimation fallAnimation;
    private SpriteAnimation squatAnimation;

    private SpriteAnimation currentAnimation;
    private boolean facingRight = true;

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadAnimations();
    }

    //speed controls
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    //ground controls
    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGround(boolean onGround) {

        isOnGround = onGround;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    //jump controls
    public void setJumpPower(double jumpPower) {
        this.jumpPower = jumpPower;
    }

    public double getJumpPower() {
        return jumpPower;
    }

    public void setJumpDirection(int jumpDirection) {
        this.jumpDirection = jumpDirection;
    }

    public int getJumpDirection() {
        return jumpDirection;
    }

    // if player is in the air because of jump
    public void setJumping(boolean jumping) {
        if (isJumping && !jumping) {
            setVelocityX(0);
        }
        isJumping = jumping;
    }
    public boolean isJumping() {
        return isJumping;
    }

    public void setSquatting(boolean squatting) {
        isSquatting = squatting;
    }

    public boolean isSquatting() {
        return isSquatting;
    }

    //animation controls
    private void loadAnimations() {
        Image[] idleFrames = loadFrames("idle/crazy_frog", 2);
        Image[] runFrames = loadFrames("run/run", 3);
        Image[] jumpFrames = loadFrames("jump/jump", 1);
        Image[] fallFrames = loadFrames("fall/fall", 1);
        Image[] squatFrames = loadFrames("squat/squat", 1);

        idleAnimation = new SpriteAnimation(idleFrames, 1, true);
        runAnimation = new SpriteAnimation(runFrames, 0.4, true);
        jumpAnimation = new SpriteAnimation(jumpFrames, 0.3, true);
        fallAnimation = new SpriteAnimation(fallFrames, 0.3, true);
        squatAnimation = new SpriteAnimation(squatFrames, 0.3, true);

        currentAnimation = idleAnimation;
    }

    private Image[] loadFrames(String path, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            System.out.println("here" + i);
            System.out.println("/images/player/" + path + (i+1) + ".png");
            //load image and check if it was loaded
            String imageLink = getClass().getResource("/images/player/" + path + (i+1) + ".png").toExternalForm();

            if (imageLink == null) {
                System.out.println("Image not found: " + path + i + ".png");
                frames[i] = new Image(getDefaultImage());
            } else {
                frames[i] = new Image(imageLink);;
            }
        }
        return frames;
    }

    private String getDefaultImage() {
        return getClass().getResource("/images/player/playerDefault.png").toExternalForm();
    }

    public void updateAnimation(double deltaTime) {
        // Update animation based on player state
        if (!isOnGround()) {
            if (velocityY < 0) {
                currentAnimation = jumpAnimation;
            } else {
                currentAnimation = fallAnimation;
            }
        } else if (Math.abs(velocityX) > 0) {
            currentAnimation = runAnimation;
        } else if (isSquatting) {
            currentAnimation = squatAnimation;
        }
        else {
            currentAnimation = idleAnimation;
        }

        // Update facing direction
        if (velocityX > 0) {
            facingRight = true;
        } else if (velocityX < 0) {
            facingRight = false;
        }

        currentAnimation.update(deltaTime);
    }

    public SpriteAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    public boolean isFacingRight() {
        return facingRight;
    }


}

