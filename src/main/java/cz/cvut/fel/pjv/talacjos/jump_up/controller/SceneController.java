package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SceneController{
    private GameController gameController;
    private MenuController menuController;

    public final Stage stage;

    public SceneController(Stage stage) {
        this.stage = stage;

        //init the scenes
    }

    public void showGameScene(String levelName, Boolean isLoaded) {
        this.gameController = new GameController(this, levelName, isLoaded);
        stage.setScene(gameController.setGameScene());
        gameController.startGame();
    }


    public void showMenuScene() {
        this.menuController = new MenuController(this);
        stage.setScene(menuController.setMenuScene());
    }

    public void exitGame() {
        Platform.exit();
    }


    public void addStyles(String name, Scene scene) {
        String stylesLink = getClass().getResource("/styles/" + name + ".css").toExternalForm();

        if (stylesLink == null) {
            System.err.println("WARNING: " + "styles file - " + name + ".css not found");
            return;
        }
        Font.loadFont(getClass().getResourceAsStream("/font/Jacquard12.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/font/Jersey10.ttf"), 12);
        scene.getStylesheets().add(stylesLink);
    }

}
