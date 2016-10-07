package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import circuits.BusIn;
import circuits.BusOut;

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
		BusIn in = new BusIn(null, 2, 0);
		BusOut out = new BusOut(null, 2, 0);
		assertEquals(out.toBooleanList(), a00);
		out.setOutput(a01);
		assertEquals(in.toBooleanList(), a00);
		
		out.tryCouple(in);
		assertEquals(in.toBooleanList(), a01);
		
		out.uncouple();
		assertEquals(in.toBooleanList(), a00);
	} 
	
	
}
