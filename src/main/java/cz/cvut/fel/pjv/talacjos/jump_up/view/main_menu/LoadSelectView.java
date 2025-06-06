package cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.MenuController;
import cz.cvut.fel.pjv.talacjos.jump_up.view.OverlayView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.util.Arrays;

/**
 * Represents the load selection view in the main menu.
 * This view allows the user to select a saved game to load and continue playing.
 */
public class LoadSelectView extends OverlayView<MenuController> {
    private String selectedSave;

    /**
     * Constructs a new LoadSelectView.
     *
     * @param menuController The menu controller associated with this view.
     */
    public LoadSelectView(MenuController menuController) {
        super(menuController);
    }

    @Override
    protected StackPane createView() {
        StackPane overlay = new StackPane();

        // Background overlay
        Rectangle background = new Rectangle(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        background.getStyleClass().add("dialog_overlay_background");

        // Main dialog container with BorderPane for layout sections
        BorderPane dialogBox = new BorderPane();
        dialogBox.getStyleClass().add("dialog-box");
        dialogBox.getStyleClass().add("load-dialog-box");


        // Title at top
        Label title = new Label("Select save:");
        title.getStyleClass().add("dialog_title");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(0, 0, 10, 0));
        dialogBox.setTop(title);

        ObservableList<String> saves = controller.getAllSavesFromFolder();

        // sort the saves based on the date
        saves.sort((save1, save2) -> {
            String[] parts1 = save1.split("_");
            String[] parts2 = save2.split("_");

            // get the date
            String dateStr1 = parts1.length > 1 ? parts1[parts1.length - 2] : "";
            String dateStr2 = parts2.length > 1 ? parts2[parts2.length - 2] : "";

            // sort desc (newest on the top)
            return dateStr2.compareTo(dateStr1);
        });

        ListView<String> savesListView = new ListView<>(saves);
        savesListView.getStyleClass().add("list-view");
        savesListView.getStyleClass().add("load-list-view");
        savesListView.setCellFactory(createCellFactory());
        savesListView.getSelectionModel().select(0);
        dialogBox.setCenter(savesListView);


        // Button bar at bottom
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        Button loadButton = new Button("Load");
        loadButton.getStyleClass().add("dialog-button");
        loadButton.getStyleClass().add("load-dialog-button");
        loadButton.setOnAction(e -> {
            selectedSave = savesListView.getSelectionModel().getSelectedItem();
            if (selectedSave != null) {
                // get the name of the map
                controller.startGameLevelLoaded(selectedSave);
                hide();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("dialog-button");
        cancelBtn.getStyleClass().add("load-dialog-button");
        cancelBtn.setOnAction(e -> hide());

        buttonBar.getChildren().addAll(loadButton, cancelBtn);
        dialogBox.setBottom(buttonBar);

        overlay.getChildren().addAll(background, dialogBox);
        return overlay;
    }

    //customize the single save line
    /**
     * Creates a custom cell factory for the ListView to display save details.
     *
     * @return A Callback to create custom ListCell instances.
     */
    private Callback<ListView<String>, ListCell<String>> createCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String filename, boolean empty) {
                        super.updateItem(filename, empty);

                        if (empty || filename == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            try {
                                //split the name and
                                String cleanName = filename.replace(".json", "");
                                String[] parts = cleanName.split("_");
                                // If there are at least 3 parts, join all elements except the last two; otherwise, use the first part.
                                String saveName = parts.length <= 3
                                        ? parts[0]
                                        : String.join("_", Arrays.copyOfRange(parts, 0, parts.length - 2));


                                // handle date
                                String date = getDate(parts);

                                // last part is the name of the map
                                String mapName = parts[parts.length - 1];

                                // layout for the line
                                BorderPane cellLayout = new BorderPane();
                                cellLayout.setPadding(new Insets(5));
                                cellLayout.getStyleClass().add("save-cell-layout");

                                // name is the main text
                                Label nameLabel = new Label(saveName);
                                nameLabel.getStyleClass().add("save-name-label");
                                cellLayout.setLeft(nameLabel);

                                // map name on the right side
                                VBox infoBox = new VBox(2);
                                infoBox.setAlignment(Pos.CENTER_RIGHT);

                                Label mapLabel = new Label("Map: " + mapName);
                                mapLabel.getStyleClass().add("map-label");

                                // display time of the save
                                Label dateLabel = new Label("Save time: " + date);
                                dateLabel.getStyleClass().add("date-label");

                                infoBox.getChildren().addAll(mapLabel, dateLabel);
                                cellLayout.setRight(infoBox);

                                setGraphic(cellLayout);
                                setText(null);
                            } catch (Exception e) {
                                // when there is error parsing display the original content
                                setText(filename);
                                setGraphic(null);
                            }
                        }
                    }

                };
            }
        };
    }

    private static String getDate(String[] parts) {
        String[] dateParts = parts[parts.length - 2].split("-");
        StringBuilder dateBuilder = new StringBuilder();
        for (int i = 0; i < dateParts.length; i++) {
            if (i == 1 || i == 2) dateBuilder.append(".");
            if (i == 3) dateBuilder.append(" ");
            if (i > 3) dateBuilder.append(":");
            dateBuilder.append(dateParts[i]);
        }
        return dateBuilder.toString();
    }

    /**
     * Handles the actions to perform when the load selection view is hidden.
     */
    @Override
    protected void onHide() {controller.closeLoadSelection();}
}
