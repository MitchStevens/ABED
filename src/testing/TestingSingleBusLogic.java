package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import core.circuits.BusIn;
import core.circuits.BusOut;

public class TestingSingleBusLogic {
	String[] evalTrue = new String[] {
			"0~",
			"01x",
			"1~~",
			"11x",
			"00x~00x~x" };

	String[] evalFalse = new String[] {
			"1~",
			"00+",
			"0~~",
			"10x",
			"00x~11x~x" };

	@Test
	public void TEST_to_list(){
		for(int i = 0; i < Tests.NUM_TESTS; i++){
			int len = Tests.r.nextInt(16);
			List<Boolean> list = random_bus(len);
			BusOut bus = new BusOut(null, len, 0);
			bus.set_outputs(list);
			assertEquals(list, bus.to_list());
		}
	}
	
//	@Test
//	public void TEST_coupling(){
//		for(int i = 0; i < Tests.NUM_TESTS; i++)
//	}
	
	public static List<Boolean> random_bus(int size){
		List<Boolean> b = new ArrayList<>();
		
		for(int i = 0; i < size; i++)
			b.add(Math.random() > 0.5);
		
		return b;
			
	}
}
