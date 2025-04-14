package cz.cvut.fel.pjv.talacjos.jump_up.view;

import javafx.scene.layout.StackPane;

/**
 * Represents an abstract base class for overlay views in the application.
 * Overlay views are UI components that can be shown or hidden on top of other content.
 *
 * @param <T> The type of the controller associated with this overlay view.
 */
abstract public class OverlayView<T> {
    protected final StackPane viewRoot;
    protected final T controller;

    /**
     * Constructs a new OverlayView.
     *
     * @param controller The controller associated with this overlay view.
     */
    public OverlayView(T controller) {
        this.controller = controller;
        viewRoot = createView();
        viewRoot.setVisible(false);
    }

    /**
     * Creates the visual components of the overlay view.
     * This method must be implemented by subclasses to define the specific layout of the overlay.
     *
     * @return A StackPane representing the root of the overlay view.
     */
    abstract protected StackPane createView();

    /**
     * Shows the overlay view by making it visible.
     */
    public void show() {viewRoot.setVisible(true);};

    /**
     * Hides the overlay view by making it invisible.
     */
    public void hide() {viewRoot.setVisible(false);};

    protected void onHide() {}

    public StackPane getRoot() {
        return viewRoot;
    }
}
