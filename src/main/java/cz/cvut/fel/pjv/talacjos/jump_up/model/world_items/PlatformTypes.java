package cz.cvut.fel.pjv.talacjos.jump_up.model.world_items;

import javafx.scene.paint.Color;

/**
 * The PlatformTypes enum defines various types of platforms in the game.
 * Each platform type is associated with a unique name and a color.
 */
public enum PlatformTypes {
    DIRT("dirt", Color.DARKGREEN),
    STONE("stone", Color.GRAY),
    CLOUD("cloud", Color.LIGHTBLUE),
    WOOD("wood", Color.BROWN),
    ICE("ice", Color.LIGHTBLUE);

    /**
     * The name of the platform type.
     */
    private final String type;

    /**
     * The color associated with the platform type.
     */
    private final Color color;

    /**
     * Constructs a PlatformTypes enum constant with the specified type and color.
     *
     * @param type  The name of the platform type.
     * @param color The color associated with the platform type.
     */
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

    /**
     * Retrieves a PlatformTypes constant based on the provided type name.
     *
     * @param type The name of the platform type to retrieve.
     * @return The corresponding PlatformTypes constant, or null if no match is found.
     */
    public static PlatformTypes getPlatformType(String type) {
        for (PlatformTypes platformType : PlatformTypes.values()) {
            if (platformType.getType().equalsIgnoreCase(type)) {
                return platformType;
            }
        }
        return null;
    }
}
