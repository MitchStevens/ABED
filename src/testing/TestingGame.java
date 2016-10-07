package testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import core.circuits.Input;
import core.circuits.Output;
import core.game.Gate;
import core.game.Game;
import data.Reader;

public class TestingGame {
	
	@Test
	public void addTest(){
		Game g = new Game(3);
		Input c1 = new Input();
		Gate c2 = Reader.get_circuit("BUS");
		Output c3 = new Output();
		
		g.add(c1, 0, 1);
		g.add(c2, 1, 1);
		g.add(c3, 2, 1);
		//System.out.println(g.printGame());
		
		assertFalse(c3.get());
		
		c1.toggle();
		assertTrue(c3.get());
		// +-+-+-+-+-+-+-+-
		g.clear();
		Gate not1 = Reader.get_circuit("NOT");
		Gate not2 = Reader.get_circuit("NOT");
		g.add(not1, 0, 0);
		assertTrue(not1.count_outputs().get(0));
		g.add(not2, 1, 0);
		g.print_game();
		assertTrue(not1.flattenOutputs().get(0));
		assertFalse(not2.flattenOutputs().get(0));
	}
	
	@Test
	public void removeTest(){
		Game g = new Game(3);
		Input c1 = new Input();
		Gate c2 = Reader.get_circuit("BUS");
		Output c3 = new Output();
		
		g.add(c1, 0, 1);
		g.add(c2, 1, 1);
		g.add(c3, 2, 1);
		//System.out.println(g.printGame());
		
		assertFalse(c3.get());
		
		c1.toggle();
		assertTrue(c3.get());
	}
}















