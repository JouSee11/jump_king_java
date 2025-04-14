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

/**
 * Utility class for loading game data from JSON files.
 * This class provides methods to load levels, player data, and other game-related information.
 */
public class JsonDataLoader {

    /**
     * Loads level data from a JSON file.
     *
     * @param filePath The path to the JSON file containing level data.
     * @return A HashMap where the key is the level ID and the value is the corresponding Level object.
     */
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

    /**
     * Helper method to load a list of collectable items (keys or power-ups) from a JSON array.
     *
     * @param collectableArray The JSON array containing collectable data.
     * @param type The type of collectable ("key" or "powerUp").
     * @param <T> The type of the collectable (Key or PowerUp).
     * @return A list of collectable items.
     */
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

    /**
     * Helper method to load a list of platforms from a JSON array.
     *
     * @param platformsArray The JSON array containing platform data.
     * @return A list of Platform objects.
     */
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

    /**
     * Loads player starting data from a JSON file.
     *
     * @param filePath The path to the JSON file containing player data.
     * @return A Player object initialized with the starting position and dimensions.
     */
    public static Player loadPlayerJson(String filePath) {
        Gson gson = new Gson();
        Player player = null;

        try (FileReader reader = new FileReader(filePath)){
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonObject playerObject = jsonObject.getAsJsonObject("playerStart");

            double x = playerObject.get("x").getAsDouble();
            double y = playerObject.get("y").getAsDouble();

            player = new Player(x, Constants.GAME_HEIGHT - y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        } catch (IOException e){
            e.printStackTrace();
        }

        return player;
    }

    /**
     * Loads key statistics from a JSON file.
     *
     * @param filePath The path to the JSON file containing key statistics.
     * @return A list of integers where the first element is the total count of keys, and the rest are collected key IDs.
     */
    public static List<Integer> loadKeysStatsJson(String filePath) {
        Gson gson = new Gson();

        List<Integer> keysStats = new ArrayList<Integer>();

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonObject keysStatsObject = jsonObject.getAsJsonObject("keysStats");

            //get all the collected keys indexes
            JsonArray collectedArray = keysStatsObject.getAsJsonArray("collected");
            for (JsonElement element : collectedArray) {
                keysStats.add(element.getAsInt());
            }
            keysStats.addFirst(keysStatsObject.get("allCount").getAsInt());

            return keysStats;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to create an End object from a JSON object.
     *
     * @param endObj The JSON object containing end data.
     * @return An End object initialized with the provided data.
     */
    private static End getEndElement(JsonObject endObj) {
        int x = endObj.get("x").getAsInt();
        int y = endObj.get("y").getAsInt();

        return new End(x, Constants.GAME_HEIGHT - y);
    }

    /**
     * Loads level statistics from a JSON file.
     *
     * @param filePath The path to the JSON file containing level statistics.
     * @return An array of integers where:
     *         - The first element represents the total number of levels.
     *         - The second element represents the starting level.
     */
    public static int[] loadLevelStatsJson(String filePath) {
        Gson gson = new Gson();

        int[] levelStats = new int[2];

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonObject levelsStatsObject = jsonObject.getAsJsonObject("levelStats");

            //get all the collected keys indexes
            JsonElement levelCountElement = levelsStatsObject.getAsJsonPrimitive("levelCount");
            JsonElement startingLevelElement = levelsStatsObject.getAsJsonPrimitive("startingLevel");
            levelStats[0] = levelCountElement != null ? levelCountElement.getAsInt() : 1;;
            levelStats[1] = startingLevelElement != null ? startingLevelElement.getAsInt() : 1;

            return levelStats;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Loads the name of the map from a save file.
     *
     * @param filePath The path to the JSON save file containing the map name.
     * @return A string representing the name of the map. Returns an empty string if an error occurs.
     */
    public static String loadMapNameFromSave(String filePath) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)){
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            return jsonObject.getAsJsonPrimitive("mapName").getAsString();

        } catch (IOException e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Loads player data from a save file.
     *
     * @param filePath The path to the JSON save file containing player data.
     * @return A HashMap where the keys are strings representing player attributes (e.g., "playerX", "playerY"),
     *         and the values are doubles representing the corresponding attribute values.
     */
    public static HashMap<String, Double> loadPlayerDataFromSave(String filePath) {
        Gson gson = new Gson();
        HashMap<String, Double> playerData = new HashMap<String, Double>();

        try (FileReader reader = new FileReader(filePath)){
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            Double curLevel = jsonObject.getAsJsonPrimitive("level").getAsDouble();
            Double playerX = jsonObject.getAsJsonPrimitive("playerX").getAsDouble();
            Double playerY = jsonObject.getAsJsonPrimitive("playerY").getAsDouble();
            Double playerVelocityX = jsonObject.getAsJsonPrimitive("playerVelocityX").getAsDouble();
            Double playerVelocityY = jsonObject.getAsJsonPrimitive("playerVelocityY").getAsDouble();
            boolean powerUpActive = jsonObject.getAsJsonPrimitive("powerUpActive").getAsBoolean();
            Double powerUpTimeRemaining = jsonObject.getAsJsonPrimitive("powerUpTimeRemaining").getAsDouble();

            playerData.put("playerX", playerX);
            playerData.put("playerY", playerY);
            playerData.put("curLevel", curLevel);
            playerData.put("velocityX", playerVelocityX);
            playerData.put("velocityY", playerVelocityY);
            playerData.put("isPowerUpActive", powerUpActive ? 1.0 : 0.0);
            playerData.put("powerUpTimeRemaining", powerUpTimeRemaining);



        } catch (IOException e){
            e.printStackTrace();
        }

        return playerData;
    }


    /**
     * Loads the list of collected keys from a save file.
     *
     * @param filePath The path to the JSON save file containing collected keys data.
     * @return A list of integers representing the IDs of collected keys. Returns null if an error occurs.
     */
    public static List<Integer> loadCollectedKeysFromSave(String filePath) {
        Gson gson = new Gson();

        List<Integer> collectedKeys = new ArrayList<Integer>();

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray collectArray = jsonObject.getAsJsonArray("collectedKeys");

            for (JsonElement element : collectArray) {
                collectedKeys.add(element.getAsInt());
            }

            return collectedKeys;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads the list of collected power-ups from a save file.
     *
     * @param filePath The path to the JSON save file containing collected power-ups data.
     * @return A list of integers representing the IDs of collected power-ups. Returns null if an error occurs.
     */
    public static List<Integer> loadCollectedPowerUpsFromSave(String filePath) {
        Gson gson = new Gson();

        List<Integer> collectedKeys = new ArrayList<Integer>();

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray collectArray = jsonObject.getAsJsonArray("collectedPowerUps");

            for (JsonElement element : collectArray) {
                collectedKeys.add(element.getAsInt());
            }

            return collectedKeys;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a JSON array from a list of integers.
     *
     * @param itemsList A list of integers to be converted into a JSON array.
     * @return A JsonArray containing the integers from the provided list.
     */
    public static JsonArray createJsonArrayFromIntList(List<Integer> itemsList) {
        JsonArray collectedList = new JsonArray();
        for(Integer itemId : itemsList) {
            collectedList.add(itemId);
        }
        return collectedList;
    }

}
