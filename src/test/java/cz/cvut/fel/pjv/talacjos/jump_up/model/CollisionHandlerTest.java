package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.Constants;
import cz.cvut.fel.pjv.talacjos.jump_up.GameLogger;
import cz.cvut.fel.pjv.talacjos.jump_up.controller.SoundController;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.*;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollisionHandlerTest {

    private CollisionHandler collisionHandler;
    private GameState gameStateMock;
    private Player playerMock;
    private Platform platformMock;
    private Key keyMock;
    private PowerUp powerUpMock;
    private Bounds playerBounds;
    private Bounds platformBounds;

    @BeforeEach
    public void setUp() {
        gameStateMock = mock(GameState.class);
        playerMock = mock(Player.class, withSettings().lenient());  // Přidáno lenient nastavení
        platformMock = mock(Platform.class, withSettings().lenient());
        keyMock = mock(Key.class, withSettings().lenient());
        powerUpMock = mock(PowerUp.class, withSettings().lenient());

        playerBounds = mock(Bounds.class);
        platformBounds = mock(Bounds.class);

        // Základní nastavení pro všechny testy
        when(playerMock.getBoundsInParent()).thenReturn(playerBounds);
        when(platformMock.getBoundsInParent()).thenReturn(platformBounds);
        when(gameStateMock.getPlayer()).thenReturn(playerMock);

        // Výchozí hodnoty pro rozměry a pozice
        when(playerMock.getHeight()).thenReturn(20.0);
        when(playerMock.getWidth()).thenReturn(20.0);

        collisionHandler = new CollisionHandler(gameStateMock);
    }

    @Test
    public void testPlayerOnGround() {
        // Nastaví pozici hráče na spodní část obrazovky v úrovni 1
        when(gameStateMock.getCurLevel()).thenReturn(1);
        when(playerMock.getY()).thenReturn((double) Constants.GAME_HEIGHT - 10);

        // Test pro hráče na zemi
        try (MockedStatic<SoundController> soundMock = mockStatic(SoundController.class);
             MockedStatic<GameLogger> loggerMock = mockStatic(GameLogger.class)) {

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            GameLogger loggerMockInstance = mock(GameLogger.class);
            loggerMock.when(GameLogger::getInstance).thenReturn(loggerMockInstance);

            when(gameStateMock.getPlatformList()).thenReturn(new ArrayList<>());
            when(gameStateMock.getKeyList()).thenReturn(new ArrayList<>());
            when(gameStateMock.getCurPowerupList()).thenReturn(new ArrayList<>());

            ArgumentCaptor<Double> yPositionCaptor = ArgumentCaptor.forClass(Double.class);

            collisionHandler.handleCollisions();

            // Ověří, že hráč je nastaven na zem
            verify(playerMock).setY(yPositionCaptor.capture());
            assertEquals(Constants.GAME_HEIGHT - playerMock.getHeight(), yPositionCaptor.getValue(), 0.001);

            verify(playerMock).setVelocityY(0);
            verify(playerMock).setOnGround(true);
            verify(playerMock).setBounced(false);
        }
    }

    @Test
    public void testPlayerPlatformCollisionTop() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(platformMock);

        when(gameStateMock.getPlatformList()).thenReturn(platforms);
        when(gameStateMock.getKeyList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getCurPowerupList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getCurLevel()).thenReturn(1);

        // Nastaví kolizi
        when(playerBounds.intersects(platformBounds)).thenReturn(true);

        // Nastaví pozici a rozměry objektů tak, aby šlo o kolizi shora
        when(playerMock.getY()).thenReturn(80.0);
        when(platformMock.getY()).thenReturn(100.0);
        when(platformMock.getHeight()).thenReturn(20.0);
        when(playerMock.getX()).thenReturn(50.0);
        when(platformMock.getX()).thenReturn(40.0);
        when(platformMock.getWidth()).thenReturn(40.0);
        when(playerMock.getVelocityY()).thenReturn(5.0); // Přidáno - rychlost směrem dolů

        try (MockedStatic<SoundController> soundMock = mockStatic(SoundController.class);
             MockedStatic<GameLogger> loggerMock = mockStatic(GameLogger.class)) {

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            GameLogger loggerMockInstance = mock(GameLogger.class);
            loggerMock.when(GameLogger::getInstance).thenReturn(loggerMockInstance);

            // Použití ArgumentCaptor pro zachycení argumentu předaného setY
            ArgumentCaptor<Double> yPositionCaptor = ArgumentCaptor.forClass(Double.class);

            collisionHandler.handleCollisions();

            // Ověření, že setY byla volána s očekávanou hodnotou
            verify(playerMock).setY(yPositionCaptor.capture());
            assertEquals(platformMock.getY() - playerMock.getHeight(), yPositionCaptor.getValue(), 0.001);

            verify(playerMock).setOnGround(true);
            verify(playerMock).setBounced(false);
            verify(playerMock).setVelocityY(0);
        }
    }

    @Test
    public void testKeyCollection() {
        // Test sběru klíče
        List<Key> keys = new ArrayList<>();
        keys.add(keyMock);

        when(gameStateMock.getPlatformList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getKeyList()).thenReturn(keys);
        when(gameStateMock.getCurPowerupList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getCurLevel()).thenReturn(1);

        // Nastaví kolizi s klíčem
        when(keyMock.getBoundsInParent()).thenReturn(platformBounds);
        when(playerBounds.intersects(platformBounds)).thenReturn(true);

        try (MockedStatic<SoundController> soundMock = mockStatic(SoundController.class);
             MockedStatic<GameLogger> loggerMock = mockStatic(GameLogger.class)) {

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            GameLogger loggerMockInstance = mock(GameLogger.class);
            loggerMock.when(GameLogger::getInstance).thenReturn(loggerMockInstance);

            collisionHandler.handleCollisions();

            // Ověřit, že metoda pro sbírání klíče byla zavolána
            verify(gameStateMock).keyCollected(keyMock);
        }
    }

    @Test
    public void testPowerUpCollection() {
        // Test sběru power-upu
        List<PowerUp> powerUps = new ArrayList<>();
        powerUps.add(powerUpMock);

        when(gameStateMock.getPlatformList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getKeyList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getCurPowerupList()).thenReturn(powerUps);
        when(gameStateMock.getCurLevel()).thenReturn(1);

        // Nastaví kolizi s power-upem
        when(powerUpMock.getBoundsInParent()).thenReturn(platformBounds);
        when(playerBounds.intersects(platformBounds)).thenReturn(true);

        try (MockedStatic<SoundController> soundMock = mockStatic(SoundController.class);
             MockedStatic<GameLogger> loggerMock = mockStatic(GameLogger.class)) {

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            GameLogger loggerMockInstance = mock(GameLogger.class);
            loggerMock.when(GameLogger::getInstance).thenReturn(loggerMockInstance);

            collisionHandler.handleCollisions();

            // Ověřit, že metoda pro sbírání power-upu byla zavolána
            verify(gameStateMock).powerUpCollected(powerUpMock);
        }
    }

    @Test
    public void testLevelChangeUp() {
        // Test přechodu na vyšší úroveň
        when(gameStateMock.getPlatformList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getKeyList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getCurPowerupList()).thenReturn(new ArrayList<>());
        when(gameStateMock.getEnd()).thenReturn(null); // Přidáno

        // Hráč je nad obrazovkou a není na nejvyšší úrovni
        when(playerMock.getY()).thenReturn(-30.0); // Upraveno, možná potřeba větší zápornou hodnotu
        when(gameStateMock.getCurLevel()).thenReturn(1);
        when(gameStateMock.getMaxLevel()).thenReturn(2);
        when(playerMock.isOnGround()).thenReturn(false); // Přidáno

        try (MockedStatic<SoundController> soundMock = mockStatic(SoundController.class);
             MockedStatic<GameLogger> loggerMock = mockStatic(GameLogger.class)) {

            SoundController soundCtrlMock = mock(SoundController.class);
            soundMock.when(SoundController::getInstance).thenReturn(soundCtrlMock);

            GameLogger loggerMockInstance = mock(GameLogger.class);
            loggerMock.when(GameLogger::getInstance).thenReturn(loggerMockInstance);

            collisionHandler.handleCollisions();

            // Ověřit přechod na vyšší úroveň
            verify(gameStateMock).setCurLevel(2);
        }
    }
}