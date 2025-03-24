package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Key;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Platform;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.PowerUp;

import java.util.List;

//one level = one screen
public class Level {
    private int id;
    private List<Platform> platforms;
    private List<Key> keys;
    private List<PowerUp> powerUps;
    //late add keys and powerups

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(List<PowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    @Override
    public String toString() {
        System.out.println("Level id: " + id);
        for (Platform platform : platforms) {
            System.out.println("Platform: " + platform.getType());
        }
        return null;
    }
}
