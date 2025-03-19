package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.Player;
import cz.cvut.fel.pjv.talacjos.jump_up.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    private final SceneController sceneController;
    private GameState gameState;
    private AnimationTimer gameLoop;
    private GameView gameView;

    private boolean spacePressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GameController(SceneController sceneController) {
        this.sceneController = sceneController;
        // game model
        this.gameState = new GameState(this);
        // game view
        this.gameView = new GameView(sceneController, this, gameState);
        //add the player

        setupGameLoop();
    }

    //setup the game scene
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
        gameLoop.start();
    }

}
