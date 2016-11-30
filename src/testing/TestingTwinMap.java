package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import core.game.TwinMap;

public class TestingTwinMap {
	
	@Test
	public void a(){
		TwinMap<String, Integer, String> twin_map = new TwinMap<>();
		twin_map.put("1", 1, "one");
		assertEquals(twin_map.get("1"), "one");
		assertEquals(twin_map.get(1),	"one");
		assertEquals(twin_map.equiv(1), "1");
		assertEquals(twin_map.equiv("1"), 1);
		assertTrue(twin_map.containsKey(1));
		assertTrue(twin_map.containsKey("1"));
		assertTrue(twin_map.containsValue("one"));
		
		twin_map.remove("1");
		assertFalse(twin_map.containsKey(1));
		assertFalse(twin_map.containsKey("1"));
		assertFalse(twin_map.containsValue("one"));
	}
	
}
