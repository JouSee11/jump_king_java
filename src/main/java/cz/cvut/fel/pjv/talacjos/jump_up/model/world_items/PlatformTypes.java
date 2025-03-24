package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import javafx.scene.paint.Color;

public enum PlatformTypes {
    DIRT("dirt", Color.DARKGREEN),
    STONE("stone", Color.GRAY),
    CLOUD("cloud", Color.LIGHTBLUE),
    WOOD("wood", Color.BROWN);

    private final String type;
    private final Color color;

    PlatformTypes(String type, Color color) {
        this.type = type;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public static PlatformTypes getPlatformType(String type) {
        for (PlatformTypes platformType : PlatformTypes.values()) {
            if (platformType.getType().equalsIgnoreCase(type)) {
                return platformType;
            }
        }
        return null;
    }
}
