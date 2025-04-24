package cz.cvut.fel.pjv.talacjos.jump_up.model;

import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.End;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.Platform;
import cz.cvut.fel.pjv.talacjos.jump_up.model.world_items.PlatformTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelTest {
   Level level;

   @BeforeEach
   void setUp() {
       level = new Level();
       level.setId(1);
   }

    @Test
    public void levelTestId() {
       Assertions.assertEquals(1, level.getId());
   }


}
