package testing;

import logic.Game;

import org.junit.Test;

import circuits.Coord;
import abedgui.GameNavigator;

public class TestingGameNav {
	@Test
	public void test1(){
		Game g = new Game(3);
		GameNavigator gn = new GameNavigator(g);
		System.out.println(gn.getTrail(new Coord(0, 0),  new Coord(2, 2)));
		System.out.println(gn.getTrail(new Coord(0, 0),  new Coord(1, 0)));
	}
}
