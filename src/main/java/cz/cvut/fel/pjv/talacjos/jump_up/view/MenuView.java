package cz.cvut.fel.pjv.talacjos.jump_up.view;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SceneController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView {
    private final Scene scene;
    private final SceneController sceneController;

    public MenuView(SceneController sceneController) {
        this.sceneController = sceneController;

        StackPane root = renderMenuView();

        scene = new Scene(root, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        sceneController.addStyles("menu_main", scene);
    }

    public Scene getScene() {
        return scene;
    }

    private StackPane renderMenuView() {
        StackPane root = new StackPane();

        //background images
        Image backgroundImage = new Image(getClass().getResource("/images/main_menu/menu_background.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(Constants.GAME_WIDTH);
        backgroundView.setFitHeight(Constants.GAME_HEIGHT);

        //menu items
        VBox vBox = new VBox(20);

        Label title = new Label("Jump Up");
        title.getStyleClass().add("menu_title");

        //menu buttons
        Button newGameBtn = new Button("New game");
        newGameBtn.getStyleClass().add("menu_button");

        Button loadGameBtn = new Button("Load game");
        loadGameBtn.getStyleClass().add("menu_button");

        Button exitBtn = new Button("Exit");
        exitBtn.getStyleClass().add("menu_button");

        //adding items to the vbox
        vBox.getChildren().addAll(title, newGameBtn, loadGameBtn, exitBtn);
        vBox.setAlignment(Pos.CENTER);

        //adding actions to the buttons
        newGameBtn.setOnAction(e -> sceneController.showGameScene());
        loadGameBtn.setOnAction(e -> sceneController.showGameScene());
        exitBtn.setOnAction(e -> sceneController.exitGame());

        //adding items to the root
        root.getChildren().addAll(backgroundView, vBox);

        return root;
    }
}
