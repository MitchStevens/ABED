package testing;

import static org.junit.Assert.assertTrue;
import logic.Game;

import org.junit.Test;

import circuits.BusIn;

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
		BusIn b = new BusIn(3);
		BusIn c = b;
		c.add(true);
		b.add(true);
	}
}
