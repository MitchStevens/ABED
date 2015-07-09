package testing;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import logic.Bus;
import logic.Circuit;
import logic.Evaluator;
import logic.Game;
import logic.Input;

public class TestingSingleBusLogic {
	String[] evalTrue = new String[]{
			"0 ~",		"0 1 |",
			"1 ~ ~",	"1 1 &",
			"0 0 & ~ 0 0 & ~ &"
	};
	
	String[] evalFalse = new String[]{
			"1 ~",		"0 0 |",
			"0 ~ ~",	"1 0 &",
			"0 0 & ~ 1 1 & ~ &"
	};
	
	
	@Test
	public void gameTestNoRot(){
		//testing games without any rotations applied to the circuits
		Game g = new Game(3);
		Circuit not = Circuit.loadedCircuits.get("Not").clone();
		Circuit or = Circuit.loadedCircuits.get("Or").clone();
		g.add(or, 1, 0);
		assertFalse(or.outputList().get(0));
		g.add(not, 0, 0);
		assertTrue(or.outputList().get(0));
		//+-+-+-+-+-
		g.clear();
		Circuit not2 = Circuit.loadedCircuits.get("Not").clone();
		g.add(not2, 1, 0);
		assertTrue(not2.outputList().get(0));
		g.add(not, 0, 0);
		assertFalse(not2.outputList().get(0));
	}
	
	@Test
	public void gameTestRot(){
		Game g = new Game(3);
		Circuit not = Circuit.loadedCircuits.get("Not").clone();
		not.addRot(1);
		Circuit or = Circuit.loadedCircuits.get("Or").clone();
		or.addRot(1);
		Circuit out = Circuit.loadedCircuits.get("Output").clone();
		out.addRot(1);
		g.add(or, 1, 1);
		assertFalse(or.outputList().get(0));
		g.add(not, 1, 0);
		g.add(out, 1, 2);
		assertTrue(g.outputBusAtDir(2).get(0));
		//+-+-+-+-+-
		g.clear();
		not.setRot(1);
		Circuit not2 = Circuit.loadedCircuits.get("Not").clone();
		not2.setRot(1);
		g.add(not2, 0, 1);
		assertTrue(not2.outputList().get(0));
		g.add(not, 0, 0);
		assertTrue(!not2.outputList().get(0));
		//+-+-+-+-+-
		g.clear();
		not.setRot(1);
		not2 = Circuit.loadedCircuits.get("Not").clone();
		not2.setRot(0);
		Circuit and = Circuit.loadedCircuits.get("And").clone();
		g.add(and, 1, 1);
		assertFalse(and.outputList().get(0));
		g.add(not, 1, 0);
		g.add(not2, 0, 1);
		assertTrue(and.outputList().get(0));
	}
	
	@Test
	public void gameFromString(){
		Game g = new Game("name;3;Input,1,1,0;Input,0,0,1;And,0,1,1;Output,0,2,1;");
		assertFalse(g.outputBusAtDir(1).get(0));
		g.toggle(0, 1);
		g.toggle(1, 0);
		assertTrue(g.outputBusAtDir(1).get(0));
		
		Game xor = Game.loadedGames.get("Xor");
		assertFalse(xor.outputBusAtDir(1).get(0));
		xor.toggle(0, 2);
		assertTrue(xor.outputBusAtDir(1).get(0));
		xor.toggle(2, 0);
		assertFalse(xor.outputBusAtDir(1).get(0));
	}
	
	@Test
	public void inputCircuitTest(){
		Game g = new Game(3);
		Circuit input = Circuit.Input.clone();
		g.add(input, 0, 0);
		assertFalse( g.circuitAtPos(0, 0).outputList().get(0) );
		g.toggle(0, 0);
		assertTrue( g.circuitAtPos(0, 0).outputList().get(0) );
	}
	
	@Test
	public void singleCircuitTest(){
		Circuit c = Circuit.loadedCircuits.get("Not");
		c.eval();
		assertTrue(c.outputList().get(0));
	}
	
	@Test
	public void printCircuitTest(){
		Circuit or = Circuit.loadedCircuits.get("Or");
		String[] str = or.printCircuit();
		assertEquals(str[0], " I ");
		assertEquals(str[1], "IoO");
		assertEquals(str[2], "   ");
		
		Circuit not = Circuit.loadedCircuits.get("Not");
		not.addRot(3);
		str = not.printCircuit();
		assertEquals(str[0], " O ");
		assertEquals(str[1], " n ");
		assertEquals(str[2], " I ");
	}
	
	@Test
	public void printGameTest(){
		Game game = Game.loadedGames.get("Game1");
		Game xor = Game.loadedGames.get("Xor");
	}
	
	@Test
	public void evaluatorTest(){
		List<Boolean> list = Arrays.asList(new Boolean[]{false, true});
		for(String s : evalTrue)
			assertTrue(new Evaluator(s).eval(list));
		for(String s : evalFalse)
			assertFalse(new Evaluator(s).eval(list));
	}
	
	@Test
	public void moveTest(){
		Game g = new Game(3);
		g.add(Circuit.loadedCircuits.get("Input").clone(), 0, 1);
		g.add(Circuit.loadedCircuits.get("Bus").clone(), 1, 1);
		g.add(Circuit.loadedCircuits.get("Output").clone(), 2, 1);
		g.toggle(0, 1);
		assertTrue(g.outputBusAtDir(1).get(0));
		
		g.move(1, 1, 1, 0);
		assertFalse(g.outputBusAtDir(1).get(0));
		
		g.move(1, 0, 1, 1);
		assertTrue(g.outputBusAtDir(1).get(0));
	}
	
	@Test
	public void generateBusTest(){
		List<Bus> buses = Bus.generateBuses(4);
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 16; j++)
				if(i != j)
					assertFalse(buses.get(i).equals(buses.get(j)));
				else
					assertTrue(buses.get(i).equals(buses.get(j)));
	}
	
	@Test
	public void generateBusRandomTest(){
		Map<Bus, Integer> map = new HashMap<>();
		double tests = 1000000;
		for(int i = 0; i < tests; i++){
			Bus b = Bus.randomBus(8);
			if(map.containsKey(b))
				map.replace(b, map.get(b) +1);
			else
				map.put(b, 1);
		}
		System.out.println(map);
	}
	
}
