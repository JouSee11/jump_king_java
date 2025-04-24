package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.End;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LevelTest {
   Level level;

   @BeforeEach
   void setUp() {
       level = new Level();
       level.setId(1);
   }

    @Test
    public void levelTest() {
       Assertions.assertEquals(1, level.getId());
   }
}
