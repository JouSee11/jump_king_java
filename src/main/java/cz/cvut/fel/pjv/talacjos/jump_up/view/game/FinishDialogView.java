package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.view.OverlayView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Represents the finish dialog view displayed when the game ends.
 * This view is an overlay that shows a victory message and provides options to return to the menu.
 */
public class FinishDialogView extends OverlayView<GameController> {

    /**
     * Constructs a new FinishDialogView.
     *
     * @param gameController The game controller associated with this view.
     */
    public FinishDialogView(GameController gameController) {
        super(gameController);
    }


    @Override
    protected StackPane createView() {
        StackPane overlay = new StackPane();

        Rectangle background = new Rectangle(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        background.getStyleClass().add("finish_overlay_background");
//        background.setFill(Color.rgb(0, 0, 0, 0.8));

        VBox dialogBox = new VBox(20);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.getStyleClass().add("finish_dialog_box");
        dialogBox.setMaxWidth(Constants.GAME_WIDTH / 2);
        dialogBox.setMaxHeight(Constants.GAME_HEIGHT / 2);

        Label title = new Label("VICTORY!");
        title.getStyleClass().add("victory_title");
//
//        Label statsLabel = new Label("Keys collected: " + collectedKeys + "/" + totalKeys);
//        statsLabel.getStyleClass().add("victory_text");

        Button menuBtn = new Button("Return to Menu");
        menuBtn.getStyleClass().add("finish_button");
        menuBtn.setOnAction(e -> controller.endGame());

//        Button exitBtn = new Button("Exit Game");
//        exitBtn.getStyleClass().add("victory_button");
//        exitBtn.setOnAction(e -> gameController.exitGame());

        dialogBox.getChildren().addAll(title, menuBtn);
        overlay.getChildren().addAll(background, dialogBox);

        return overlay;
    }
}
