package cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
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

public class MenuView {
    private final Scene scene;
    private final SceneController sceneController;
    private final MenuController menuController;

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

        //menu buttons
//        Button newGameBtn = new Button("New game");
//        newGameBtn.getStyleClass().addAll("menu-button", "menu-play");

        ImageView startGameBtn = createMenuButton("/images/main_menu/button_start.png");
        ImageView loadGameBtn = createMenuButton("/images/main_menu/button_load.png");
        ImageView exitBtn = createMenuButton("/images/main_menu/button_exit.png");

        startGameBtn.getStyleClass().add("menu-button");
        loadGameBtn.getStyleClass().add("menu-button");
        exitBtn.getStyleClass().add("menu-button");
//
//        Button loadGameBtn = new Button("Load game");
//        loadGameBtn.getStyleClass().addAll("menu-button", "menu-load");
//
//        Button exitBtn = new Button("Exit");
//        exitBtn.getStyleClass().addAll("menu-button", "menu-exit");

        //adding items to the vbox
        vBox.getChildren().addAll(startGameBtn ,loadGameBtn, exitBtn);
        vBox.setAlignment(Pos.CENTER);

        //adding actions to the buttons
//        newGameBtn.setOnAction(e -> sceneController.showGameScene());
        startGameBtn.setOnMouseClicked(e -> menuController.showLevelSelection());
        loadGameBtn.setOnMouseClicked(e -> menuController.showLoadSelection());
        exitBtn.setOnMouseClicked(e -> sceneController.exitGame());

        //adding items to the root
        root.getChildren().addAll(backgroundVideo, vBox);

        return root;
    }

    public void addLevelSelectionDialog(StackPane levelSelectionRoot) {
        if (levelSelectionRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(levelSelectionRoot);
        }
    }

    public void addLoadSelectionDialog(StackPane loadSelectionRoot) {
        if (loadSelectionRoot != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().add(loadSelectionRoot);
        }
    }

    private MediaView getBackgroundVideo() {
        String videoPath = getClass().getResource("/video/background_animation.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);

        mediaPlayer.setOnError(() -> {
            System.err.println("Chyba přehrávání videa: " + mediaPlayer.getError());
        });

        mediaPlayer.setOnReady(() -> {
            System.out.println("Video připraveno k přehrávání");
            mediaPlayer.play();
        });

        mediaView.setFitWidth(Constants.GAME_WIDTH);
        mediaView.setFitHeight(Constants.GAME_HEIGHT);

        return mediaView;
    }

    private ImageView createMenuButton(String filePath) {
        try {
            Image img = new Image((getClass().getResource(filePath).toExternalForm()));
            ImageView imgView = new ImageView(img);

            imgView.setFitWidth(150);
            imgView.setPreserveRatio(true);

            return imgView;
        } catch (Exception e) {
            System.err.println("Image doesnt exist: " + filePath);
            e.printStackTrace();
            return new ImageView();
        }

    }

}
