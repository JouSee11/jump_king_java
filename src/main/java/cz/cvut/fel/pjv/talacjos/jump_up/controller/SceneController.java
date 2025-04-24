package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The SceneController class is responsible for managing the application's scenes,
 * including transitioning between the game and menu scenes, applying styles, and exiting the game.
 */
public class SceneController{
    private GameController gameController;
    private MenuController menuController;

    public final Stage stage;

    /**
     * Constructs a SceneController instance.
     *
     * @param stage The primary stage of the application.
     */
    public SceneController(Stage stage) {
        this.stage = stage;

        //init the scenes
    }

    /**
     * Displays the game scene with the specified level.
     *
     * @param levelName The name of the level to load.
     * @param isLoaded  Indicates whether the game is loaded from a save file.
     */
    public void showGameScene(String levelName, Boolean isLoaded) {
        try{

            this.gameController = new GameController(this, levelName, isLoaded);
            Scene gameScene = gameController.setGameScene();

            if (gameScene != null) {
                stage.setScene(gameScene);
                gameController.startGame();
            }

        } catch (Exception e) {
            showMenuSceneWithError();
        }



    }

    /**
     * Displays the main menu scene.
     */
    public void showMenuScene() {
        this.menuController = new MenuController(this, false);
        stage.setScene(menuController.setMenuScene());
    }

    public void showMenuSceneWithError() {
        this.menuController = new MenuController(this, true);
        stage.setScene(menuController.setMenuScene());
    }

    /**
     * Exits the game by terminating the application.
     */
    public void exitGame() {
        GameLogger.getInstance().fine("Exiting game application");
        Platform.exit();
    }

    /**
     * Adds styles and fonts to the specified scene.
     *
     * @param name  The name of the CSS file (without the .css extension).
     * @param scene The Scene object to which the styles will be applied.
     */
    public void addStyles(String name, Scene scene) {
        String stylesLink = getClass().getResource("/styles/" + name + ".css").toExternalForm();

        if (stylesLink == null) {
            GameLogger.getInstance().severe("WARNING: " + "styles file - " + name + ".css not found");
            return;
        }
        Font.loadFont(getClass().getResourceAsStream("/font/Jacquard12.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/font/Jersey10.ttf"), 12);
        scene.getStylesheets().add(stylesLink);
    }

}
