package testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import core.eval.Mapping;
import core.game.Coord;
import core.game.Game;
import core.operations.Input;
import core.operations.Output;
import data.Reader;

public class TestingGame {
	
	@Test
	public void TEST_bus() throws Exception{
		Game game = new Game(3);
		Input in    = new Input();
		Mapping bus = new Mapping("BUS", "0001", "0100", "0");
		Output out  = new Output();
		
		game.add(in,  new Coord(1, 0));
		game.add(bus, new Coord(1, 1));
		game.add(out, new Coord(1, 2));
		
		game.print_game();
		System.out.println(game.f);
		
		Thread.sleep(300);
		assertTrue(game.f.num_nodes() == 3);
		assertTrue(game.f.num_edges() == 2);
		assertFalse(out.values());
		in.toggle();
		
		Thread.sleep(300);
		assertTrue(out.values());
	}
	
	@Test
	public void TEST_remove(){
		
	}
	
	@Test
	public void TEST_bus_rot() throws Exception {
		Game game 	= new Game(3);
		Input in	= new Input();
		Mapping bus	= new Mapping("BUS", "0001", "0100", "0");
		Output out	= new Output();
		
		game.add(in,  new Coord(0, 1));
		game.add(bus, new Coord(1, 1));
		game.add(out, new Coord(2, 1));
		
		game.rotate(new Coord(0, 1), 1);
		game.rotate(new Coord(1, 1), 1);
		game.rotate(new Coord(2, 1), 1);
		
		Thread.sleep(300);
		assertTrue(game.f.num_nodes() == 3);
		assertTrue(game.f.num_edges() == 2);
		assertTrue(out.values());
		in.toggle();
	}
	
	@Test
	public void TEST_not() throws Exception {
		Game game 	= new Game(3);
		Input in    = new Input();
		Mapping not = new Mapping("NOT", "0001", "0100", "0~");
		Output out  = new Output();
		
		game.add(in,  new Coord(1, 0));
		game.add(not, new Coord(1, 1));
		game.add(out, new Coord(1, 2));
		
		//game.print_game();
		
		Thread.sleep(300);
		assertTrue(game.f.num_nodes() == 3);
		assertTrue(game.f.num_edges() == 2);
		assertTrue(out.values());
		in.toggle();
		
		Thread.sleep(300);
		assertFalse(out.values());
	}
}















