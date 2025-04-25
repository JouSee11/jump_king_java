package cz.cvut.fel.pjv.talacjos.jump_up.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.End;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonDataLoaderTest {

    @TempDir
    Path tempDir;

    private File keysStatsFile;
    private File levelStatsFile;
    private File saveFile;

    @BeforeEach
    void setUp() throws IOException {
        // Vytvoření dočasných JSON souborů pro testování
        keysStatsFile = tempDir.resolve("keysStats.json").toFile();
        levelStatsFile = tempDir.resolve("levelStats.json").toFile();
        saveFile = tempDir.resolve("save.json").toFile();

        // Připravíme obsah souboru pro klíče
        try (FileWriter writer = new FileWriter(keysStatsFile)) {
            writer.write("{ \"keysStats\": { \"allCount\": 5, \"collected\": [1, 2, 4] } }");
        }

        // Připravíme obsah souboru pro úrovně
        try (FileWriter writer = new FileWriter(levelStatsFile)) {
            writer.write("{ \"levelStats\": { \"levelCount\": 10, \"startingLevel\": 3 } }");
        }

        // Připravíme obsah souboru pro uložení hry
        try (FileWriter writer = new FileWriter(saveFile)) {
            writer.write("{ " +
                    "\"mapName\": \"testMap\", " +
                    "\"level\": 3.0, " +
                    "\"playerX\": 150.5, " +
                    "\"playerY\": 250.5, " +
                    "\"playerVelocityX\": 1.5, " +
                    "\"playerVelocityY\": -2.5, " +
                    "\"powerUpActive\": true, " +
                    "\"powerUpTimeRemaining\": 5.5, " +
                    "\"collectedKeys\": [1, 3, 5], " +
                    "\"collectedPowerUps\": [2, 4] " +
                    "}");
        }
    }

    @Test
    void testLoadKeysStatsJson() {
        List<Integer> keysStats = JsonDataLoader.loadKeysStatsJson(keysStatsFile.getAbsolutePath());

        assertNotNull(keysStats);
        assertEquals(4, keysStats.size());
        assertEquals(5, keysStats.get(0)); // allCount
        assertEquals(Arrays.asList(5, 1, 2, 4), keysStats);
    }

    @Test
    void testLoadLevelStatsJson() {
        int[] levelStats = JsonDataLoader.loadLevelStatsJson(levelStatsFile.getAbsolutePath());

        assertNotNull(levelStats);
        assertEquals(2, levelStats.length);
        assertEquals(10, levelStats[0]); // levelCount
        assertEquals(3, levelStats[1]); // startingLevel
    }

    @Test
    void testLoadMapNameFromSave() {
        String mapName = JsonDataLoader.loadMapNameFromSave(saveFile.getAbsolutePath());

        assertEquals("testMap", mapName);
    }

    @Test
    void testLoadPlayerDataFromSave() {
        HashMap<String, Double> playerData = JsonDataLoader.loadPlayerDataFromSave(saveFile.getAbsolutePath());

        assertNotNull(playerData);
        assertEquals(150.5, playerData.get("playerX"), 0.001);
        assertEquals(250.5, playerData.get("playerY"), 0.001);
        assertEquals(3.0, playerData.get("curLevel"), 0.001);
        assertEquals(1.5, playerData.get("velocityX"), 0.001);
        assertEquals(-2.5, playerData.get("velocityY"), 0.001);
        assertEquals(1.0, playerData.get("isPowerUpActive"), 0.001);
        assertEquals(5.5, playerData.get("powerUpTimeRemaining"), 0.001);
    }

    @Test
    void testLoadCollectedKeysFromSave() {
        List<Integer> collectedKeys = JsonDataLoader.loadCollectedKeysFromSave(saveFile.getAbsolutePath());

        assertNotNull(collectedKeys);
        assertEquals(3, collectedKeys.size());
        assertTrue(collectedKeys.contains(1));
        assertTrue(collectedKeys.contains(3));
        assertTrue(collectedKeys.contains(5));
    }

    @Test
    void testLoadCollectedPowerUpsFromSave() {
        List<Integer> collectedPowerUps = JsonDataLoader.loadCollectedPowerUpsFromSave(saveFile.getAbsolutePath());

        assertNotNull(collectedPowerUps);
        assertEquals(2, collectedPowerUps.size());
        assertTrue(collectedPowerUps.contains(2));
        assertTrue(collectedPowerUps.contains(4));
    }

    @Test
    void testCreateJsonArrayFromIntList() {
        List<Integer> items = Arrays.asList(1, 3, 5);
        JsonArray jsonArray = JsonDataLoader.createJsonArrayFromIntList(items);

        assertNotNull(jsonArray);
        assertEquals(3, jsonArray.size());
        assertEquals(1, jsonArray.get(0).getAsInt());
        assertEquals(3, jsonArray.get(1).getAsInt());
        assertEquals(5, jsonArray.get(2).getAsInt());
    }

    @Test
    void testFileNotFoundReturnsNull() {
        // Test, že při neexistujícím souboru metody vrací null
        assertNull(JsonDataLoader.loadKeysStatsJson("neexistující_soubor.json"));
        assertNull(JsonDataLoader.loadLevelStatsJson("neexistující_soubor.json"));
        assertNull(JsonDataLoader.loadCollectedKeysFromSave("neexistující_soubor.json"));
        assertNull(JsonDataLoader.loadCollectedPowerUpsFromSave("neexistující_soubor.json"));
    }

    @Test
    void testFileNotFoundReturnsEmptyString() {
        // Test, že při neexistujícím souboru metoda vrací prázdný řetězec
        assertEquals("", JsonDataLoader.loadMapNameFromSave("neexistující_soubor.json"));
    }

    @Test
    void testMalformedJsonThrowsException() throws IOException {
        // Vytvoření souboru s chybným JSON obsahem
        File malformedFile = tempDir.resolve("malformed.json").toFile();
        try (FileWriter writer = new FileWriter(malformedFile)) {
            writer.write("{ toto není platný JSON }");
        }

        // Očekáváme výjimku místo null hodnoty
        assertThrows(JsonSyntaxException.class, () -> {
            JsonDataLoader.loadKeysStatsJson(malformedFile.getAbsolutePath());
        });

        assertThrows(JsonSyntaxException.class, () -> {
            JsonDataLoader.loadLevelStatsJson(malformedFile.getAbsolutePath());
        });
    }
}