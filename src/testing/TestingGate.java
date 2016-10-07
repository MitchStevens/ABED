package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.Utilities;
import core.circuits.BusIn;
import core.circuits.BusOut;
import core.game.Gate;

public class TestingGate {
	List<Boolean> inputs = new ArrayList<>();
	
	//Test cases:
	String[] valid_gates = new String[]{
		"gate1|0000|0000|",
		"gate2|0010|1000|0",
		"gate3|0011|0000|",
		"gate4|0011|1000|01x",
		"gate5|8800|1000|abx",
		"gate6|"
	};
	
	String[] invalid_gates = new String[]{
		"gate1|0000|0000|0"
	};
	
	@Before
	public void a(){
		inputs.add(false);
		inputs.add(true);
	}
	
	@Test
	public void TEST_bus_creation(){
	}
	
	@Test
	public void TEST_initialise(){		
		//Gate g = new Gate("gate1", "0001", "0100", "0");
	}

	@Test
	public void TEST_eval(){
		Gate g = new Gate("gate1", "0001", "0100", "0~");
		assertTrue(g.get_buses()[0] == null);
		assertTrue(g.get_buses()[1] instanceof BusOut);
		assertTrue(g.get_buses()[2] == null);
		assertTrue(g.get_buses()[3] instanceof BusIn);
		System.out.println(g.get_outputs());
	}

}









