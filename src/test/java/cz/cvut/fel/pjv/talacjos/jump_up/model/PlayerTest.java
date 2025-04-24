//package cz.cvut.fel.pjv.talacjos.jump_up.model;
//
//import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Player;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//public class PlayerTest {
//    Player player;
//
//    @BeforeEach
//    public void setUp() {
//        player = Mockito.spy(new Player(10, 10, 50, 50));
//
//        Mockito.doNothing().when(player).loadAnimations();
//    }
//
//    @Test
//    public void testPlayerConstructor() {
//        Assertions.assertEquals(10, player.getX());
//    }
//}
