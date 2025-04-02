package cz.cvut.fel.pjv.talacjos.jump_up.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonDataLoader {

    public static HashMap<Integer,Level> loadLevelsJson(String filePath) {
        Gson gson = new Gson();
        HashMap<Integer, Level> levels = new HashMap<Integer, Level>();

        try (FileReader reader = new FileReader(filePath)) {
            // Convert JSON File to Java Object
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            //get all the levels array
            JsonArray jsonArrayLevels = jsonObject.getAsJsonArray("levels");

            //go through all levels
            for (JsonElement levelElement : jsonArrayLevels) {
                JsonObject levelObj = levelElement.getAsJsonObject();

                //create new level object
                Level level = new Level();
                level.setId(levelObj.get("id").getAsInt());

                //get all platforms for level and add them to the level object
                JsonArray platformsArray = levelObj.getAsJsonArray("platforms");
                List<Platform> platforms = getPlatformList(platformsArray);

                //get all keys for the level
                JsonArray keysArray = levelObj.getAsJsonArray("keys");
                List<Key> keys = getCollectableList(keysArray, "key");

                //get all powerups for the level
                JsonArray powerUpsArray = levelObj.getAsJsonArray("powerUp");
                List<PowerUp> powerUps = getCollectableList(powerUpsArray, "powerUp");

                //load the end if the map if it is end level
                JsonObject endObj = levelObj.getAsJsonObject("end");
                End endElement = null;
                if (endObj != null) {
                    endElement = getEndElement(endObj);
                }

                level.setPlatforms(platforms);
                level.setKeys(keys);
                level.setPowerUps(powerUps);
                level.setEnd(endElement);
                levels.put(level.getId(), level);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    private static <T extends Entity> List<T> getCollectableList(JsonArray collectableArray, String type) {
        List<T> collectables = new ArrayList<T>();

        //go through all keys for the level
        for (JsonElement collectableElement : collectableArray) {
            JsonObject collectableObj = collectableElement.getAsJsonObject();

            int x = collectableObj.get("x").getAsInt();
            int y = collectableObj.get("y").getAsInt();
            int id = collectableObj.get("id").getAsInt();


            if (type.equals("key")) {
                Key key = new Key(x, y, id);
                collectables.add((T) key);
            } else {
                PowerUp powerUp = new PowerUp(x, y, id);
                collectables.add((T) powerUp);
            }
        }
        return collectables;
    }

    private static List<Platform> getPlatformList(JsonArray platformsArray) {
        List<Platform> platforms = new ArrayList<Platform>();

        //go through all platforms
        for (JsonElement platformElement : platformsArray) {
            JsonObject platformObj = platformElement.getAsJsonObject();

            int x = platformObj.get("x").getAsInt();
            int y = platformObj.get("y").getAsInt();
            int width = platformObj.get("width").getAsInt();
            int height = platformObj.get("height").getAsInt();
            String type = platformObj.get("type").getAsString();

            PlatformTypes platformType = PlatformTypes.getPlatformType(type);
            Platform platform = new Platform(
                    x,
                    Constants.GAME_HEIGHT - y,
                    width,
                    height,
                    platformType);

            platforms.add(platform);
        }
        return platforms;
    }

    public static Player loadPlayerJson(String filePath) {
        Gson gson = new Gson();
        Player player = null;

        try (FileReader reader = new FileReader(filePath)){
            JsonObject playerObject = gson.fromJson(reader, JsonObject.class);

            double x = playerObject.get("x").getAsDouble();
            double y = playerObject.get("y").getAsDouble();

            player = new Player(x, Constants.GAME_HEIGHT - y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        } catch (IOException e){
            e.printStackTrace();
        }

        return player;
    }

    public static int[] loadKeysStatsJson(String filePath) {
        Gson gson = new Gson();

        int[] keysStats = new int[2];

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class).getAsJsonObject("keys");
//            JsonObject keysStatsObject = jsonObject.getAsJsonObject("keys");

            keysStats[0] = jsonObject.get("allCount").getAsInt();
            keysStats[1] = jsonObject.get("collected").getAsInt();

            return keysStats;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static End getEndElement(JsonObject endObj) {
        int x = endObj.get("x").getAsInt();
        int y = endObj.get("y").getAsInt();

        return new End(x, Constants.GAME_HEIGHT - y);
    }


}
