package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.GameView;
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
}
