package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    GameController gameController;
    private final CollisionHandler collisionHandler = new CollisionHandler(this);

    private List<Platform> platformList;
    private Player player;
    private double floorY = Constants.GAME_HEIGHT;
    private double jumpHoldTime = 0;

    private int curLevel;
    private int maxLevel;


    public GameState(GameController gameController) {
        this.gameController = gameController;
        this.platformList = new ArrayList<Platform>();

        //add player and platforms
        addPlayer(new Player(200, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        addPlatforms();

        //set levels data
        setCurLevel(1);
        setMaxLevel(7);
    }

    public void update(double deltaTime) {

        // Apply velocity to position (X and Y separately)
        double newX = player.getX() + player.getVelocityX() * deltaTime;
        //make the jumps longer
        if (player.isJumping()) {
            newX = player.getX() + (player.getVelocityX() * Constants.JUMP_LENGTH_COEFFICIENT) * deltaTime;
        }

        double newY = player.getY() + player.getVelocityY() * deltaTime;

        // Update positions
        player.setX(newX);
        player.setY(newY);

        // Handle all collisions
        collisionHandler.handleCollisions(player, platformList, floorY, curLevel, maxLevel);

        // Apply gravity
        player.setVelocityY(Math.min( Constants.TERMINAL_VELOCITY, player.getVelocityY() + Constants.GRAVITY * deltaTime));

        // Control horizontal velocity when on ground -X
        playerRunUpdate();

        // Control space bar press time
        spacerUpdate(deltaTime);

        //update player animation
        player.updateAnimation(deltaTime);
    }

    private void spacerUpdate(double deltaTime){
        if (gameController.isSpacePressed() && player.isOnGround()) {
            jumpHoldTime += deltaTime;

            //if we have maximum wait time, we execute the jump and "release" the space bar
            if (jumpHoldTime >= Constants.MAX_JUMP_WAIT) {
                // Set jump direction
                player.setJumpDirection(0);
                if (gameController.isLeftPressed()) {
                    player.setJumpDirection(-1);
                } else if (gameController.isRightPressed()) {
                    player.setJumpDirection(1);
                }

                playerJumpExecute();
                jumpHoldTime = 0;
                gameController.setSpacePressed(false);
            }
        } else {
            jumpHoldTime = 0;
        }
    }

    private void playerRunUpdate() {
        if (player.isOnGround()) {
            if (gameController.isSpacePressed()) {
                player.setVelocityX(0);
            } else {
                if (gameController.isLeftPressed() && !gameController.isRightPressed()) {
                    player.setVelocityX(-player.getMoveSpeed());
                } else if (gameController.isRightPressed() && !gameController.isLeftPressed()) {
                    player.setVelocityX(player.getMoveSpeed());
                } else {
                    player.setVelocityX(0);
                }
            }
        }
    }

    //player control
    private void addPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


    //jumping
    public void prepareJump() {
        player.setVelocityX(0);
        player.setSquatting(true);
    }


    public void playerJumpExecute() {
        double jumpVelocity = calculateJumpVelocity(jumpHoldTime);

        // Set vertical velocity for jump (negative means upward)
        player.setVelocityY(jumpVelocity);
        // Apply horizontal velocity based on the aimed jump direction.
        player.setVelocityX(player.getMoveSpeed() * player.getJumpDirection());
        player.setOnGround(false);
        player.setSquatting(false);
        player.setJumping(true);
        jumpHoldTime = 0;
    }

    private double calculateJumpVelocity(double jumpHoldTime) {
        double ratio = Math.min(jumpHoldTime / Constants.MAX_JUMP_WAIT, 1.0);
        double velocityRatio = Constants.MIN_JUMP_COEFFICIENT + (1 - Constants.MIN_JUMP_COEFFICIENT) * ratio; //usual formula for calculating jump ratio
        return -player.getJumpPower() * velocityRatio;
    }


    //platform control
    private void addPlatforms() {
        platformList = new ArrayList<Platform>();
//        addSinglePlatform(0, 400, 300, 400, PlatformTypes.DIRT);
        addSinglePlatform(800, 200, 500, 200, PlatformTypes.STONE);
        addSinglePlatform(500, 100, 500, 100, PlatformTypes.STONE);
//        addSinglePlatform(300, 300, 100, 100, PlatformTypes.STONE);
        addSinglePlatform(700, 600, 100, 50, PlatformTypes.DIRT);
        addSinglePlatform(500, 650, 100, 50, PlatformTypes.DIRT);
        addSinglePlatform(200, 700, 100, 50, PlatformTypes.DIRT);

        System.out.println(platformList);
    }

    private void addSinglePlatform(double x, double y, double width, double height, PlatformTypes type) {
        Platform platform = new Platform(x, Constants.GAME_HEIGHT - y, width, height, type);
        platformList.add(platform);
    }

    public List<Platform> getPlatformList() {
        return platformList;
    }

    //later will we declared in the json map file
    public double getFloorY() {
        return floorY;
    }

    public void setFloorY(double floorY) {
        this.floorY = floorY;
    }

    //level control
    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
