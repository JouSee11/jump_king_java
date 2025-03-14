package cz.cvut.fel.pjv.talacjos.jump_up.model;

public class Player extends Entity{
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean isOnGround = true;
    private int jumpDirection = 0;

    private double jumpPower = 300;
    private double moveSpeed = 250;

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
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
}
