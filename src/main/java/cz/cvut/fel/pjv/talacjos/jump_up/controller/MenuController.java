package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu.LevelSelectView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu.MenuView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public ObservableList<String> getAllLevelsFromFolder() {
        ObservableList<String> levelFolders = FXCollections.observableArrayList();

        try {
            // Specify the path to your levels directory
            Path levelsPath = Paths.get("src/main/resources/maps");

            // List all directories in the levels folder
            Files.list(levelsPath)
                    .filter(Files::isDirectory)
                    .forEach(path -> levelFolders.add((path.getFileName().toString())));

        } catch (IOException e) {
            System.err.println("Error reading level directories: " + e.getMessage());
        }

        return levelFolders;
    }

    public void startGameLevel(String levelName) {
        sceneController.showGameScene(levelName);
    }

}
