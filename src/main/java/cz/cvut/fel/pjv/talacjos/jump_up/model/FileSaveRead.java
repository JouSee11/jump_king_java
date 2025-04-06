package cz.cvut.fel.pjv.talacjos.jump_up.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSaveRead {

    public static void saveGameToFile(String fileName, String jsonData, String mapName) {

        try{
            File savesDir = new File("src/main/resources/saves");
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
            System.err.println(e);
            e.printStackTrace();
        }


    }

}
