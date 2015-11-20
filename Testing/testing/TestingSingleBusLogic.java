package testing;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import circuits.BusIn;
import circuits.Circuit;
import circuits.Input;
import circuits.Output;
import eval.Evaluator;
import logic.Game;
import logic.Reader;

public class TestingSingleBusLogic {
	String[] evalTrue = new String[] { "0 ~", "0 1 |", "1 ~ ~", "1 1 &",
			"0 0 & ~ 0 0 & ~ &" };

	String[] evalFalse = new String[] { "1 ~", "0 0 |", "0 ~ ~", "1 0 &",
			"0 0 & ~ 1 1 & ~ &" };

	@Before
	public void init(){
		Reader.loadAll();
	}
	
	@Test
	public void gameTestNoRot() {
		// testing games without any rotations applied to the circuits
		Game g = new Game(3);
		Circuit not = Circuit.allCircuits.get("Not").clone();
		Circuit or = Circuit.allCircuits.get("Or").clone();
		g.add(or, 1, 0);
		assertFalse(or.flattenOutputs().get(0));
		g.add(not, 0, 0);
		assertTrue(not.flattenOutputs().get(0));
		assertTrue(or.flattenOutputs().get(0));
		// +-+-+-+-+-
		g.clear();
		not.uncoupleBuses();
		Circuit not2 = Circuit.allCircuits.get("Not").clone();
		g.add(not2, 1, 0);
		assertTrue(not2.flattenOutputs().get(0));
		g.add(not, 0, 0);
		assertTrue(not.flattenOutputs().get(0));
		assertFalse(not2.flattenOutputs().get(0));
	}

	@Test
	public void gameTestRot() {
		Game g = new Game(3);
		Circuit not = Circuit.allCircuits.get("Not").clone();
		not.addRot(1);
		Circuit or = Circuit.allCircuits.get("Or").clone();
		or.addRot(1);
		Circuit out = Circuit.allCircuits.get("Output").clone();
		out.addRot(1);
		g.add(or, 1, 1);
		assertFalse(or.flattenOutputs().get(0));
		g.add(not, 1, 0);
		g.add(out, 1, 2);
		g.printGame();
		assertTrue(((Output)g.circuitsOnEdge(2, "Output").get(0)).get());
		// +-+-+-+-+-
		g.clear();
		not.setRot(1);
		Circuit not2 = Circuit.allCircuits.get("Not").clone();
		not2.setRot(1);
		g.add(not2, 0, 1);
		assertTrue(not2.flattenOutputs().get(0));
		g.add(not, 0, 0);
		//assertFalse(not2.flattenOutputs().get(0));
		// +-+-+-+-+-
		g.clear();
		not.setRot(0);
		g.add(not, 1, 1);
		out.setRot(0);
		g.add(out, 2, 1);
		g.printGame();
		assertTrue(g.flattenOutputs().get(0));

		g.rotate(1, 1, 1);
		g.printGame();
		assertFalse(g.flattenOutputs().get(0));

		g.rotate(1, 1, 1);
		g.printGame();
		assertFalse(g.flattenOutputs().get(0));

		g.rotate(1, 1, 1);
		g.printGame();
		assertFalse(g.flattenOutputs().get(0));

		g.rotate(1, 1, 1);
		g.printGame();
		assertTrue(g.flattenOutputs().get(0));
		// +-+-+-+-+-
		g.clear();
		not.setRot(1);
		not2 = Circuit.allCircuits.get("Not").clone();
		not2.setRot(0);
		Circuit and = Circuit.allCircuits.get("And").clone();
		g.add(and, 1, 1);
		assertFalse(and.flattenOutputs().get(0));
		g.add(not, 1, 0);
		g.add(not2, 0, 1);
		assertTrue(and.flattenOutputs().get(0));
	}

	@Test
	public void gameFromString() {
		Game g = new Game(
				"name;3;Input,1,1,0;Input,0,0,1;And,0,1,1;Output,0,2,1;");
		assertFalse(g.flattenOutputs().get(0));
		g.toggle(0, 1);
		g.toggle(1, 0);
		assertTrue(g.flattenOutputs().get(0));

		Game xor = Game.ALL_GAMES.get("Xor");
		assertFalse(xor.flattenOutputs().get(0));
		xor.toggle(0, 2);
		assertTrue(xor.flattenOutputs().get(0));
		xor.toggle(2, 0);
		assertFalse(xor.flattenOutputs().get(0));
	}

	@Test
	public void inputCircuitTest() {
		Game g = new Game(3);
		Circuit input = new Input();
		g.add(input, 0, 0);
		assertFalse(g.circuitAtPos(0, 0).flattenOutputs().get(0));
		g.toggle(0, 0);
		assertTrue(g.circuitAtPos(0, 0).flattenOutputs().get(0));
	}

	@Test
	public void singleCircuitTest() {
		Circuit c = Circuit.allCircuits.get("Not");
		c.eval();
		assertTrue(c.flattenOutputs().get(0));
	}

	@Test
	public void printCircuitTest() {
		Circuit or = Circuit.allCircuits.get("Or");
		String[] str = or.printCircuit();
		for(int i = 0 ; i < 3; i++)
			System.out.println(str[i]);
		assertEquals(str[0], "  \\/  ");
		assertEquals(str[1], ">>OR>>");
		assertEquals(str[2], "      ");

		Circuit not = Circuit.allCircuits.get("Not");
		not.addRot(3);
		str = not.printCircuit();
		assertEquals(str[0], "  /\\  ");
		assertEquals(str[1], "  NO  ");
		assertEquals(str[2], "  /\\  ");
	}

	@Test
	public void evaluatorTest() {
		List<Boolean> list = Arrays.asList(new Boolean[] { false, true });
		for (String s : evalTrue)
			assertTrue(new Evaluator(s).eval(list));
		for (String s : evalFalse)
			assertFalse(new Evaluator(s).eval(list));
	}

	@Test
	public void moveTest() {
		Game g = new Game(3);
		g.add(Circuit.allCircuits.get("Input").clone(), 0, 1);
		g.add(Circuit.allCircuits.get("Bus").clone(), 1, 1);
		g.add(Circuit.allCircuits.get("Output").clone(), 2, 1);
		g.toggle(0, 1);
		assertTrue(g.flattenOutputs().get(0));

		g.move(1, 1, 1, 0);
		assertFalse(g.flattenOutputs().get(0));

		g.move(1, 0, 1, 1);
		assertTrue(g.flattenOutputs().get(0));
	}
//
//	@Test
//	public void generateBusTest() {
//		List<BusIn> buses = BusIn.generateBuses(4);
//		for (int i = 0; i < 8; i++)
//			for (int j = 0; j < 8; j++)
//				if (i != j)
//					assertFalse(buses.get(i).equals(buses.get(j)));
//				else
//					assertTrue(buses.get(i).equals(buses.get(j)));
//	}
//
//	@Test
//	public void generateBusRandomTest() {
//		Map<BusIn, Integer> map = new HashMap<>();
//		double tests = 1000000;
//		for (int i = 0; i < tests; i++) {
//			BusIn b = BusIn.randomBus(8);
//			if (map.containsKey(b))
//				map.replace(b, map.get(b) + 1);
//			else
//				map.put(b, 1);
//		}
//	}

}
