package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The FileSaveRead class provides functionality for saving game data to a file.
 */
public class FileSaveRead {

    /**
     * Saves the game data to a file in JSON format.
     * The file is saved in the 'src/main/resources/saves' directory with a name
     * that includes the provided file name, the current date and time, and the map name.
     *
     * @param fileName The base name of the file to save.
     * @param jsonData The game data in JSON format to be written to the file.
     * @param mapName  The name of the map associated with the save file.
     */
    public static void saveGameToFile(String fileName, String jsonData, String mapName) {

        try{
            File savesDir = new File("saves");
            if (!savesDir.exists()) {
                savesDir.mkdir();
            }
            //create the file
            String dateNow = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
            String saveFileName = fileName + "_" + dateNow + "_" + mapName + ".json";
            File saveFile = new File(savesDir, saveFileName);

            // write data to the file
            try (FileWriter writer = new FileWriter(saveFile)) {
                writer.write(jsonData);
            }

        } catch (IOException e) {
            GameLogger.getInstance().severe("Failed to save the game: " + e.getMessage());
        }


    }

}
