package cz.cvut.fel.pjv.talacjos.jump_up;

import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry point for the JumpUP application.
 * This class initializes and starts the JavaFX application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by setting up the primary stage and displaying the main menu.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If an error occurs during loading resources.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JumpUP");

        //set window size
        stage.setMinHeight(930);
        stage.setMinWidth(1200);

        SceneController sceneController = new SceneController(stage);
        sceneController.showMenuScene();
        stage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}