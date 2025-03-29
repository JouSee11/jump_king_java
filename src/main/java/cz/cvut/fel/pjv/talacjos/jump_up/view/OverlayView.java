package cz.cvut.fel.pjv.talacjos.jump_up.view;

import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import javafx.scene.layout.StackPane;

abstract public class OverlayView {
    protected final StackPane viewRoot;
    protected final GameController gameController;

    public OverlayView(GameController gameController) {
        this.gameController = gameController;
        viewRoot = createView();
        viewRoot.setVisible(false);
    }

    abstract protected StackPane createView();

    public void show() {viewRoot.setVisible(true);};
    public void hide() {viewRoot.setVisible(false);};

    public StackPane getRoot() {
        return viewRoot;
    }
}
