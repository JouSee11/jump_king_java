package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.FinishDialogView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.LevelSelectView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.MenuView;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.List;

public class MenuController {
    private SceneController sceneController;
    private MenuView menuView;
    private LevelSelectView levelSelectView;

    public MenuController(SceneController sceneController) {
        this.sceneController = sceneController;
        this.menuView = new MenuView(sceneController, this);

    }

    public Scene setMenuScene() {
        return  menuView.getScene();
    }

    public void showMenuSelection() {
        LevelSelectView levelSelectView = new LevelSelectView(this);
        menuView.addLevelSelectionDialog(levelSelectView.getRoot());
        levelSelectView.show();
    }

    //TODO: get all the folder names for the levels (maps)
    public List<String> getAllLevelsFromFolder() {
        return List.of();
    }
}
