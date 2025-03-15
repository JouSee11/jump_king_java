package cz.cvut.fel.pjv.talacjos.jump_up.view;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.Platform;
import cz.cvut.fel.pjv.talacjos.jump_up.model.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameView {
    private final Scene scene;
    private final SceneController sceneController;
    private final GameState gameState; // get the game state data

    private final Canvas canvas;

    //image parts
    private Image backgroundImage;


    public GameView(SceneController sceneController, GameController gameController, GameState gameState) {
        this.sceneController = sceneController;
        this.gameState = gameState;

        this.canvas = new Canvas(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        scene = new Scene(root, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        //add styles to scene - later

        //handle key events
        scene.setOnKeyPressed(gameController::handleKeyPress);
        scene.setOnKeyReleased(gameController::handleKeyRelease);

        loadBackgroundImage();
    }

    public void render() {
        //render game state - later
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //clear the previous frame
        gc.clearRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Render background image
//        gc.setFill(Color.SKYBLUE);
//        gc.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        gc.drawImage(backgroundImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Render platforms
        gc.setFill(Color.GREEN);
        for (Platform platform : gameState.getPlatformList()) {
            gc.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
        }

        // Render player with animation
        Player player = gameState.getPlayer();
        Image currentFrame = player.getCurrentAnimation().getCurrentFrame();

        // Handle flipping the sprite based on direction
        if (player.isFacingRight()) {
            gc.drawImage(currentFrame, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        } else {
            // Flip the image horizontally when facing left
            gc.save(); // Save current graphics state
            gc.translate(player.getX() + player.getWidth(), player.getY()); // Move origin to right edge
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(currentFrame, 0, 0, player.getWidth(), player.getHeight()); // Draw at new origin
            gc.restore(); // Restore original graphics state
        }

//        gc.setFill(Color.RED);
//        gc.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }

    private void loadBackgroundImage() {
        backgroundImage = new Image(getClass().getResource("/images/background1.png").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
