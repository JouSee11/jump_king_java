package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.GameView;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SceneController{
    private final GameController gameController;

    public final Stage stage;

    public SceneController(Stage stage) {
        this.stage = stage;

        //init the scenes
        this.gameController = new GameController(this);
    }

    public void showGameScene() {
        stage.setScene(gameController.setGameScene());
        gameController.startGame();
    }

    public void addStyles(String name, Scene scene) {
        String stylesLink = getClass().getResource("/styles/" + name + ".css").toExternalForm();

        if (stylesLink == null) {
            System.err.println("WARNING: " + "styles file - " + name + ".css not found");
            return;
        }
        Font.loadFont(getClass().getResourceAsStream("/font/Jacquard12.ttf"), 12);
        scene.getStylesheets().add(stylesLink);
    }

}
