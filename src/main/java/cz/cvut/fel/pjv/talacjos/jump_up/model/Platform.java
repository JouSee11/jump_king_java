package cz.cvut.fel.pjv.talacjos.jump_up.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform extends Rectangle {
    private PlatformTypes type;
    private Image image;

    public Platform(int x, int y, int width, int height, PlatformTypes type) {
        super(x, y, width, height);
        this.type = type;
        loadImage(this.type.getType());
    }


    public void setType(PlatformTypes type) {
        this.type = type;
        loadImage(this.type.getType());
    }

    public PlatformTypes getType() {
        return type;
    }

    public Color getBorderColor() {
        return  type.getColor();
    }

    private void loadImage(String type) {
        //get random image for the specified type
        int randomImg = (int) (Math.random() * 3) + 1;
        String imageLink = getClass().getResource("/images/platform/" + type + "/" + type  + randomImg + ".png").toExternalForm();
        image = new Image(imageLink);
    }

    public Image getImage() {
        return image;
    }
}
