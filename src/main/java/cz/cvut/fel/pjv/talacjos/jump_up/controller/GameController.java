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
                updateInput();
                gameState.update(deltaTime);
                gameView.render();
            }
        };
    }

    // updateInput applies walking if space is NOT held, or resets horizontal velocity if aiming for a jump.
    private void updateInput() {
        Player player = gameState.getPlayer();
        if (player.isOnGround()) {
            if (!spacePressed) {
                // Walking mode: arrow keys set horizontal velocity.
                if (leftPressed && !rightPressed) {
                    player.setVelocityX(-player.getMoveSpeed());
                } else if (rightPressed && !leftPressed) {
                    player.setVelocityX(player.getMoveSpeed());
                } else {
                    player.setVelocityX(0);
                }
                // When walking, clear any jump aim.
                player.setJumpDirection(0);
            } else {
                // Aiming mode (space held): disable walking movement.
                player.setVelocityX(0);
            }
        }
    }


//    public void handleKeyPress(KeyEvent event) {
//        Player player = gameState.getPlayer();
//
//        switch (event.getCode()) {
//            case LEFT:
//                leftPressed = true;
//                if (!spacePressed && player.isOnGround()) { //if player is on the ground and not space pressed
//                    gameState.movePlayerX(-1);
//                } else if (spacePressed && player.isOnGround()) { // if player is on the ground and space pressed
//                    player.setJumpDirection(-1);
//                }
//                break;
//            case RIGHT:
//                rightPressed = true;
//                if (!spacePressed && player.isOnGround()) {
//                    gameState.movePlayerX(1);
//                } else if (spacePressed && player.isOnGround()) {
//                    player.setJumpDirection(1);
//                }
//                break;
//            case SPACE:
//                if (!spacePressed && player.isOnGround()) {
//                    spacePressed = true;
//                    gameState.prepareJump();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void handleKeyRelease(KeyEvent event) {
//        Player player = gameState.getPlayer();
//
//        switch (event.getCode()) {
//            case LEFT:
//                leftPressed = false;
//                if (player.isOnGround() && !spacePressed) {
//                    // Stop horizontal movement only when on ground and not jumping
//                    player.setVelocityX(0);
//                }
//                break;
//            case RIGHT:
//                rightPressed = false;
//                if (player.isOnGround() && !spacePressed) {
//                    // Stop horizontal movement only when on ground and not jumping
//                    player.setVelocityX(0);
//                }
//                break;
//            case SPACE:
//                if (spacePressed) {
//                    spacePressed = false;
//                    // Execute jump when space is released
//                    gameState.playerJumpExecute();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    public void handleKeyPress(KeyEvent event) {
        Player player = gameState.getPlayer();
        KeyCode code = event.getCode();
        if (player.isOnGround()) {
            if (code == KeyCode.LEFT) {
                leftPressed = true;
                // In aiming mode, update jump direction.
                if (spacePressed) {
                    player.setJumpDirection(-1);
                }
            } else if (code == KeyCode.RIGHT) {
                rightPressed = true;
                if (spacePressed) {
                    player.setJumpDirection(1);
                }
            } else if (code == KeyCode.SPACE) {
                if (!spacePressed && player.isOnGround()) {
                    spacePressed = true;
                    // Execute jump immediately using the current jump direction.
                    gameState.playerJumpExecute();
                }
            }
        }
    }

    public void handleKeyRelease(KeyEvent event) {
        Player player = gameState.getPlayer();
        KeyCode code = event.getCode();
        if (code == KeyCode.LEFT) {
            leftPressed = false;
            if (player.isOnGround() && spacePressed && !rightPressed) {
                player.setJumpDirection(0);
            }
        } else if (code == KeyCode.RIGHT) {
            rightPressed = false;
            if (player.isOnGround() && spacePressed && !leftPressed) {
                player.setJumpDirection(0);
            }
        } else if (code == KeyCode.SPACE) {
            spacePressed = false;
        }
    }

    public void startGame() {
        gameLoop.start();
    }

}
