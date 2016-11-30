package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.eval.Mapping;
import core.game.Direction;

public class TestingMapping {
	List<Boolean> inputs = Arrays.asList(false, true);
	
	@Test
	public void TEST_init(){
		Mapping mapping = new Mapping("map1", "0001", "0100", "0");
	}
	
	@Test
	public void TEST_eval_single_output(){
		Mapping map0 = new Mapping("map0", "0001", "0100", "0~");
		map0.eval(inputs);
		assertTrue(map0.last_outputs(Direction.RIGHT).get(0));
		
		Mapping map1 = new Mapping("map1", "1001", "0100", "01+");
		map1.eval(inputs);
		assertTrue(map1.last_outputs(Direction.RIGHT).get(0));
		
		Mapping map2 = new Mapping("map2", "1001", "0100", "01x");
		map2.eval(inputs);
		assertFalse(map2.last_outputs(Direction.RIGHT).get(0));
		
		Mapping map3 = new Mapping("map3", "1001", "0100", "01(xor)");
		map3.eval(inputs);
		assertTrue(map3.last_outputs(Direction.RIGHT).get(0));
	}
	
	@Test
	public void TEST_eval_multiple_output(){
		Mapping map0 = new Mapping("map0", "1001", "0200", "01");
		map0.eval(inputs);
		assertEquals(map0.last_outputs(Direction.RIGHT), inputs);
	}
	
}



























