package testing;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import logic.Reader;

import org.junit.Test;

import circuits.Circuit;

public class TestingReader {

	// @Test
	// public void getCircuitsTest(){
	// Map<String, Circuit> list = Reader.loadCircuits();
	// assertTrue(list.size() > 0);
	// }
	//
	@Test
	public void getImagesTest() {
		Reader.loadImages();
	}
}
