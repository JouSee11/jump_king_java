package cz.cvut.fel.pjv.talacjos.jump_up.model;

import javafx.geometry.Bounds;

import java.util.List;

public class CollisionHandler{

    // First check and handle vertical movement, then horizontal
    public void handleCollisions(Player player, List<Platform> platforms, double floorY, double gameWidth) {
        // Reset ground state before checking
        player.setOnGround(false);

        // Check floor collision (highest priority)
        if (player.getY() + player.getHeight() > floorY) {
            player.setY(floorY - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);
            player.setJumping(false);
        }

        // Handle boundary constraints (edges of screen)
        if (player.getX() < 0) {
            player.setX(0);
            bounceBack(player);
        } else if (player.getX() + player.getWidth() > gameWidth) {
            player.setX(gameWidth - player.getWidth());
            bounceBack(player);
        }


        // Handle platform collisions - first vertical then horizontal
        for (Platform platform : platforms) {
            if (checkCollision(player, platform)) {
                resolveCollision(player, platform);
            }
        }
    }
//

//
    private boolean checkCollision(Player player, Platform platform) {
            Bounds platformBounds = platform.getBoundsInParent();
            Bounds playerBounds = player.getBoundsInParent();

            return playerBounds.intersects(platformBounds);
    }

    private void resolveCollision(Player player, Platform platform) {
        //Calculate horizontal overlap
        double overlapLeft = player.getX() + player.getWidth() - platform.getX();
        double overlapRight = platform.getX() + platform.getWidth() - player.getX();
        double horizontalOverlap = Math.min(overlapLeft, overlapRight);

        //Calculate vertical overlap
        double overlapTop = player.getY() + player.getHeight() - platform.getY();
        double overlapBottom = platform.getY() + platform.getHeight() - player.getY();
        double verticalOverlap = Math.min(overlapTop, overlapBottom);

        //Resolve collision based on smallest overlap
        if (horizontalOverlap < verticalOverlap) {
            if (overlapLeft < overlapRight) {
                player.setX(platform.getX() - player.getWidth()); // player hit the platform from left
            } else {
                player.setX(platform.getX() + platform.getWidth()); // player hit the platform from right
            }
            //if the player is jumping bounce back
            bounceBack(player);
        } else {
            if (overlapTop < overlapBottom) {
                player.setY(platform.getY() - player.getHeight()); // player hit the platform from top (landed on the platform)

                player.setOnGround(true);
                player.setJumping(false);
            } else  {
                player.setY(platform.getY() + platform.getHeight()); // player hit the platform from bottom
            }
            player.setVelocityY(0);
        }

    }

    private void bounceBack(Player player) {
        //if the player is jumping bounce back
        if (!player.isOnGround()) {
            player.setVelocityX(-1 * player.getVelocityX());
        } else {
            player.setVelocityY(0);
        }
    }

}