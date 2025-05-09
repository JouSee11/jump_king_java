package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.*;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the main game view, responsible for rendering the game state and UI elements.
 */
public class GameView {
    private final Scene scene;
    private final GameState gameState; // get the game state data

    private final Canvas canvas;

    //cashing data - optimisation
    private Image backgroundImage;
    private final Map<Integer, Image> backgroundCache = new HashMap<>();
//    private Map<String, Image> platformsImageCache = new HashMap<String, Image>();

    //ui parts
    private Label keyCountLabel;
    private Label powerUpTimerLabel;

    private ImageView timerIconView;

    /**
     * Constructs a new GameView.
     *
     * @param sceneController The controller for managing scenes.
     * @param gameController  The controller for handling game logic.
     * @param gameState       The current state of the game.
     */
    public GameView(SceneController sceneController, GameController gameController, GameState gameState) {
        this.gameState = gameState;

        this.canvas = new Canvas(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Create a Group to hold the canvas and UI elements
        StackPane gameArea = new StackPane();
        gameArea.setPrefSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        gameArea.getChildren().add(canvas);

        StackPane root = new StackPane();
        root.getChildren().add(gameArea);

        scene = new Scene(root, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        //add styles to scene - later
        sceneController.addStyles("game_main", scene);
        sceneController.addStyles("pause_menu", scene);
        sceneController.addStyles("finish_dialog", scene);
        sceneController.addStyles("save_dialog", scene);

        //show ui parts
        renderKeyCountUI(gameArea);
        renderPowerUpTimerUi(gameArea);

        //handle key events
        scene.setOnKeyPressed(gameController::handleKeyPress);
        scene.setOnKeyReleased(gameController::handleKeyRelease);

    }

    /**
     * Renders the game state and updates the UI elements.
     */
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

        //update interface elements
        updateKeyCountUI();
        updatePowerUpTimer();

        // Render player with animation
        renderPlayer(gc);

        //Render key with animation
        renderKey(gc);

        //render powerups with animation
        renderPowerUp(gc);

        //render end element
        renderEnd(gc);

        //apply color filters - when some item was colected
        applyColorFilter(gc);

    }

    /**
     * Renders the platforms in the game.
     *
     * @param gc The graphics context used for rendering.
     */
    private void renderPlatform(GraphicsContext gc) {
        double cornerRadius = 4;

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
            gc.setLineWidth(3);
            gc.strokeRoundRect(platform.getX(), platform.getY(), platform.getWidth(),
                    platform.getHeight(), cornerRadius*2, cornerRadius*2);

        }
    }

    /**
     * Renders the keys in the game.
     *
     * @param gc The graphics context used for rendering.
     */
    private void renderKey(GraphicsContext gc) {
        for (Key key : gameState.getKeyList()) {
            Image curFrame = key.getCurrentAnimation().getCurrentFrame();
            gc.drawImage(curFrame, key.getX(), key.getY(), key.getWidth(), key.getHeight());
        }
    }

    /**
     * Renders the power-ups in the game.
     *
     * @param gc The graphics context used for rendering.
     */
    private void renderPowerUp(GraphicsContext gc) {
        for (PowerUp powerup : gameState.getCurPowerupList()) {
            Image curFrame = powerup.getCurrentAnimation().getCurrentFrame();
            gc.drawImage(curFrame, powerup.getX(), powerup.getY(), powerup.getWidth(), powerup.getHeight());
        }
    }

    /**
     * Renders the player in the game, including flipping the sprite based on direction.
     *
     * @param gc The graphics context used for rendering.
     */
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

    /**
     * Renders the key count UI in the game.
     *
     * @param root The root container to which the UI is added.
     */
    private void renderKeyCountUI(StackPane root) {
        keyCountLabel = new Label("0/3");
        keyCountLabel.getStyleClass().add("count_label");

        //load the icon
        Image keyImage = new Image(getClass().getResource("/images/assets/key.png").toExternalForm());
        ImageView keyIconView = new ImageView(keyImage);
        keyIconView.setFitWidth(50);
        keyIconView.setFitHeight(50);
        keyIconView.setPreserveRatio(true);

        // Create HBox to hold icon and label horizontally
        HBox keyBox = new HBox(10); // 10px spacing
        keyBox.getChildren().addAll(keyCountLabel, keyIconView);
        keyBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Position the HBox in the bottom right corner
        StackPane.setMargin(keyBox, new Insets(0, 20, 20, 0));

        // Add to root
        root.getChildren().add(keyBox);
    }

    /**
     * Renders the power-up timer UI in the game.
     *
     * @param root The root container to which the UI is added.
     */
    private void renderPowerUpTimerUi(StackPane root) {
        powerUpTimerLabel = new Label("");
        powerUpTimerLabel.getStyleClass().add("power-up-timer");
        powerUpTimerLabel.setVisible(false);

        //load icon
        Image timerImage = new Image(getClass().getResource("/images/assets/timer.png").toExternalForm());
        timerIconView = new ImageView(timerImage);
        timerIconView.setFitWidth(30);
        timerIconView.setFitHeight(30);
        timerIconView.setVisible(false);

        // Create HBox to hold icon and label horizontally
        HBox keyBox = new HBox(10); // 10px spacing
        keyBox.getChildren().addAll(timerIconView, powerUpTimerLabel);
        keyBox.setAlignment(Pos.BOTTOM_LEFT);

        // Position the HBox in the bottom right corner
        StackPane.setMargin(keyBox, new Insets(0, 0, 20, 40));

        // Add to root
        root.getChildren().add(keyBox);
    }

    /**
     * Renders the end element in the game.
     *
     * @param gc The graphics context used for rendering.
     */
    private void renderEnd(GraphicsContext gc) {
        End end = gameState.getEnd();
        if (end == null) {return;}

        Image currentFrame = end.getCurrentAnimation().getCurrentFrame();
        gc.drawImage(currentFrame, end.getX(), end.getY(), end.getWidth(), end.getHeight());

        //render the key press icon above the end if player collide end
        if (gameState.isCollisionEnd()) {
            int iconSize = 30;
            String curState = gameState.isActionButtonPressed() ? "_pressed" : "";
            Image keypress_img = new Image(getClass().getResource("/images/map_end/E_icon" + curState + ".png").toExternalForm());
            double x_center = end.getX() - iconSize / 2 + end.getWidth() / 2;
            gc.drawImage(keypress_img, x_center, end.getY() - 40, iconSize, iconSize);
        }

    }

    /**
     * Updates the key count UI based on the current game state.
     */
    private void updateKeyCountUI() {
        int[] keyStats = gameState.getKeyStats();
        keyCountLabel.setText(keyStats[1] + "/" + keyStats[0]);
    }

    /**
     * Loads the background image for the current level.
     */
    private void loadBackgroundImage() {
        int curLevel = Math.min(gameState.getCurLevel(), 7);
        if (!backgroundCache.containsKey(curLevel)) {
            backgroundCache.put(curLevel, new Image(getClass().getResource("/images/background/background" + curLevel + ".png").toExternalForm()));
        }
        backgroundImage = backgroundCache.get(curLevel);
    }

    /**
     * Updates the power-up timer UI based on the current game state.
     *
     * @param gc The graphics context used for rendering.
     */
    private void updatePowerUpTimer() {
        if (gameState.isPowerUpActive()) {
            int timeRemaining = gameState.getPowerUpTimeRemaining();
            String formattedString = String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60);
            powerUpTimerLabel.setText(formattedString);
            powerUpTimerLabel.setVisible(true);
            timerIconView.setVisible(true);
        } else {
            powerUpTimerLabel.setVisible(false);
            timerIconView.setVisible(false);
        }
    }

    /**
     * Applies a color filter to the game view when a power-up is active.
     *
     * @param gc The graphics context used for rendering.
     */
    private void applyColorFilter(GraphicsContext gc) {
        if (!gameState.isPowerUpActive()) {
            return;
        }

        // Save the current state
        gc.save();

        // Set blend mode to preserve contrast
        gc.setGlobalBlendMode(BlendMode.OVERLAY);

        // Set a semi-transparent red color
        gc.setGlobalAlpha(0.4);

        gc.setFill(Color.RED);

        // Draw over the entire canvas
        gc.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Restore previous state
        gc.restore();
    }

    /**
     * Adds a pause menu overlay to the game view.
     *
     * @param pauseMenuRoot The root container of the pause menu.
     */
    public void addPauseMenu(StackPane pauseMenuRoot) {
        if (pauseMenuRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(pauseMenuRoot);
        }
    }

    /**
     * Adds a finish dialog overlay to the game view.
     *
     * @param finishDialogRoot The root container of the finish dialog.
     */
    public void addFinishDialog(StackPane finishDialogRoot) {
        if (finishDialogRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(finishDialogRoot);
        }
    }

    /**
     * Adds a save dialog overlay to the game view.
     *
     * @param saveDialogRoot The root container of the save dialog.
     */
    public void addSaveDialog(StackPane saveDialogRoot) {
        if (saveDialogRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(saveDialogRoot);
        }
    }



    public Scene getScene() {
        return scene;
    }
}
