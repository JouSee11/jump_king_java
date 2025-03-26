package cz.cvut.fel.pjv.talacjos.jump_up;

import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JumpUP");

        //set window size
        stage.setMinHeight(930);
        stage.setMinWidth(1200);

        SceneController sceneController = new SceneController(stage);
        sceneController.showGameScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}