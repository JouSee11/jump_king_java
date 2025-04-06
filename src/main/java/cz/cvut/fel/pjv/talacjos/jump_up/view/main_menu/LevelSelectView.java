package cz.cvut.fel.pjv.talacjos.jump_up.view.main_menu;

        import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
        import cz.cvut.fel.pjv.talacjos.jump_up.controller.MenuController;
        import cz.cvut.fel.pjv.talacjos.jump_up.view.OverlayView;
        import javafx.collections.ObservableList;
        import javafx.geometry.Insets;
        import javafx.geometry.Pos;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.ListView;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.StackPane;
        import javafx.scene.shape.Rectangle;

public class LevelSelectView extends OverlayView<MenuController> {
            private String selectedLevel;

            public LevelSelectView(MenuController menuController) {
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

                // Title at top
                Label title = new Label("Select map:");
                title.getStyleClass().add("dialog_title");
                BorderPane.setAlignment(title, Pos.CENTER);
                BorderPane.setMargin(title, new Insets(0, 0, 10, 0));
                dialogBox.setTop(title);

                ObservableList<String> levels = controller.getAllLevelsFromFolder();


                ListView<String> levelListView = new ListView<>(levels);
                levelListView.getStyleClass().add("list_view");
                levelListView.getSelectionModel().select(0);
                dialogBox.setCenter(levelListView);


                // Button bar at bottom
                HBox buttonBar = new HBox(10);
                buttonBar.setAlignment(Pos.CENTER);
                buttonBar.setPadding(new Insets(10, 0, 0, 0));

                Button okBtn = new Button("Ok");
                okBtn.getStyleClass().add("dialog-button");
                okBtn.setOnAction(e -> {
                    selectedLevel = levelListView.getSelectionModel().getSelectedItem();
                    if (selectedLevel != null) {
                        System.out.println(selectedLevel);
                        // Here we would call a method in controller to start the selected level
                        controller.startGameLevel(selectedLevel);
                        hide();
                    }
                });

                Button cancelBtn = new Button("Cancel");
                cancelBtn.getStyleClass().add("dialog-button");
                cancelBtn.setOnAction(e -> hide());

                buttonBar.getChildren().addAll(okBtn, cancelBtn);
                dialogBox.setBottom(buttonBar);

                overlay.getChildren().addAll(background, dialogBox);
                return overlay;
            }




        }