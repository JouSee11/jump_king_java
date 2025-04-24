package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Platform;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.PlatformTypes;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class PlatformTest {
//    @BeforeAll
//    static void initJFX() {
//        new JFXPanel(); // Inicializace JavaFX bez GUI
//    }

    @BeforeAll
    static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void testPlatformConstructor() {
        Platform platform = new Platform(50, 100, 200, 20, PlatformTypes.CLOUD);

        assertEquals(PlatformTypes.CLOUD, platform.getType());
        assertEquals(50, platform.getX());
        assertEquals(100, platform.getY());
        assertEquals(200, platform.getWidth());
        assertEquals(20, platform.getHeight());

        assertNotNull(platform.getImage(), "Image should be loaded");
        assertEquals(Color.LIMEGREEN, platform.getBorderColor(), "Color should match GRASS color");
    }

    @Test
    void testSetTypeChangesImageAndColor() {
        Platform platform = new Platform(0, 0, 100, 10, PlatformTypes.CLOUD);
        Image originalImage = platform.getImage();
        platform.setType(PlatformTypes.ICE);

        assertEquals(PlatformTypes.ICE, platform.getType());
        assertEquals(Color.LIGHTBLUE, platform.getBorderColor());
        assertNotNull(platform.getImage());
        assertNotSame(originalImage, platform.getImage(), "Image should change after type is changed");
    }
}
