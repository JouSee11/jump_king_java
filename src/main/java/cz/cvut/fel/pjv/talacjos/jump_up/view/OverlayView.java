package cz.cvut.fel.pjv.talacjos.jump_up.view;

import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import javafx.scene.layout.StackPane;

abstract public class OverlayView<T> {
    protected final StackPane viewRoot;
    protected final T controller;

    public OverlayView(T controller) {
        this.controller = controller;
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
