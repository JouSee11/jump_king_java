package cz.cvut.fel.pjv.talacjos.jump_up.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Platform extends Entity{
    private String type;
    private Image image;

    public Platform(double x, double y, double width, double height, String type) {
        super(x, y, width, height);
        this.type = PlatformTypes.valueOf(type).getType();
        loadImage(type);
    }

    //for no type specified
    public Platform(double x, double y, double width, double height) {
        this(x, y, width, height, "dirt");
    }

    public void setType(String type) {
        this.type = PlatformTypes.valueOf(type).getType();
        loadImage(type);
    }

    public String getType() {
        return type;
    }

    public Color getBorderColor() {
        return  PlatformTypes.valueOf(type).getColor();
    }

    private void loadImage(String type) {
        //get random image for the specified type
        int randomImg = (int) (Math.random() * 3) + 1;
        String imageLink = getClass().getResource("/images/platform/" + type + randomImg + ".png").toExternalForm();
        image = new Image(imageLink);
    }

    public Image getImage() {
        return image;
    }
}
