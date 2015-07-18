package testing;

import static org.junit.Assert.assertTrue;
import logic.Bus;
import logic.Game;

import org.junit.Test;

public class TestingMultiBusLogic {
	@Test
	public void basicMultiTest() {
		Game g = Game.ALL_GAMES.get("MultiTest").clone();
		assertTrue(!g.outputBusAtDir(1).get(0) && !g.outputBusAtDir(1).get(1));
		g.toggle(0, 1);
		assertTrue(g.outputBusAtDir(1).get(0) && !g.outputBusAtDir(1).get(1));
		g.toggle(0, 2);
		assertTrue(g.outputBusAtDir(1).get(0) && g.outputBusAtDir(1).get(1));
	}

	@Test
	public void randomBusTest() {
		Bus b = new Bus(3);
		Bus c = b;
		c.add(true);
		b.add(true);
	}
}
