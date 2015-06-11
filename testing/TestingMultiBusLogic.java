package testing;

import static org.junit.Assert.assertTrue;
import logic.Game;

import org.junit.Test;

public class TestingMultiBusLogic {
	@Test
	public void basicMultiTest(){
		Game g = Game.loadedGames.get("MultiTest");
		assertTrue(!g.outputsAtDir(1).get(0) && !g.outputsAtDir(1).get(1));
		g.tileGrid[0][0].toggle();
		assertTrue(g.outputsAtDir(1).get(0) && !g.outputsAtDir(1).get(1));
		g.tileGrid[0][1].toggle();
		assertTrue(g.outputsAtDir(1).get(0) && g.outputsAtDir(1).get(1));
	}
}
