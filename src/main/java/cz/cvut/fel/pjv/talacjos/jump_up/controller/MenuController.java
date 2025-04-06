package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu.LevelSelectView;
import cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu.LoadSelectView;
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

    public MenuController(SceneController sceneController) {
        this.sceneController = sceneController;
        this.menuView = new MenuView(sceneController, this);

    }

    public Scene setMenuScene() {
        return  menuView.getScene();
    }

    public void showLevelSelection() {
        LevelSelectView levelSelectView = new LevelSelectView(this);
        menuView.addLevelSelectionDialog(levelSelectView.getRoot());
        levelSelectView.show();
    }
    public void showLoadSelection() {
        LoadSelectView loadSelectView = new LoadSelectView(this);
        menuView.addLoadSelectionDialog(loadSelectView.getRoot());
        loadSelectView.show();
    }

    public ObservableList<String> getAllLevelsFromFolder() {
        ObservableList<String> levelFolderNames = FXCollections.observableArrayList();

        try {
            // Specify the path to your levels directory
            Path levelsPath = Paths.get("src/main/resources/maps");

            // List all directories in the levels folder
            Files.list(levelsPath)
                    .filter(Files::isDirectory)
                    .forEach(path -> levelFolderNames.add((path.getFileName().toString())));

        } catch (IOException e) {
            System.err.println("Error reading level directories: " + e.getMessage());
        }

        return levelFolderNames;
    }

    public ObservableList<String> getAllSavesFromFolder() {
        ObservableList<String> savesNames = FXCollections.observableArrayList();

        try {
            // Specify the path to your levels directory
            Path levelsPath = Paths.get("src/main/resources/saves");

            // List all directories in the levels folder
            Files.list(levelsPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> savesNames.add((path.getFileName().toString())));

        } catch (IOException e) {
            System.err.println("Error reading level directories: " + e.getMessage());
        }

        return savesNames;
    }

    public void startGameLevel(String levelName) {
        sceneController.showGameScene(levelName, false);
    }

    public void startGameLevelLoaded(String saveName) {
        sceneController.showGameScene(saveName, true);
    }

}
