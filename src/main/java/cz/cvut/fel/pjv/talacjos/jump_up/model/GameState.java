package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    GameController gameController;
    private final CollisionHandler collisionHandler = new CollisionHandler();

    private final List<Platform> platformList;
    private Player player;

    private double floorY = Constants.GAME_HEIGHT;

    public GameState(GameController gameController) {
        this.gameController = gameController;
        this.platformList = new ArrayList<Platform>();

        //
        addPlayer(new Player(100, 200, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        addPlatforms();
    }

    public void update(double deltaTime) {

        // Apply velocity to position (X and Y separately)
        double newX = player.getX() + player.getVelocityX() * deltaTime;
        double newY = player.getY() + player.getVelocityY() * deltaTime;

        // Update positions
        player.setX(newX);
        player.setY(newY);

        // Handle all collisions
        collisionHandler.handleCollisions(player, platformList, floorY, Constants.GAME_WIDTH);

        player.setVelocityY(player.getVelocityY() + Constants.GRAVITY * deltaTime);
    }

    //player control
    private void addPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    //player move
    public void movePlayerX(double coefficient) {
        player.setVelocityX(player.getMoveSpeed() * coefficient);
    }


    public void movePlayerY(double coefficient) {
        player.setVelocityY(player.getJumpPower() * coefficient);
        System.out.println(player.getVelocityY() * coefficient);
    }

    //jumping
    public void prepareJump() {
        player.setVelocityX(0);
//        player.setJumpDirection(0);
    }


//    public void playerJumpExecute() {
//        if (player.isOnGround()) {
//            player.setVelocityY(-player.getJumpPower());
//            player.setVelocityX(player.getMoveSpeed() * player.getJumpDirection());
//            player.setOnGround(false);
//        }
//    }

    public void playerJump(){
        player.setVelocityY(-player.getJumpPower());
    }

    public void playerJumpExecute() {
        // Set vertical velocity for jump (negative means upward)
        player.setVelocityY(-player.getJumpPower());
        // Apply horizontal velocity based on the aimed jump direction.
        player.setVelocityX(player.getMoveSpeed() * player.getJumpDirection());
        player.setOnGround(false);
        player.setJumping(true);

    }




    //platform control
    private void addPlatforms() {
        addSinglePlatform(100, 300, 280, 150);
        addSinglePlatform(400, 360, 100, 230);

        System.out.println(platformList);
    }

    private void addSinglePlatform(double x, double y, double width, double height) {
        Platform platform = new Platform(x, Constants.GAME_HEIGHT - y, width, height);
        platformList.add(platform);
    }

    public List<Platform> getPlatformList() {
        return platformList;
    }

    public double getFloorY() {
        return floorY;
    }

    public void setFloorY(double floorY) {
        this.floorY = floorY;
    }
}
