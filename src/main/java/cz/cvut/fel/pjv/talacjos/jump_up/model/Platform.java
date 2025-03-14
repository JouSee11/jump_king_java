package cz.cvut.fel.pjv.talacjos.jump_up.model;

public class Platform extends Entity{
    private String type;

    public Platform(double x, double y, double width, double height, String type) {
        super(x, y, width, height);
        this.type = type;
    }

    //for no type specified
    public Platform(double x, double y, double width, double height) {
        this(x, y, width, height, "normal");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
