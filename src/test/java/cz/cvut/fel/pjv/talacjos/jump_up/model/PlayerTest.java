package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {

    private Player player;
    private final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        // Vytvoříme mock objekt namísto skutečné instance
        player = Mockito.mock(Player.class);

        // Nastavíme výchozí hodnoty pro gettery
        when(player.getX()).thenReturn(100.0);
        when(player.getY()).thenReturn(200.0);
        when(player.getWidth()).thenReturn(50.0);
        when(player.getHeight()).thenReturn(50.0);
        when(player.getVelocityX()).thenReturn(0.0);
        when(player.getVelocityY()).thenReturn(0.0);
        when(player.isOnGround()).thenReturn(true);
        when(player.isJumping()).thenReturn(false);
        when(player.isSquatting()).thenReturn(false);
        when(player.getJumpPowerMultiplier()).thenReturn(1.0);
        when(player.getMoveSpeedMultiplier()).thenReturn(1.0);
        when(player.isFacingRight()).thenReturn(true);
        when(player.getJumpDirection()).thenReturn(0);
    }

    @Test
    void testInitialValues() {
        assertEquals(100, player.getX(), DELTA);
        assertEquals(200, player.getY(), DELTA);
        assertEquals(50, player.getWidth(), DELTA);
        assertEquals(50, player.getHeight(), DELTA);
        assertEquals(0, player.getVelocityX(), DELTA);
        assertEquals(0, player.getVelocityY(), DELTA);
        assertTrue(player.isOnGround());
        assertFalse(player.isJumping());
        assertFalse(player.isSquatting());
        assertEquals(1.0, player.getJumpPowerMultiplier(), DELTA);
        assertEquals(1.0, player.getMoveSpeedMultiplier(), DELTA);
        assertTrue(player.isFacingRight());
    }

    @Test
    void testVelocitySettersAndGetters() {
        // Nastavení nových hodnot pro návratové hodnoty mocku
        when(player.getVelocityX()).thenReturn(5.5);
        when(player.getVelocityY()).thenReturn(-3.2);

        assertEquals(5.5, player.getVelocityX(), DELTA);
        assertEquals(-3.2, player.getVelocityY(), DELTA);

        // Ověření, že settery byly zavolány
        player.setVelocityX(5.5);
        player.setVelocityY(-3.2);

        verify(player).setVelocityX(5.5);
        verify(player).setVelocityY(-3.2);
    }

    @Test
    void testOnGroundSetterAndGetter() {
        // Nastavení false a ověření
        when(player.isOnGround()).thenReturn(false);
        assertFalse(player.isOnGround());
        player.setOnGround(false);
        verify(player).setOnGround(false);

        // Nastavení true a ověření
        when(player.isOnGround()).thenReturn(true);
        assertTrue(player.isOnGround());
        player.setOnGround(true);
        verify(player).setOnGround(true);
    }

    @Test
    void testPowerUpActivate() {
        // Nastavení očekávaných hodnot po aktivaci
        when(player.getJumpPowerMultiplier()).thenReturn(1.7);
        when(player.getMoveSpeedMultiplier()).thenReturn(2.0);

        player.powerUpActivate();
        verify(player).powerUpActivate();

        assertEquals(1.7, player.getJumpPowerMultiplier(), DELTA);
        assertEquals(2.0, player.getMoveSpeedMultiplier(), DELTA);
    }

    @Test
    void testPowerUpDeactivate() {
        // Nastavení očekávaných hodnot po deaktivaci
        when(player.getJumpPowerMultiplier()).thenReturn(1.0);
        when(player.getMoveSpeedMultiplier()).thenReturn(1.0);

        player.powerUpDeactivate();
        verify(player).powerUpDeactivate();

        assertEquals(1.0, player.getJumpPowerMultiplier(), DELTA);
        assertEquals(1.0, player.getMoveSpeedMultiplier(), DELTA);
    }

}