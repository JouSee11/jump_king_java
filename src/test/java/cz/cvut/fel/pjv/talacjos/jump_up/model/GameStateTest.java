package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SoundController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GameStateTest {
    private GameState gameState;
    private GameController gameControllerMock;
    private Player playerMock;
    private HashMap<Integer, Level> levelsDataMapMock;
    private Level levelMock;

    @BeforeEach
    public void init() {
        gameControllerMock = Mockito.mock(GameController.class);
        playerMock = Mockito.mock(Player.class);

        // mock static methods JsonDataLoader
        try (MockedStatic<JsonDataLoader> jsonDataLoaderMock = mockStatic(JsonDataLoader.class)) {
            levelsDataMapMock = new HashMap<>();
            levelMock = Mockito.mock(Level.class);
            levelsDataMapMock.put(1, levelMock);

            jsonDataLoaderMock.when(() -> JsonDataLoader.loadLevelsJson(anyString())).thenReturn(levelsDataMapMock);
            jsonDataLoaderMock.when(() -> JsonDataLoader.loadPlayerJson(anyString())).thenReturn(playerMock);

            List<Integer> keyStats = new ArrayList<>();
            keyStats.add(5); // all keys number
            jsonDataLoaderMock.when(() -> JsonDataLoader.loadKeysStatsJson(anyString())).thenReturn(keyStats);

            int[] levelsData = {3, 1}; // maxLevel, curLevel
            jsonDataLoaderMock.when(() -> JsonDataLoader.loadLevelStatsJson(anyString())).thenReturn(levelsData);

            // setup mock data for level
            when(levelMock.getPlatforms()).thenReturn(new ArrayList<>());
            when(levelMock.getKeys()).thenReturn(new ArrayList<>());
            when(levelMock.getPowerUps()).thenReturn(new ArrayList<>());

            // create game state instance with mocks
            gameState = new GameState(gameControllerMock, "test", false);
        }
    }

    @Test
    public void testInitialization() {
        assertEquals(1, gameState.getCurLevel());
        assertEquals(3, gameState.getMaxLevel());
        assertEquals(0, gameState.getCollectedKeys());
        assertEquals(5, gameState.getAllKeys());
        assertFalse(gameState.isPowerUpActive());
    }

    @Test
    public void testPlayerJumpMechanics() {
        // setup player state before jump
        when(playerMock.isOnGround()).thenReturn(true);

        // test jump prepare
        gameState.prepareJump();

        verify(playerMock).setVelocityX(0);
        verify(playerMock).setSquatting(true);

        //test jump
        try (MockedStatic<GameLogger> gameLoggerMock = mockStatic(GameLogger.class)) {
            GameLogger loggerMock = mock(GameLogger.class);
            gameLoggerMock.when(GameLogger::getInstance).thenReturn(loggerMock);

            gameState.playerJumpExecute();

            verify(playerMock).setVelocityY(anyDouble());
            verify(playerMock).setOnGround(false);
            verify(playerMock).setSquatting(false);
            verify(playerMock).setJumping(true);
        }
    }

    @Test
    public void testKeyCollected() {
        // create key
        Key keyMock = Mockito.mock(Key.class);
        when(keyMock.getKeyId()).thenReturn(1);

        List<Key> keyList = new ArrayList<>();
        keyList.add(keyMock);
        when(levelMock.getKeys()).thenReturn(keyList);

        // Reset GameState
        gameState = new GameState(gameControllerMock, "test", false);

        // initialize collectedKey for game state
        try {
            // Inicializovat collectedKeys
            Field collectedKeysField = GameState.class.getDeclaredField("collectedKeys");
            collectedKeysField.setAccessible(true);
            collectedKeysField.set(gameState, new ArrayList<Integer>());

            // Zajistit, že máme správnou hodnotu v levelsDataMap
            Field levelsDataMapField = GameState.class.getDeclaredField("levelsDataMap");
            levelsDataMapField.setAccessible(true);

            HashMap<Integer, Level> map = new HashMap<>();
            map.put(1, levelMock); // 1 je hodnota curLevel
            levelsDataMapField.set(gameState, map);

            // Nastavit aktuální úroveň
            Field curLevelField = GameState.class.getDeclaredField("curLevel");
            curLevelField.setAccessible(true);
            curLevelField.set(gameState, 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Nepovedlo se nastavit testovací prostředí");
        }

        // Test key collect
        try (MockedStatic<GameLogger> gameLoggerMock = mockStatic(GameLogger.class)) {
            GameLogger loggerMock = mock(GameLogger.class);
            gameLoggerMock.when(GameLogger::getInstance).thenReturn(loggerMock);

            gameState.keyCollected(keyMock);

            assertEquals(1, gameState.getCollectedKeysList().size());
            assertTrue(gameState.getCollectedKeysList().contains(1));
            assertEquals(0, gameState.getKeyList().size());
        }
    }

    @Test
    public void testPowerUpCollected() {
        // Vytvoření power-upu
        PowerUp powerUpMock = Mockito.mock(PowerUp.class);
        when(powerUpMock.getPowerUpId()).thenReturn(1);

        List<PowerUp> powerUpList = new ArrayList<>();
        powerUpList.add(powerUpMock);
        when(levelMock.getPowerUps()).thenReturn(powerUpList);

        // Reset GameState pro nové mockované hodnoty
        gameState = new GameState(gameControllerMock, "test", false);


        // Inicializace potřebných polí pomocí reflexe
        try {
            // Inicializace collectedPowerUps
            Field collectedPowerUpsField = GameState.class.getDeclaredField("collectedPowerUps");
            collectedPowerUpsField.setAccessible(true);
            collectedPowerUpsField.set(gameState, new ArrayList<Integer>());

            // Zajištění správné hodnoty v levelsDataMap
            Field levelsDataMapField = GameState.class.getDeclaredField("levelsDataMap");
            levelsDataMapField.setAccessible(true);

            HashMap<Integer, Level> map = new HashMap<>();
            map.put(1, levelMock);
            levelsDataMapField.set(gameState, map);

            // Nastavení aktuální úrovně
            Field curLevelField = GameState.class.getDeclaredField("curLevel");
            curLevelField.setAccessible(true);
            curLevelField.set(gameState, 1);

            // Nastavení player fieldu - toto řeší NPE chybu
            Field playerField = GameState.class.getDeclaredField("player");
            playerField.setAccessible(true);
            playerField.set(gameState, playerMock);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Nepovedlo se nastavit testovací prostředí");
        }


        // Test sbírání power-upu
        try (MockedStatic<GameLogger> gameLoggerMock = mockStatic(GameLogger.class);
             MockedStatic<SoundController> soundMock = mockStatic(SoundController.class)) {

            GameLogger loggerMock = mock(GameLogger.class);
            gameLoggerMock.when(GameLogger::getInstance).thenReturn(loggerMock);

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            gameState.powerUpCollected(powerUpMock);

            assertTrue(gameState.isPowerUpActive());
            assertEquals(Constants.POWERUP_DURATION, gameState.getPowerUpTimeRemaining());
            verify(playerMock).powerUpActivate();
        }
    }

    @Test
    public void testUpdateWithPlayerMovement() {
        when(playerMock.getX()).thenReturn(10.0);
        when(playerMock.getY()).thenReturn(10.0);
        when(playerMock.getVelocityX()).thenReturn(5.0);
        when(playerMock.getVelocityY()).thenReturn(0.0);
        when(playerMock.isJumping()).thenReturn(false);

        // Test update metody
        gameState.update(0.16); // přibližně 16ms (60fps)

        // Ověření, že pozice hráče byla aktualizována
        verify(playerMock).setX(10.0 + 5.0 * 0.16);
        verify(playerMock).setY(10.0);

        // Ověření gravitace
        verify(playerMock).setVelocityY(anyDouble());
    }

    @Test
    public void testSetCurLevel() {
        // Příprava mocku pro různé úrovně
        Level level2Mock = mock(Level.class);
        when(level2Mock.getPlatforms()).thenReturn(new ArrayList<>());
        when(level2Mock.getKeys()).thenReturn(new ArrayList<>());
        when(level2Mock.getPowerUps()).thenReturn(new ArrayList<>());
        End endMock = mock(End.class);
        when(level2Mock.getEnd()).thenReturn(endMock);

        levelsDataMapMock.put(2, level2Mock);

        // Nastavení nové úrovně
        gameState.setCurLevel(2);

        // Ověření, že úroveň byla změněna
        assertEquals(2, gameState.getCurLevel());
        assertEquals(endMock, gameState.getEnd());
    }
}