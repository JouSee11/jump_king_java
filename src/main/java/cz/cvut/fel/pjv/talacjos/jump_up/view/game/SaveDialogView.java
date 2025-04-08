package cz.cvut.fel.pjv.talacjos.jump_up.view.game;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.view.OverlayView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class SaveDialogView extends OverlayView<GameController> {

    public SaveDialogView(GameController gameController) {
        super(gameController);
    }

    @Override
    protected StackPane createView() {
        StackPane overlay = new StackPane();

        Rectangle background = new Rectangle(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        background.getStyleClass().add("background");

        VBox dialogBox = new VBox(20);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.getStyleClass().add("save_dialog_box");

        Label title = new Label("Save name:");
        title.getStyleClass().add("save_title");

        TextField saveNameField = new TextField();
        saveNameField.setPromptText("Enter name of the save");
        saveNameField.getStyleClass().add("save_text_field");

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("save_button");
        saveBtn.setOnAction(e -> {
            String saveName = saveNameField.getText().trim();
            if (!saveName.isEmpty()) {
                controller.saveGame(saveName);
                hide();
                controller.endGame();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("save_button");
        cancelBtn.setOnAction(e -> hide());

        buttonsBox.getChildren().addAll(saveBtn, cancelBtn);
        dialogBox.getChildren().addAll(title, saveNameField, buttonsBox);

        overlay.getChildren().addAll(background, dialogBox);
        return overlay;
    }

    @Override
    protected void onHide() {controller.closeSaveDialog();}



}
