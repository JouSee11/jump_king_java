package cz.cvut.fel.pjv.talacjos.jump_up.view;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.GameState;
import cz.cvut.fel.pjv.talacjos.jump_up.model.Platform;
import cz.cvut.fel.pjv.talacjos.jump_up.model.Player;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

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
        sceneController.addStyles("game_main", scene);


        //handle key events
        scene.setOnKeyPressed(gameController::handleKeyPress);
        scene.setOnKeyReleased(gameController::handleKeyRelease);

    }

    //render the game updates every game loop !!!
    public void render() {

        //render game state - later
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //clear the previous frame
        gc.clearRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        loadBackgroundImage();
        // Render background image
        gc.drawImage(backgroundImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Render platforms
        renderPlatform(gc);

        // Render player with animation
        renderPlayer(gc);

    }

    private void renderPlatform(GraphicsContext gc) {
        double cornerRadius = 7;

        for (Platform platform : gameState.getPlatformList()) {
            Image platformImage = platform.getImage();

            if (platformImage != null) {
                // Get dimensions
                double platformWidth = platform.getWidth();
                double platformHeight = platform.getHeight();
                double imageWidth = platformImage.getWidth();
                double imageHeight = platformImage.getHeight();

                // Tile the image across the platform
                for (double x = 0; x < platformWidth; x += imageWidth) {
                    for (double y = 0; y < platformHeight; y += imageHeight) {
                        double tileWidth = Math.min(imageWidth, platformWidth - x);
                        double tileHeight = Math.min(imageHeight, platformHeight - y);

                        gc.drawImage(
                                platformImage,
                                0, 0, tileWidth, tileHeight,  // Source rectangle
                                platform.getX() + x, platform.getY() + y, tileWidth, tileHeight  // Destination rectangle
                        );
                    }
                }
            } else {
                // Fallback to solid color
                gc.setFill(Color.GREEN);
                gc.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            }

            //add border
            gc.setStroke(platform.getBorderColor());
            gc.setLineWidth(6);
            gc.strokeRoundRect(platform.getX(), platform.getY(), platform.getWidth(),
                    platform.getHeight(), cornerRadius*2, cornerRadius*2);
        }
    }

    private void renderPlayer(GraphicsContext gc) {
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
    }



    private void loadBackgroundImage() {
//        int randomImg = (int) (Math.random() * 7) + 1;
        int imgNumber = gameState.getCurLevel();
        backgroundImage = new Image(getClass().getResource("/images/background/background" + imgNumber + ".png").toExternalForm());
//        backgroundImage = new Image(getClass().getResource("/images/background/background1" + ".png").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
