package cz.cvut.fel.pjv.talacjos.jump_up.model;

import java.util.List;

//one level = like one screen
public class Level {
    private int id;
    private List<Platform> platforms;
    private List<Key> keys;
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

    @Override
    public String toString() {
        System.out.println("Level id: " + id);
        for (Platform platform : platforms) {
            System.out.println("Platform: " + platform.getType());
        }
        return null;
    }
}
