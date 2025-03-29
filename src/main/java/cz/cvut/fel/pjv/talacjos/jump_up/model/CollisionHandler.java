package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SoundController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CollisionHandler{
    private GameState gameState;

    private boolean wasOnGround  = false;

    public CollisionHandler(GameState gameState) {
        this.gameState = gameState;
    }

    // First check and handle vertical movement, then horizontal
    public void handleCollisions() {
        //get the data from the game state that are relevant for collisions handling
        Player player = gameState.getPlayer();
        int curLevel = gameState.getCurLevel();
        int maxLevel = gameState.getMaxLevel();

        // Reset ground state before checking
        wasOnGround = player.isOnGround();
        player.setOnGround(false);
        gameState.setCollisionEnd(false);

        // Check player on the floor on level 1
        if (player.getY() + player.getHeight() > Constants.GAME_HEIGHT && curLevel == 1) {
            player.setY(Constants.GAME_HEIGHT - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);
            player.setBounced(false);

            playSoundFall(player);
            player.setJumping(false);
        }

        // check if the player is colliding with the "celling" and is on the top level
        if (player.getY() < 0 && curLevel == maxLevel) {
            player.setY(0);
            player.setVelocityY(0);

            SoundController.getInstance().playSound("bump", 0.6);

        }

        // check next level and move player to the next level - DOWN
        if (player.getY() > Constants.GAME_HEIGHT && curLevel > 1) {
            changeLevel(player, curLevel - 1, "down");
        }
        // check next level and move player to the next level - UP
        if (player.getY() + player.getHeight() < 0 && curLevel < maxLevel) {
            changeLevel(player, curLevel + 1, "up");
        }

        // Handle boundary constraints (edges of screen)
        if (player.getX() < 0) {
            player.setX(0);
            bounceBack(player);
        } else if (player.getX() + player.getWidth() > Constants.GAME_WIDTH) {
            player.setX(Constants.GAME_WIDTH - player.getWidth());
            bounceBack(player);
        }

        // Handle platform collisions - first vertical then horizontal
        for (Platform platform : gameState.getPlatformList()) {
            if (checkCollision(player, platform)) {
                resolveCollisionPlatform(player, platform);
            }
        }

        //handle key collect collision
        List<Key> keyToCollect = new ArrayList<Key>();
        for (Key key : gameState.getKeyList()) {
            if(checkCollision(player, key)) {
                keyToCollect.add(key);
            }
        }
        for (Key key : keyToCollect) {
            gameState.keyCollected(key);
        }

        //handle powerup collect collision
        List<PowerUp> powerUpsToCollect = new ArrayList<PowerUp>();
        for (PowerUp powerUp : gameState.getCurPowerupList()) {
            if(checkCollision(player, powerUp)) {
                powerUpsToCollect.add(powerUp);
            }
        }
        for (PowerUp powerUp : powerUpsToCollect) {
            gameState.powerUpCollected(powerUp);
        }

        //handle end collision
        checkEndCollision(player);
    }
//

//
    private boolean checkCollision(Player player, Rectangle mapObject) {
            Bounds platformBounds = mapObject.getBoundsInParent();
            Bounds playerBounds = player.getBoundsInParent();

            return playerBounds.intersects(platformBounds);
    }

    private void resolveCollisionPlatform(Player player, Rectangle platform) {
        //Calculate horizontal overlap
        double overlapLeft = player.getX() + player.getWidth() - platform.getX();
        double overlapRight = platform.getX() + platform.getWidth() - player.getX();
        double horizontalOverlap = Math.min(overlapLeft, overlapRight);

        //Calculate vertical overlap
        double overlapTop = player.getY() + player.getHeight() - platform.getY();
        double overlapBottom = platform.getY() + platform.getHeight() - player.getY();
        double verticalOverlap = Math.min(overlapTop, overlapBottom);

        //Resolve collision based on smallest overlap
        if (verticalOverlap < horizontalOverlap) {
            if (overlapTop < overlapBottom) {
                player.setY(platform.getY() - player.getHeight()); // player hit the platform from top (landed on the platform)
                player.setOnGround(true);
                player.setBounced(false);

                playSoundFall(player);

                player.setJumping(false);
            } else  {
                player.setY(platform.getY() + platform.getHeight()); // player hit the platform from bottom

                SoundController.getInstance().playSound("bump", 0.6);

            }
            player.setVelocityY(0);

        } else {
            if (overlapLeft < overlapRight) {
                player.setX(platform.getX() - player.getWidth()); // player hit the platform from left
            } else {
                player.setX(platform.getX() + platform.getWidth()); // player hit the platform from right
            }

            bounceBack(player);
        }

    }

    private void checkEndCollision(Player player) {
        End end = gameState.getEnd();
        //check if we are at the level where there is the end
        if (end == null) {return;}

        //check if player collides with the end element
        if (checkCollision(player, end)) {
            gameState.setCollisionEnd(true);
            //check if the player has all keys
            if (gameState.getCollectedKeys() >= gameState.getAllKeys() && gameState.isActionButtonPressed()) {
                gameState.endGame();
            } else if (gameState.isActionButtonPressed()) {
                SoundController.getInstance().playSound("denied", 1);
            } else {
                resolveCollisionPlatform(player, end);
            }
        }

    }


    private void bounceBack(Player player) {
        //if the player is jumping bounce back
        if (!player.isOnGround()) {
            player.setVelocityX(-Constants.BOUNCE_COEFFICIENT * player.getVelocityX());
            player.setBounced(true);

            //play sound
            if (player.getVelocityX() != 0) {
                SoundController.getInstance().playSound("bump", 0.6);
            }
        } else {
            player.setVelocityY(0);
        }
    }

    private void changeLevel(Player player, int level, String direction) {
        gameState.setCurLevel(level);

        if (direction.equals("down")) {
            player.setY(0);
        } else {
            player.setY(Constants.GAME_HEIGHT - player.getHeight());
        }

        System.out.println("Player 'y' velocity:" + player.getVelocityY());

        System.out.println("Next level " + direction);
    }

    private void playSoundFall(Player player) {
        if (player.isJumping() || !wasOnGround) {
            SoundController.getInstance().playSound("fall", 1);
        }
    }

}