package cz.cvut.fel.pjv.talacjos.jump_up.model;

import java.util.List;

public class CollisionHandler {

    // First check and handle vertical movement, then horizontal
    public void handleCollisions(Player player, List<Platform> platforms, double floorY, double gameWidth) {
        // Reset ground state before checking
        player.setOnGround(false);

        // Check floor collision (highest priority)
        if (player.getY() + player.getHeight() > floorY) {
            player.setY(floorY - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);
        }

        // Handle platform collisions - first vertical then horizontal
        for (Platform platform : platforms) {
            if (checkCollision(player, platform)) {
                resolveCollision(player, platform);
            }
        }

        // Handle boundary constraints (edges of screen)
        if (player.getX() < 0) {
            player.setX(0);
            player.setVelocityX(0);
        } else if (player.getX() + player.getWidth() > gameWidth) {
            player.setX(gameWidth - player.getWidth());
            player.setVelocityX(0);
        }
    }

    private boolean checkCollision(Player player, Platform platform) {
        return player.getX() < platform.getX() + platform.getWidth() &&
                player.getX() + player.getWidth() > platform.getX() &&
                player.getY() < platform.getY() + platform.getHeight() &&
                player.getY() + player.getHeight() > platform.getY();
    }

    private void resolveCollision(Player player, Platform platform) {
        // Calculate overlaps
        double overlapLeft = player.getX() + player.getWidth() - platform.getX();
        double overlapRight = platform.getX() + platform.getWidth() - player.getX();
        double overlapTop = player.getY() + player.getHeight() - platform.getY();
        double overlapBottom = platform.getY() + platform.getHeight() - player.getY();

        // Find the minimum overlap to determine collision side
        double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom));

        // Resolve based on collision side
        if (minOverlap == overlapTop && player.getVelocityY() >= 0) {
            // Landing on top of platform
            player.setY(platform.getY() - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);
        } else if (minOverlap == overlapBottom && player.getVelocityY() <= 0) {
            // Hitting bottom of platform
            player.setY(platform.getY() + platform.getHeight());
            player.setVelocityY(0);
        } else if (minOverlap == overlapLeft && player.getVelocityX() > 0) {
            // Hitting left side of platform
            player.setX(platform.getX() - player.getWidth());
            player.setVelocityX(0);
        } else if (minOverlap == overlapRight && player.getVelocityX() < 0) {
            // Hitting right side of platform
            player.setX(platform.getX() + platform.getWidth());
            player.setVelocityX(0);
        }
    }
}