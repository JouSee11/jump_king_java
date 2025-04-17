package cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.MenuController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Represents the main menu view of the game.
 * This view displays the main menu with options to start a new game, load a saved game, or exit the application.
 */
public class MenuView {
    private final Scene scene;
    private final SceneController sceneController;
    private final MenuController menuController;

    /**
     * Constructs a new MenuView.
     *
     * @param sceneController The controller responsible for managing scenes.
     * @param menuController  The controller responsible for handling menu actions.
     */
    public MenuView(SceneController sceneController, MenuController menuController) {
        this.sceneController = sceneController;
        this.menuController = menuController;

        StackPane root = renderMenuView();

        scene = new Scene(root, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        sceneController.addStyles("menu_main", scene);
        sceneController.addStyles("menu_dialogs", scene);
        sceneController.addStyles("load_saves_dialog", scene);
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Renders the main menu view.
     *
     * @return A StackPane containing the main menu layout.
     */
    private StackPane renderMenuView() {
        StackPane root = new StackPane();

        MediaView backgroundVideo = getBackgroundVideo();

        //background images
        Image backgroundImage = new Image(getClass().getResource("/images/main_menu/menu_background.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(Constants.GAME_WIDTH);
        backgroundView.setFitHeight(Constants.GAME_HEIGHT);

        //menu items
        VBox vBox = new VBox(20);
        vBox.getStyleClass().add("menu_vbox");

        Label title = new Label("Jump Up");
        title.getStyleClass().add("menu_title");

        ImageView startGameBtn = createMenuButton("/images/main_menu/button_start.png");
        ImageView loadGameBtn = createMenuButton("/images/main_menu/button_load.png");
        ImageView exitBtn = createMenuButton("/images/main_menu/button_exit.png");

        startGameBtn.getStyleClass().add("menu-button");
        loadGameBtn.getStyleClass().add("menu-button");
        exitBtn.getStyleClass().add("menu-button");


        //adding items to the vbox
        vBox.getChildren().addAll(startGameBtn ,loadGameBtn, exitBtn);
        vBox.setAlignment(Pos.CENTER);

        //adding actions to the buttons
        startGameBtn.setOnMouseClicked(e -> menuController.showLevelSelection());
        loadGameBtn.setOnMouseClicked(e -> menuController.showLoadSelection());
        exitBtn.setOnMouseClicked(e -> sceneController.exitGame());

        //adding items to the root
        root.getChildren().addAll(backgroundView, backgroundVideo, vBox);

        return root;
    }

    /**
     * Adds the level selection dialog to the main menu view.
     *
     * @param levelSelectionRoot The root of the level selection dialog.
     */
    public void addLevelSelectionDialog(StackPane levelSelectionRoot) {
        if (levelSelectionRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(levelSelectionRoot);
        }
    }

    /**
     * Adds the load selection dialog to the main menu view.
     *
     * @param loadSelectionRoot The root of the load selection dialog.
     */
    public void addLoadSelectionDialog(StackPane loadSelectionRoot) {
        if (loadSelectionRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(loadSelectionRoot);
        }
    }

    /**
     * Creates and returns the background video for the main menu.
     *
     * @return A MediaView containing the background video.
     */
    private MediaView getBackgroundVideo() {
        try {
            String videoPath = getClass().getResource("/video/background_animation.mp4").toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);

            mediaPlayer.setOnError(() -> {
                GameLogger.getInstance().warning("Error playing video: " + mediaPlayer.getError());
            });

            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
            });

            mediaView.setFitWidth(Constants.GAME_WIDTH);
            mediaView.setFitHeight(Constants.GAME_HEIGHT);

            return mediaView;
        } catch (Exception e) {
            GameLogger.getInstance().warning("Failed to load video: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a menu button with the specified image file path.
     *
     * @param filePath The file path of the button image.
     * @return An ImageView representing the menu button.
     */
    private ImageView createMenuButton(String filePath) {
        try {
            Image img = new Image((getClass().getResource(filePath).toExternalForm()));
            ImageView imgView = new ImageView(img);

            imgView.setFitWidth(150);
            imgView.setPreserveRatio(true);

            return imgView;
        } catch (Exception e) {
            GameLogger.getInstance().warning("Menu button image doesnt exist: " + filePath);
            e.printStackTrace();
            return new ImageView();
        }

    }

}
