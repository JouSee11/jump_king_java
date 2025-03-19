package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameState {
    GameController gameController;
    private final CollisionHandler collisionHandler = new CollisionHandler(this);

    private List<Platform> curPlatformList;
    private HashMap<Integer, Level> levelsDataList;

    private Player player;
    private double jumpHoldTime = 0;

    private int curLevel;
    private int maxLevel;

    public GameState(GameController gameController) {
        this.gameController = gameController;
        this.curPlatformList = new ArrayList<Platform>();

        //load data from json files
        loadPlayerData();
        loadLevelsData();

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
        collisionHandler.handleCollisions(player, curPlatformList, curLevel, maxLevel);

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

    private void loadLevelsData() {
        levelsDataList = JsonDataLoader.loadLevelsJson("src/main/resources/maps/Map2/levels.json");
    }

    private void loadPlayerData() {
        player = JsonDataLoader.loadPlayerJson("src/main/resources/maps/Map2/player.json");
    }

    public List<Platform> getPlatformList() {
        return curPlatformList;
    }

    //level control
    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
        curPlatformList = levelsDataList.get(curLevel).getPlatforms(); //set the platforms for the current level
//        addPlatformsLevel(curLevel);
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
