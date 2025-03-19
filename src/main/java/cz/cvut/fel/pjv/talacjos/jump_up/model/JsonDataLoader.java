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
                List<Platform> platforms = new ArrayList<Platform>();

                //go through all platforms
                for (JsonElement platformElement : platformsArray) {
                    JsonObject platformObj = platformElement.getAsJsonObject();

                    double x = platformObj.get("x").getAsDouble();
                    double y = platformObj.get("y").getAsDouble();
                    double width = platformObj.get("width").getAsDouble();
                    double height = platformObj.get("height").getAsDouble();
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

                level.setPlatforms(platforms);
                levels.put(level.getId(), level);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
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
}
