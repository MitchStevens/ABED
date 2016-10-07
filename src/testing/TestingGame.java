package testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import logic.Game;

import org.junit.Before;
import org.junit.Test;

import circuits.Circuit;
import circuits.Input;
import circuits.Output;

public class TestingGame {
	
	Map<String, Circuit> circuits = new HashMap<>();

	@Before
	public void init(){
		circuits.put("Bus", 	new Circuit("Bus;0,0,0,1;0,1,0,0;0"));
		circuits.put("Left", 	new Circuit("Left;0,0,0,1;1,0,0,0;0"));
		circuits.put("Right",	new Circuit("Right;0,0,0,1;0,0,1,0;0"));
		circuits.put("Output", 	new Circuit("Output;0,0,0,1;0,0,0,0;"));
		circuits.put("Not",		new Circuit("Not;0,0,0,1;0,1,0,0;0 ~"));
		circuits.put("Or", 		new Circuit("Or;1,0,0,1;0,1,0,0;0 1 |"));
		circuits.put("And",		new Circuit("And;1,0,0,1;0,1,0,0;0 1 &"));
	}
	
	@Test
	public void addTest(){
		Game g = new Game(3);
		Input c1 = new Input();
		Circuit c2 = circuits.get("Bus").clone();
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
		Circuit not1 = circuits.get("Not").clone();
		Circuit not2 = circuits.get("Not").clone();
		g.add(not1, 0, 0);
		assertTrue(not1.flattenOutputs().get(0));
		g.add(not2, 1, 0);
		g.printGame();
		assertTrue(not1.flattenOutputs().get(0));
		assertFalse(not2.flattenOutputs().get(0));
	}
	
	@Test
	public void removeTest(){
		Game g = new Game(3);
		Input c1 = new Input();
		Circuit c2 = circuits.get("Bus").clone();
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















