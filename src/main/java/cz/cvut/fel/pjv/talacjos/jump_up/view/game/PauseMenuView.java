package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.view.OverlayView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the pause menu view displayed when the game is paused.
 * This view is an overlay that provides options to resume, save and exit, or exit the game.
 */
public class PauseMenuView extends OverlayView<GameController> {

    /**
     * Constructs a new PauseMenuView.
     *
     * @param gameController The game controller associated with this view.
     */
    public PauseMenuView(GameController gameController) {
        super(gameController);

    }

    @Override
    protected StackPane createView() {
        StackPane overlay = new StackPane();

        // Semi-transparent background
        Rectangle background = new Rectangle(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        background.setFill(Color.rgb(0, 0, 0, 0.7));

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Label title = new Label("PAUSED");
        title.getStyleClass().add("pause_title");

        Button resumeBtn = new Button("Resume");
        resumeBtn.getStyleClass().add("pause_button");
        resumeBtn.setOnAction(e -> controller.resumeGame());

        Button exitSaveBtn = new Button("Save and exit");
        exitSaveBtn.getStyleClass().add("pause_button");
        exitSaveBtn.setOnAction(e -> controller.showSaveDialog());

        Button exitBtn = new Button("Exit");
        exitBtn.getStyleClass().add("pause_button");
        exitBtn.setOnAction(e -> controller.endGame());

        menuBox.getChildren().addAll(title, resumeBtn, exitSaveBtn, exitBtn);
        overlay.getChildren().addAll(background, menuBox);

        return overlay;
    }

}
