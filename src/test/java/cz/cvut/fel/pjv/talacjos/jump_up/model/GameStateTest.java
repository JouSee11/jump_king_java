package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameStateTest {
    GameState gameState;

    @BeforeEach
    public void init() {
        GameController gameControllerMock = Mockito.mock(GameController.class);

        gameState = new GameState(gameControllerMock, "test", false);
    }

//    @Test
//    public void
}
