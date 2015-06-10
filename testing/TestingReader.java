package testing;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import logic.Circuit;

import org.junit.Test;

import data.Reader;

public class TestingReader {
	
	@Test
	public void getCircuitsTest(){
		Map<String, Circuit> list = Reader.loadCircuits();
		assertTrue(list.size() > 0);
	}
	
}
