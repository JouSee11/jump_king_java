package cz.cvut.fel.pjv.talacjos.jump_up.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Platform extends Entity{
    private PlatformTypes type;
    private Image image;

    public Platform(double x, double y, double width, double height, PlatformTypes type) {
        super(x, y, width, height);
        this.type = type;
        loadImage(this.type.getType());
    }

    //for no type specified
    public Platform(double x, double y, double width, double height) {
        this(x, y, width, height, PlatformTypes.DIRT);
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
