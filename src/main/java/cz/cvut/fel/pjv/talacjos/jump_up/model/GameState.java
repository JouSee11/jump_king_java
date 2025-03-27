package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SoundController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameState {
    GameController gameController;
    private final CollisionHandler collisionHandler = new CollisionHandler(this);

    private List<Platform> curPlatformList;
    private List<Key> curKeyList;
    private List<PowerUp> curPowerupList;
    private End end = null;

    private HashMap<Integer, Level> levelsDataMap;

    private Player player;
    private double jumpHoldTime = 0;

    private boolean actionButtonPressed = false;
    private boolean collisionEnd = false;

    private int curLevel;
    private int maxLevel;

    private int collectedKeys;
    private int allKeys;

    //powerup data
    private boolean powerUpActive = false;
    private int powerUpTimeRemaining = 0;
    private Thread powerUpTimerThread;

    public GameState(GameController gameController) {
        this.gameController = gameController;
        this.curPlatformList = new ArrayList<Platform>();
        this.curKeyList = new ArrayList<Key>();
        this.curPowerupList = new ArrayList<PowerUp>();

        //load data from json files
        loadPlayerData();
        loadLevelsData();
        loadKeysData();

        //set levels data
        setCollectedKeys(4);
        setCurLevel(7);
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
        collisionHandler.handleCollisions();

        // Apply gravity
        player.setVelocityY(Math.min( Constants.TERMINAL_VELOCITY, player.getVelocityY() + Constants.GRAVITY * deltaTime));

        // Control horizontal velocity when on ground -X
        playerRunUpdate();

        // Control space bar press time
        spacerUpdate(deltaTime);

        //update player animation
        player.updateAnimation(deltaTime);

        //update collectable animation
        updateCollectableAnimation(deltaTime, curKeyList);
        updateCollectableAnimation(deltaTime, curPowerupList);
        updateEndAnimation(end, deltaTime);
    }

    private <T extends Entity> void updateCollectableAnimation(double deltaTime, List<T>  entities) {
        for (T entity : entities) {
            entity.updateAnimation(deltaTime);
        }
    }

    private void updateEndAnimation(End end, double deltaTime) {
        if (end != null) {
            end.updateAnimation(deltaTime);
        }
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
                    player.setVelocityX(-Constants.MOVE_SPEED * player.getMoveSpeedMultiplier());
                } else if (gameController.isRightPressed() && !gameController.isLeftPressed()) {
                    player.setVelocityX(Constants.MOVE_SPEED * player.getMoveSpeedMultiplier());
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
        player.setVelocityX(Constants.MOVE_SPEED * player.getMoveSpeedMultiplier() * player.getJumpDirection());
        player.setOnGround(false);
        player.setSquatting(false);
        player.setJumping(true);
        jumpHoldTime = 0;

        SoundController.getInstance().playRandomJump();

    }

    private double calculateJumpVelocity(double jumpHoldTime) {
        double ratio = Math.min(jumpHoldTime / Constants.MAX_JUMP_WAIT, 1.0);
        double velocityRatio = Constants.MIN_JUMP_COEFFICIENT + (1 - Constants.MIN_JUMP_COEFFICIENT) * ratio; //usual formula for calculating jump ratio
        return -Constants.JUMP_POWER * velocityRatio * player.getJumpPowerMultiplier();
    }

    private void loadLevelsData() {
        levelsDataMap = JsonDataLoader.loadLevelsJson("src/main/resources/maps/Map3/levels.json");
    }

    private void loadPlayerData() {
        player = JsonDataLoader.loadPlayerJson("src/main/resources/maps/Map3/player.json");
    }

    private void loadKeysData() {
        int[] keyStatResp = JsonDataLoader.loadKeysStatsJson("src/main/resources/maps/Map3/collectables.json");
        allKeys = keyStatResp[0];
        collectedKeys = keyStatResp[1];
    }

    public int[] getKeyStats() {
        return new int[]{allKeys, collectedKeys};
    }

    public void setCollectedKeys(int collectedKeys) {
        this.collectedKeys = collectedKeys;
    }

    public int getCollectedKeys() {
        return collectedKeys;
    }

    public int getAllKeys() {
        return allKeys;
    }

    public List<Platform> getPlatformList() {
        return curPlatformList;
    }

    public List<Key> getKeyList() {
        return curKeyList;
    }

    public List<PowerUp> getCurPowerupList() {
        return curPowerupList;
    }

    public End getEnd() {
        return end;
    }

    public void setActionButtonPressed(boolean actionButtonPressed) {
        this.actionButtonPressed = actionButtonPressed;
    }

    public boolean isActionButtonPressed() {
        return actionButtonPressed;
    }

    public boolean isCollisionEnd() {
        return collisionEnd;
    }

    public void setCollisionEnd(boolean collisionEnd) {
        this.collisionEnd = collisionEnd;
    }

    //level control
    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
        curPlatformList = levelsDataMap.get(curLevel).getPlatforms(); //set the platforms for the current level
        curKeyList = levelsDataMap.get(curLevel).getKeys();
        curPowerupList = levelsDataMap.get(curLevel).getPowerUps();
        end = levelsDataMap.get(curLevel).getEnd();
        //        addPlatformsLevel(curLevel);
    }

    public void keyCollected(Key key) {
        collectedKeys++;
        curKeyList.remove(key);
        levelsDataMap.get(curLevel).getKeys().remove(key);

        SoundController.getInstance().playSound("collectSuccess", 1);
        if (collectedKeys == allKeys) {
            SoundController.getInstance().playSound("collectedAllKeys", 1);
        } else {
            SoundController.getInstance().playRandomKeyCollect();
        }
    }

    public void powerUpCollected(PowerUp powerUp) {
        //remove the powerup from the map
        curPowerupList.remove(powerUp);
        levelsDataMap.get(curLevel).getPowerUps().remove(powerUp);

        // add the powerup to the player
        powerUpActive = true;
        player.powerUpActivate();

        //start the timer thread
        powerUpTimeRemaining = Constants.POWERUP_DURATION;
        startPowerUpTimerThread();

        SoundController.getInstance().playSound("powerUpCollected", 1);
    }

    private void deactivatePowerUp() {
        powerUpActive = false;
        powerUpTimeRemaining = 0;
        player.powerUpDeactivate();
        stopPowerUpTimerThread();
    }

    private void startPowerUpTimerThread() {
        //stop the thread if it is already running
        stopPowerUpTimerThread();

        powerUpTimerThread = new Thread(() -> {
           try {
               while(powerUpTimeRemaining > 0) {
                   Thread.sleep(1000); //wait 1 second for the timer
                    powerUpTimeRemaining--;
               }
               javafx.application.Platform.runLater(this::deactivatePowerUp);
               System.out.println("Power up timer thread has been started");
           } catch (InterruptedException e) {
           }
        });
        powerUpTimerThread.setDaemon(true);
        powerUpTimerThread.start();

    }

    private void stopPowerUpTimerThread() {
        if (powerUpTimerThread != null && powerUpTimerThread.isAlive()) {
            powerUpTimerThread.interrupt();
        }
    }

    public void setPowerUpActive(boolean powerUpActive) {
        this.powerUpActive = powerUpActive;
    }

    public boolean isPowerUpActive() {
        return powerUpActive;
    }

    public int getPowerUpTimeRemaining() {
        return powerUpTimeRemaining;
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
