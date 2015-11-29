package testing;

import static org.junit.Assert.*;
import logic.Game;

import org.junit.Test;

import circuits.Circuit;
import circuits.Coord;

public class TestingCircuit {
	
	@Test
	public void equiv_test(){
		Game g = new Game(4);
		g.add(Circuit.allCircuits.get("Input").clone(),  new Coord(0, 1));
		g.add(Circuit.allCircuits.get("Not").clone(),    new Coord(1, 1));
		g.add(Circuit.allCircuits.get("Not").clone(),    new Coord(2, 1));
		g.add(Circuit.allCircuits.get("Output").clone(), new Coord(3, 1));
		g.printGame();
		
		assertTrue(g.toCircuit().equiv(Circuit.allCircuits.get("Bus").clone()));
		assertFalse(g.toCircuit().equiv(Circuit.allCircuits.get("Not").clone()));
	}
	
}
