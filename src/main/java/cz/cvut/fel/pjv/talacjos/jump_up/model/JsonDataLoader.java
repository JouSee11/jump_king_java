package cz.cvut.fel.pjv.talacjos.jump_up.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.cvut.fel.pjv.talacjos.jump_up.Constants;

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
                List<Key> keys = getKeyList(keysArray);

                level.setPlatforms(platforms);
                level.setKeys(keys);
                levels.put(level.getId(), level);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    private static List<Key> getKeyList(JsonArray keysArray) {
        List<Key> keys = new ArrayList<Key>();

        //go through all keys for the level
        for (JsonElement keyElement : keysArray) {
            JsonObject keyObj = keyElement.getAsJsonObject();

            int x = keyObj.get("x").getAsInt();
            int y = keyObj.get("y").getAsInt();
            int id = keyObj.get("id").getAsInt();


            Key key = new Key(x, y, id);
            keys.add(key);
        }
        return keys;
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
}
