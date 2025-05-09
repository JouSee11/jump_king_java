package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import cz.cvut.fel.pjv.talacjos.jump_up.model.FileSaveRead;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.JsonDataLoader;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Player;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.FinishDialogView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.GameView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.PauseMenuView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SaveDialogView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 * The GameController class is responsible for managing the game logic, handling user input,
 * and coordinating interactions between the game model, view, and other components.
 */
public class GameController {
    private final SceneController sceneController;
    private final GameState gameState;
    private AnimationTimer gameLoop;
    private final GameView gameView;
    private final PauseMenuView pauseMenuView;

    private SaveDialogView saveDialogView = null;
    private final boolean isLoadedFromSave;

    private boolean spacePressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    protected boolean isPaused = false;


    /**
     * Constructs a GameController instance.
     *
     * @param sceneController The SceneController instance for managing scenes.
     * @param fileName The name of the file to load game data from.
     * @param loadedFromSave Indicates whether the game is loaded from a save file.
     */
    public GameController(SceneController sceneController, String fileName, Boolean loadedFromSave) {
        this.sceneController = sceneController;
        // game model
        this.gameState = new GameState(this, fileName, loadedFromSave);
        // game view
        this.gameView = new GameView(sceneController, this, gameState);
        //pause menu
        this.pauseMenuView = new PauseMenuView(this);
        gameView.addPauseMenu(pauseMenuView.getRoot());

        isLoadedFromSave = loadedFromSave;


    }

    /**
     * Sets up the game scene.
     *
     * @return The Scene object representing the game scene.
     */
    public Scene setGameScene() {
        return gameView.getScene();
    }

    /**
     * Initializes the game loop, which updates the game state and renders the view.
     */
    protected void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long currentNanoTime) {
                if (lastUpdate == 0) {
                    lastUpdate = currentNanoTime;
                    return;
                }

                // Calculate time since last update in seconds
                double deltaTime = (currentNanoTime - lastUpdate) / 1_000_000_000.0;

                deltaTime = Math.min(deltaTime, 0.017); // max 60 FPS

                lastUpdate = currentNanoTime;

                // Update game state and render
                gameState.update(deltaTime);
                gameView.render();
            }
        };
    }

    /**
     * Handles key press events and updates the game state accordingly.
     *
     * @param event The KeyEvent representing the key press.
     */
    public void handleKeyPress(KeyEvent event) {
        Player player = gameState.getPlayer();

        switch (event.getCode()) {

            case LEFT:
                leftPressed = true;
                break;

            case RIGHT:
                rightPressed = true;
                break;

            case SPACE:
                spacePressed = true;
                if (player.isOnGround()) {
                    gameState.prepareJump();
                }
                break;
            //for testing purposes - skip levels
            case N:
                int currentLevel = gameState.getCurLevel();
                int maxLevel = gameState.getMaxLevel();
                if (currentLevel < maxLevel) {
                    gameState.setCurLevel(currentLevel+1);
                }
                break;
            case E:
                gameState.setActionButtonPressed(true);
                break;
            case ESCAPE:
                if (saveDialogView != null) {
                    saveDialogView.hide();
                    saveDialogView = null;
                } else {
                    togglePause();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handles key release events and updates the game state accordingly.
     *
     * @param event The KeyEvent representing the key release.
     */
    public void handleKeyRelease(KeyEvent event) {
        Player player = gameState.getPlayer();

        switch (event.getCode()) {

            case LEFT:
                leftPressed = false;

                break;

            case RIGHT:
                rightPressed = false;

                break;
            case SPACE:
                spacePressed = false;
                if (player.isOnGround()) {
                    //set the direction of the jump
                    player.setJumpDirection(0);
                    if (leftPressed) {
                        player.setJumpDirection(-1);
                    } else if (rightPressed) {
                        player.setJumpDirection(1);
                    }
                    gameState.playerJumpExecute();
                }
                break;
            case E:
                gameState.setActionButtonPressed(false);
            default:
                break;
        }
    }


    public boolean isLeftPressed() {
        return leftPressed;
    }


    public boolean isRightPressed() {
        return rightPressed;
    }


    public boolean isSpacePressed() {
        return spacePressed;
    }


    public void setSpacePressed(boolean spacePressed) {
        this.spacePressed = spacePressed;
    }


    /**
     * Starts the game by initializing the game loop and playing sounds.
     */
    public void startGame() {
        //init sounds

        if (!isLoadedFromSave) { // don't play the starting sound when the game is loaded from save
            SoundController.getInstance().playSound("startingMsg", 1);
        }
        if (!gameState.isPowerUpActive()) { // if power up is active the fast music is playing
            SoundController.getInstance().playMusic("main_sound.wav");
        }
        SoundController.getInstance().setMusicVolume(Constants.DEFAULT_MUSIC_VOLUME);


        setupGameLoop();
        gameLoop.start();
    }

    /**
     * Ends the game by stopping the game loop and returning to the menu scene.
     */
    public void endGame() {
        GameLogger.getInstance().info("Game ended");

        gameLoop.stop();
        SoundController.getInstance().stopAllSounds();
        sceneController.showMenuScene();
    }

    /**
     * Ends the game by stopping the game loop and returning to the menu scene - called when there is some error in the game
     */
    public void endGameError() {
        GameLogger.getInstance().warning("Loading game ended with error");

        gameLoop.stop();
        SoundController.getInstance().stopAllSounds();
        sceneController.showMenuSceneWithError();
    }


    /**
     * Toggles the pause state of the game.
     */
    public void togglePause() {

        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }


    /**
     * Pauses the game by stopping the game loop and showing the pause menu.
     */
    public void pauseGame() {
        SoundController.getInstance().stopAllSounds();
        if (!isPaused) {
            isPaused = true;
            gameLoop.stop();
            pauseMenuView.show();

        }
    }

    /**
     * Resumes the game by restarting the game loop and hiding the pause menu.
     */
    public void resumeGame() {
        SoundController.getInstance().resumeAllSounds();
        if (isPaused) {
            isPaused = false;
            pauseMenuView.hide();
            gameLoop.start();
        }
    }

    /**
     * Saves the current game state to a file.
     *
     * @param fileName The name of the file to save the game state to.
     */
    public void saveGame(String fileName) {
        String mapName = gameState.getMapName();

        JsonObject saveData = getJsonObject(mapName);

        //create new json array and add all the collected keys to the save
        JsonArray collectedKeys = JsonDataLoader.createJsonArrayFromIntList(gameState.getCollectedKeysList());
        saveData.add("collectedKeys", collectedKeys);

        //create list form collected power ups
        JsonArray collectedPowerUps = JsonDataLoader.createJsonArrayFromIntList(gameState.getCollectedPowerUps());
        saveData.add("collectedPowerUps", collectedPowerUps);


        //create list of json array
        //convert to json string with pretty printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(saveData);

        //call the function to save the data to the file
        FileSaveRead.saveGameToFile(fileName, jsonString, mapName);

    }

    private JsonObject getJsonObject(String mapName) {
        JsonObject saveData = new JsonObject();
        saveData.addProperty("mapName", mapName);
        saveData.addProperty("level", gameState.getCurLevel());
        saveData.addProperty("playerX", gameState.getPlayer().getX());
        saveData.addProperty("playerY", gameState.getPlayer().getY());
        saveData.addProperty("playerVelocityX", gameState.getPlayer().getVelocityX());
        saveData.addProperty("playerVelocityY", gameState.getPlayer().getVelocityY());
        saveData.addProperty("powerUpActive", gameState.isPowerUpActive());
        saveData.addProperty("powerUpTimeRemaining", gameState.getPowerUpTimeRemaining());
        return saveData;
    }


    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Displays the victory dialog when the game ends.
     */
    public void showFinish() {
        gameLoop.stop();
        SoundController.getInstance().stopAllSounds();
        SoundController.getInstance().playSound("victory", 1);

        FinishDialogView finishDialogView = new FinishDialogView(this);
        gameView.addFinishDialog(finishDialogView.getRoot());
        finishDialogView.show();
    }

    /**
     * Displays the save dialog for saving the game.
     */
    public void showSaveDialog() {
        if (saveDialogView != null) {
            saveDialogView.show();
        } else {
            saveDialogView = new SaveDialogView(this);
            gameView.addSaveDialog(saveDialogView.getRoot());
            saveDialogView.show();
        }
    }

    /**
     * Closes the save dialog if it is open.
     */
    public void closeSaveDialog() {
        if (saveDialogView != null) {
            saveDialogView.hide();
        }
    }
}
