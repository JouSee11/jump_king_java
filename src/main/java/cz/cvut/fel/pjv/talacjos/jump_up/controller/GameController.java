package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Player;
import cz.cvut.fel.pjv.talacjos.jump_up.view.FinishDialogView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.GameView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.PauseMenuView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class GameController {
    private final SceneController sceneController;
    private GameState gameState;
    private AnimationTimer gameLoop;
    private GameView gameView;
    private PauseMenuView pauseMenuView;

    private boolean spacePressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private boolean isPaused = false;

    public GameController(SceneController sceneController, String mapName) {
        this.sceneController = sceneController;
        // game model
        this.gameState = new GameState(this, mapName);
        // game view
        this.gameView = new GameView(sceneController, this, gameState);
        //pause menu
        this.pauseMenuView = new PauseMenuView(this);
        gameView.addPauseMenu(pauseMenuView.getRoot());


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
                togglePause();
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
        SoundController.getInstance().playSound("startingMsg", 1);
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
}
