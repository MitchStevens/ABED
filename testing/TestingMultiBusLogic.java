package testing;

import static org.junit.Assert.assertTrue;
import logic.Bus;
import logic.Game;

import org.junit.Test;

public class TestingMultiBusLogic {
	@Test
	public void basicMultiTest(){
		Game g = Game.loadedGames.get("MultiTest");
		assertTrue(!g.outputsAtDir(1).get(0) && !g.outputsAtDir(1).get(1));
		g.toggle(0, 0);
		assertTrue(g.outputsAtDir(1).get(0) && !g.outputsAtDir(1).get(1));
		g.toggle(0, 1);
		assertTrue(g.outputsAtDir(1).get(0) && g.outputsAtDir(1).get(1));
	}
	
	@Test
	public void randomBusTest(){
		Bus b = new Bus(3);
		Bus c = b;
		c.add(true);
		System.out.println(b+" / "+c);
		b.add(true);
		System.out.println(b+" / "+c);
	}
}
