package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SoundController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The GameState class represents the current state of the game, including player data,
 * level data, collected items, and power-up states. It handles game updates, level transitions,
 * and interactions between the player and game objects.
 */
public class GameState {
    GameController gameController;
    private String mapName;
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

    private List<Integer> collectedKeys;
    private int allKeys;

    private List<Integer> collectedPowerUps;

    //powerup data
    private boolean powerUpActive = false;
    private int powerUpTimeRemaining = 0;
    private Thread powerUpTimerThread;

    /**
     * Constructs a GameState object with the specified game controller, map name, and save state.
     *
     * @param gameController   The game controller managing the game.
     * @param mapName          The name of the map to load.
     * @param isLoadedFromSave Indicates whether the game is loaded from a save file.
     */
    public GameState(GameController gameController, String mapName, Boolean isLoadedFromSave) {
        this.gameController = gameController;
        this.curPlatformList = new ArrayList<>();
        this.curKeyList = new ArrayList<>();
        this.curPowerupList = new ArrayList<>();

        //set levels data
        try {
            if (!isLoadedFromSave) {
                if (!loadLevelsData(mapName)) {gameController.endGame();}
            } else {
                if(!loadSavedData(mapName)) {gameController.endGame();}
            }

        } catch (Exception e) {
            gameController.endGameError();
        }
    }

    /**
     * Updates the game state, including player movement, collisions, animations, and physics.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
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

    /**
     * Updates the animations for a list of collectable entities.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     * @param entities  The list of entities to update.
     * @param <T>       The type of the entities, extending Entity.
     */
    private <T extends Entity> void updateCollectableAnimation(double deltaTime, List<T>  entities) {
        for (T entity : entities) {
            entity.updateAnimation(deltaTime);
        }
    }

    /**
     * Updates the animation for the end object, if it exists.
     *
     * @param end       The end object to update.
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    private void updateEndAnimation(End end, double deltaTime) {
        if (end != null) {
            end.updateAnimation(deltaTime);
        }
    }

    /**
     * Updates the jump hold time and executes the jump if the maximum wait time is reached.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
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

    /**
     * Updates the player's horizontal velocity based on user input and ground state.
     */
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


    /**
     * Prepares the player for a jump by setting the horizontal velocity to zero
     * and enabling the squatting state.
     */
    public void prepareJump() {
        player.setVelocityX(0);
        player.setSquatting(true);
    }

    /**
     * Executes the player's jump by calculating the jump velocity based on the
     * jump hold time, setting the vertical and horizontal velocities, and updating
     * the player's state to reflect the jump.
     */
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
        GameLogger.getInstance().fine("Player jumped");

    }

    /**
     * Calculates the vertical jump velocity based on the jump hold time.
     * The velocity is determined using a ratio that considers the minimum jump
     * coefficient and the player's jump power multiplier.
     *
     * @param jumpHoldTime The time the jump button has been held down.
     * @return The calculated vertical jump velocity.
     */
    private double calculateJumpVelocity(double jumpHoldTime) {
        double ratio = Math.min(jumpHoldTime / Constants.MAX_JUMP_WAIT, 1.0);
        double velocityRatio = Constants.MIN_JUMP_COEFFICIENT + (1 - Constants.MIN_JUMP_COEFFICIENT) * ratio; //usual formula for calculating jump ratio
        return -Constants.JUMP_POWER * velocityRatio * player.getJumpPowerMultiplier();
    }

    /**
     * Loads level data from a JSON file for the specified map name.
     * This method initializes the levels, player data, key statistics, and other game-related data.
     * It also removes any collectables (keys and power-ups) that have already been collected.
     *
     * @param mapName The name of the map to load data for.
     */
    private boolean loadLevelsData(String mapName) {
        GameLogger.getInstance().info("Game started");

        //try if the save file is correctly created
        try {
            String filePath = "maps/" + mapName + "/map_data.json";
            //platform and level data
            levelsDataMap = JsonDataLoader.loadLevelsJson(filePath);
            //player start data
            player = JsonDataLoader.loadPlayerJson(filePath);
            //keys data stats
            List<Integer> keyStats = JsonDataLoader.loadKeysStatsJson(filePath);
            assert keyStats != null;
            allKeys = keyStats.getFirst();
            collectedKeys = keyStats.subList(1, keyStats.size());
            collectedPowerUps = new ArrayList<>();
            //get levels data - set the starting level and maximum level
            int[] levelsData = JsonDataLoader.loadLevelStatsJson(filePath);

            assert levelsData != null;
            setMaxLevel(levelsData[0]);
            setCurLevel(levelsData[1]);

            removeCollectablesInit(collectedKeys, collectedPowerUps);
            this.mapName = mapName;

            return true;
        } catch (NullPointerException e) {
            gameController.endGameError();
            return false;
        }


    }

    /**
     * Loads saved game data from a JSON file for the specified map name.
     * This method restores the player's position, collected items, and power-up states.
     *
     * @param mapName The name of the map to load saved data for.
     */
    private boolean loadSavedData(String mapName) {
        String filePath = "saves/" + mapName;

        try{
            //get the map name and load all the thing for it
            String loadedMapName = JsonDataLoader.loadMapNameFromSave(filePath);
            GameLogger.getInstance().info("Loaded game: " + loadedMapName );
            loadLevelsData(loadedMapName);

            // load the players position saved
            HashMap<String, Double> playerDataLoaded = JsonDataLoader.loadPlayerDataFromSave(filePath);
            setCurLevel(playerDataLoaded.get("curLevel").intValue());
            player.setX(playerDataLoaded.get("playerX"));
            player.setY(playerDataLoaded.get("playerY"));
            player.setVelocityX(playerDataLoaded.get("velocityX"));
            player.setVelocityY(playerDataLoaded.get("velocityY"));

            //set up the powerup when it was active
            if (playerDataLoaded.get("isPowerUpActive") == 1.0) {
                savePowerUpLoad(playerDataLoaded.get("powerUpTimeRemaining").intValue());
            }

            //load the player collected keys
            collectedKeys = JsonDataLoader.loadCollectedKeysFromSave(filePath);
            collectedPowerUps = JsonDataLoader.loadCollectedPowerUpsFromSave(filePath);
            removeCollectablesInit(collectedKeys, collectedPowerUps);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    /**
     * Restores the power-up state and starts the timer for the remaining duration.
     *
     * @param timeRemaining The remaining time for the power-up, in seconds.
     */
    private void savePowerUpLoad(int timeRemaining) {
        setPowerUpActive(true);
        powerUpTimeRemaining = timeRemaining;
        startPowerUpTimerThread();
        player.powerUpActivate();
        playPowerUpMusic();
    }

    public int[] getKeyStats() {
        return new int[]{allKeys, collectedKeys.size()};
    }


    public int getCollectedKeys() {
        return collectedKeys.size();
    }

    public List<Integer> getCollectedKeysList() {
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

    public List<Integer> getCollectedPowerUps() {return collectedPowerUps;}

    /**
     * Sets the current level and updates the game state with the platforms, keys, power-ups,
     * and end object associated with the specified level.
     *
     * @param curLevel The level number to set as the current level.
     */
    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
        curPlatformList = levelsDataMap.get(curLevel).getPlatforms(); //set the platforms for the current level
        curKeyList = levelsDataMap.get(curLevel).getKeys();
        curPowerupList = levelsDataMap.get(curLevel).getPowerUps();
        end = levelsDataMap.get(curLevel).getEnd();
        //        addPlatformsLevel(curLevel);

    }

    /**
     * Handles the collection of a key by the player. The key is removed from the current level,
     * added to the list of collected keys, and appropriate sounds are played.
     *
     * @param key The key object that has been collected.
     */
    public void keyCollected(Key key) {
        GameLogger.getInstance().info("Key collected");

        collectedKeys.add(key.getKeyId());
        curKeyList.remove(key);
        levelsDataMap.get(curLevel).getKeys().remove(key);

        SoundController.getInstance().playSound("collectSuccess", 1);
        if (collectedKeys.size() == allKeys) {
            SoundController.getInstance().playSound("collectedAllKeys", 1);
            GameLogger.getInstance().info("All keys collected");
        } else {
            SoundController.getInstance().playRandomKeyCollect();
        }
    }

    /**
     * Removes already collected keys and power-ups from the levels during initialization.
     *
     * @param keysIds    A list of IDs of keys that have already been collected.
     * @param powerUpsIds A list of IDs of power-ups that have already been collected.
     */
    private void removeCollectablesInit(List<Integer> keysIds, List<Integer> powerUpsIds) {
        //get key by key id
        for (Level level : levelsDataMap.values()) {
            List<Key> levelKeys = level.getKeys();
            List<PowerUp> levelPowerUps = level.getPowerUps();
            levelKeys.removeIf(key -> keysIds.contains(key.getKeyId()));
            levelPowerUps.removeIf(powerUp -> powerUpsIds.contains(powerUp.getPowerUpId()));
        }

    }

    /**
     * Handles the collection of a power-up by the player. The power-up is removed from the map,
     * added to the list of collected power-ups, and activates the power-up state for the player.
     *
     * @param powerUp The power-up object that has been collected.
     */
    public void powerUpCollected(PowerUp powerUp) {
        //remove the powerup from the map
        collectedPowerUps.add(powerUp.getPowerUpId());
        GameLogger.getInstance().info("Power up collected");
        curPowerupList.remove(powerUp);
        levelsDataMap.get(curLevel).getPowerUps().remove(powerUp);

        // add the powerup to the player
        powerUpActive = true;
        player.powerUpActivate();

        //start the timer thread
        powerUpTimeRemaining = Constants.POWERUP_DURATION;
        startPowerUpTimerThread();

        playPowerUpMusic();
    }

    /**
     * Plays the music associated with collecting a power-up.
     */
    public void playPowerUpMusic() {
        SoundController.getInstance().playSound("powerUpCollected", 1);
        SoundController.getInstance().playMusic("fast_music.wav");
    }

    /**
     * Deactivates the currently active power-up, stops the timer, and restores the default game state.
     */
    private void deactivatePowerUp() {
        powerUpActive = false;
        powerUpTimeRemaining = 0;
        player.powerUpDeactivate();
        stopPowerUpTimerThread();

        SoundController.getInstance().playMusic("main_sound.wav");

        GameLogger.getInstance().info("Powerup ended");
    }

    /**
     * Starts a timer thread to manage the duration of the active power-up.
     * The timer decreases the remaining time every second and deactivates the power-up when time runs out.
     */
    private void startPowerUpTimerThread() {
        //stop the thread if it is already running
        stopPowerUpTimerThread();

        powerUpTimerThread = new Thread(() -> {
           try {
               while(powerUpTimeRemaining > 0) {
                   //stop the timer if the game is paused
                   Thread.sleep(1000); //wait 1 second for the timer
                   if (!gameController.isPaused()) {
                       powerUpTimeRemaining--;
                   }
               }
               javafx.application.Platform.runLater(this::deactivatePowerUp);
           } catch (InterruptedException e) {
               System.out.println(e.getMessage());
           }
        });
        powerUpTimerThread.setDaemon(true);
        powerUpTimerThread.start();

    }

    /**
     * Stops the power-up timer thread if it is currently running.
     */
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

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    /**
     * Ends the game and displays the finish screen.
     */
    public void endGame() {
        gameController.showFinish();
    }
}
