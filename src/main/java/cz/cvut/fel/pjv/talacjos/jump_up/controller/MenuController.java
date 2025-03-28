package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.MenuView;
import javafx.scene.Scene;

public class MenuController {
    private SceneController sceneController;
    private MenuView menuView;

    public MenuController(SceneController sceneController) {
        this.sceneController = sceneController;
        this.menuView = new MenuView(sceneController);
    }

    public Scene setMenuScene() {
        return  menuView.getScene();
    }

}
