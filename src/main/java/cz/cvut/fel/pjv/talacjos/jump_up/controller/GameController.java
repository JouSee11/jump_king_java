package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.model.FileSaveRead;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Player;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.FinishDialogView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.GameView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.PauseMenuView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.game.SaveDialogView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class GameController {
    private final SceneController sceneController;
    private GameState gameState;
    private AnimationTimer gameLoop;
    private GameView gameView;
    private PauseMenuView pauseMenuView;

    private SaveDialogView saveDialogView = null;
    private boolean isSaveDialogOpened = false;
    private boolean isLoadedFromSave = false;

    private boolean spacePressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private boolean isPaused = false;

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

    //set up the game scene
    public Scene setGameScene() {
        return gameView.getScene();
    }

    private void setupGameLoop() {
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


    public void handleKeyPress(KeyEvent event) {
        Player player = gameState.getPlayer();

        switch (event.getCode()) {

            case LEFT:
                leftPressed = true;
//                if (player.isOnGround() && !spacePressed) {
//                    gameState.movePlayerX(-1);
//                }
                break;

            case RIGHT:
                rightPressed = true;
//                if (player.isOnGround() && !spacePressed) {
//                    gameState.movePlayerX(1);
//                }
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
                System.out.println(saveDialogView);
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

    public void handleKeyRelease(KeyEvent event) {
        Player player = gameState.getPlayer();

        switch (event.getCode()) {

            case LEFT:
                leftPressed = false;
//                if (!rightPressed && player.isOnGround() && !spacePressed) {
//                    player.setVelocityX(0);
//                }
                break;

            case RIGHT:
                rightPressed = false;
//                if (!leftPressed && player.isOnGround() && !spacePressed) {
//                    player.setVelocityX(0);
//                }
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


    public void startGame() {
        //init sounds

        if (!isLoadedFromSave) { // dont play the starting sound when the game is loaded from save
            SoundController.getInstance().playSound("startingMsg", 1);
        }
        SoundController.getInstance().playMusic("main_sound.wav");
        SoundController.getInstance().setMusicVolume(Constants.DEFAULT_MUSIC_VOLUME);


        setupGameLoop();
        gameLoop.start();
    }

    public void endGame() {
        gameLoop.stop();
        SoundController.getInstance().stopAllSounds();
        sceneController.showMenuScene();
    }

    //pause game handle
    public void togglePause() {

        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void pauseGame() {
        SoundController.getInstance().stopAllSounds();
        if (!isPaused) {
            isPaused = true;
            gameLoop.stop();
            pauseMenuView.show();

        }
    }

    public void resumeGame() {
        SoundController.getInstance().resumeAllSounds();
        if (isPaused) {
            isPaused = false;
            pauseMenuView.hide();
            gameLoop.start();
        }
    }

    public void saveGame(String fileName) {
        String mapName = gameState.getMapName();

        JsonObject saveData = new JsonObject();
        saveData.addProperty("mapName", mapName);
        saveData.addProperty("level", gameState.getCurLevel());
        saveData.addProperty("playerX", gameState.getPlayer().getX());
        saveData.addProperty("playerY", gameState.getPlayer().getY());

        //create new json array and add all the collected keys to the save
        JsonArray collectedKeys = new JsonArray();
        for(Integer keyId : gameState.getCollectedKeysList()) {
            collectedKeys.add(keyId);
        }
        saveData.add("collectedKeys", collectedKeys);

        //convert to json string with pretty printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(saveData);

        //call the function to save the data to the file
        FileSaveRead.saveGameToFile(fileName, jsonString, mapName);

    }

    public boolean isPaused() {
        return isPaused;
    }

    //victory dialog when game ends
    public void showFinish() {
        gameLoop.stop();
        SoundController.getInstance().stopAllSounds();
        SoundController.getInstance().playSound("victory", 1);

        FinishDialogView finishDialogView = new FinishDialogView(this);
        gameView.addFinishDialog(finishDialogView.getRoot());
        finishDialogView.show();
    }

    public void showSaveDialog() {
        saveDialogView = new SaveDialogView(this);
        gameView.addSaveDialog(saveDialogView.getRoot());
        saveDialogView.show();
    }
}
