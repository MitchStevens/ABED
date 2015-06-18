package testing;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import logic.Circuit;
import logic.Evaluator;
import logic.Game;

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
		//+-+-+-+-+-
		g.clear();
		g.add(Circuit.loadedCircuits.get("Input").clone(), 0, 0);
		g.add(Circuit.loadedCircuits.get("Bus").clone(), 1, 0);
		g.add(Circuit.loadedCircuits.get("Output").clone(), 2, 0);
		g.toggle(0, 0);
		assertTrue(g.outputsAtDir(1).get(0));
		
		g.move(1, 0, 1, 1);
		assertFalse(g.outputsAtDir(1).get(0));
		
		g.move(1, 1, 1, 0);
		assertTrue(g.outputsAtDir(1).get(0));
	}
	
	@Test
	public void gameTestRot(){
		Game g = new Game(3);
		Circuit not = Circuit.loadedCircuits.get("Not").clone();
		not.addRot(1);
		Circuit or = Circuit.loadedCircuits.get("Or").clone();
		or.addRot(1);
		g.add(or, 0, 1);
		assertFalse(or.outputList().get(0));
		g.add(not, 0, 0);
		assertTrue(or.outputList().get(0));
		//+-+-+-+-+-
		g.clear();
		not.setRot(1);
		Circuit not2 = Circuit.loadedCircuits.get("Not").clone();;
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
		assertFalse(g.outputsAtDir(1).get(0));
		g.toggle(0, 1);
		g.toggle(1, 0);
		assertTrue(g.outputsAtDir(1).get(0));
		
		Game xor = Game.loadedGames.get("Xor");
		assertFalse(xor.outputsAtDir(1).get(0));
		xor.toggle(0, 2);
		assertTrue(xor.outputsAtDir(1).get(0));
		xor.toggle(2, 0);
		assertFalse(xor.outputsAtDir(1).get(0));
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
		Game g = new Game(2);
		g.add(Circuit.Input, 0, 0);
		g.add(Circuit.loadedCircuits.get("Output"), 1, 0);
		g.toggle(0, 0);
		assertTrue(g.outputsAtDir(1).get(0));
		g.move(g.circuitAtPos(1, 0), 1, 1);
		assertFalse(g.outputsAtDir(1).get(0));
	}

}
