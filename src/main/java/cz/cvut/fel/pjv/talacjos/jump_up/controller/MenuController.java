package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
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

/**
 * The MenuController class is responsible for managing the main menu logic,
 * including handling user interactions, displaying dialogs, and loading levels or saves.
 */
public class MenuController {
    private final SceneController sceneController;
    private final MenuView menuView;

    //dialogs
    private LevelSelectView levelSelectView;
    private LoadSelectView loadSelectView;

    /**
     * Constructs a MenuController instance.
     *
     * @param sceneController The SceneController instance for managing scenes.
     */
    public MenuController(SceneController sceneController, Boolean withError) {
        this.sceneController = sceneController;
        this.menuView = new MenuView(sceneController, this, withError);

        SoundController.getInstance().playMusic("menu_music.m4a");

    }


    public Scene setMenuScene() {
        return  menuView.getScene();
    }

    /**
     * Displays the level selection dialog.
     * If the dialog already exists, it is shown; otherwise, it is created and displayed.
     */
    public void showLevelSelection() {
        // if dialog exists, show it
        if (levelSelectView != null) {
            levelSelectView.show();
        } else {
            // create dialog if it doesn't work
            levelSelectView = new LevelSelectView(this);
            menuView.addLevelSelectionDialog(levelSelectView.getRoot());
            levelSelectView.show();
        }
    }

    /**
     * Displays the load selection dialog.
     * If the dialog already exists, it is shown; otherwise, it is created and displayed.
     */
    public void showLoadSelection() {
        if (loadSelectView != null) {
            loadSelectView.show();
        } else {
            loadSelectView = new LoadSelectView(this);
            menuView.addLoadSelectionDialog(loadSelectView.getRoot());
            loadSelectView.show();
        }
    }


    /**
     * Closes the level selection dialog if it is open.
     */
    public void closeLevelSelection() {
        if (levelSelectView != null) {
            levelSelectView.hide();
        }
    }

    /**
     * Closes the load selection dialog if it is open.
     */
    public void closeLoadSelection() {
        if (loadSelectView != null) {
            loadSelectView.hide();
        }
    }


    /**
     * Retrieves a list of all level folder names from the levels directory.
     *
     * @return An ObservableList containing the names of all level folders.
     */
    public ObservableList<String> getAllLevelsFromFolder() {
        ObservableList<String> levelFolderNames = FXCollections.observableArrayList();

        try {
            // Specify the path to your levels directory
            Path levelsPath = Paths.get("maps");

            // List all directories in the levels folder
            Files.list(levelsPath)
                    .filter(Files::isDirectory)
                    .forEach(path -> levelFolderNames.add((path.getFileName().toString())));

        } catch (IOException e) {
            GameLogger.getInstance().severe("Error reading maps directories: " + e.getMessage());
        }

        return levelFolderNames;
    }

    /**
     * Retrieves a list of all save file names from the saves directory.
     *
     * @return An ObservableList containing the names of all save files.
     */
    public ObservableList<String> getAllSavesFromFolder() {
        ObservableList<String> savesNames = FXCollections.observableArrayList();

        try {
            // Specify the path to your levels directory
            Path levelsPath = Paths.get("saves");

            // List all directories in the levels folder
            Files.list(levelsPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> savesNames.add((path.getFileName().toString())));

        } catch (IOException e) {
            GameLogger.getInstance().severe("Error reading saves directories: " + e.getMessage());
        }

        return savesNames;
    }

    /**
     * Starts a new game with the specified level.
     *
     * @param levelName The name of the level to start.
     */
    public void startGameLevel(String levelName) {
        sceneController.showGameScene(levelName, false);
    }

    /**
     * Starts a game loaded from a save file.
     *
     * @param saveName The name of the save file to load.
     */
    public void startGameLevelLoaded(String saveName) {
        sceneController.showGameScene(saveName, true);
    }

}
