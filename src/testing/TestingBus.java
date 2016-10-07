package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.circuits.BusIn;
import core.circuits.BusOut;
import core.game.Direction;

public class TestingBus {
	private List<Boolean> a00 = new ArrayList<>();
	private List<Boolean> a01 = new ArrayList<>();
	private List<Boolean> a10 = new ArrayList<>();
	private List<Boolean> a11 = new ArrayList<>();
	
	@Before
	public void init(){
		a00.add(false); a00.add(false);
		a01.add(false); a01.add(true);
		a10.add(true);  a10.add(false);
		a11.add(true);  a11.add(true);
	}
	
	@Test
	public void basicTest(){
		BusIn in = new BusIn(null, 2, Direction.UP);
		BusOut out = new BusOut(null, 0, Direction.UP);
		assertEquals(out.to_list(), a00);
		out.set_outputs(a01);
		assertEquals(in.to_list(), a00);
		
		out.uncouple();
		assertEquals(in.to_list(), a00);
	} 
	
	
}
