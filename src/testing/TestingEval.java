package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.Circuit;
import logic.Evaluator;
import logic.Flag;
import logic.Game;
import logic.Operation;
import logic.Token;

import org.junit.Before;
import org.junit.Test;

public class TestingEval {
	int numTests = 50;
	List<Boolean> b01 = new ArrayList<>();
	List<Boolean> b11 = new ArrayList<>();
	String[] trueQs = new String[] {
			"0 1 |",
			"0 1 Or>0",
			"0 1 Xor>0 0 Xor>0",
			"1 0 Xor>0" };
	String[] falseQs = new String[] {
			"0 1 & ~",
			"0 1 And>0 Not>0",
			"0 1 Xor>0"
	};
	
	String[] equiv = new String[]{
		"1", "0 0 1 Xor Xor",
		"0 0 1 Xor Xor", "0 0 1 Xor>0 Xor>0",
		"T", "0 0 ~ |",
		"0 1 2 Full_Adder>0", "0 1 Xor 2 Xor"
	};

	@Before
	public void init() {
		b01.add(false);
		b01.add(true);
		b11.add(true);
		b11.add(true);
	}

	@Test
	public void tokenizeTest() {
		String s = "0 1 2 12 & | Xor>0 Nand>0";
		List<Token> tokens = Evaluator.tokenize(s);
		assertTrue(tokens.get(0) instanceof Flag);
		assertTrue(tokens.get(1) instanceof Flag);
		assertTrue(tokens.get(2) instanceof Flag);
		assertTrue(tokens.get(3) instanceof Flag);
		assertTrue(tokens.get(4) instanceof Operation);
		assertTrue(tokens.get(5) instanceof Operation);
		assertTrue(tokens.get(6) instanceof Operation);
		assertTrue(tokens.get(7) instanceof Operation);
	}

	@Test
	public void evaluateTest() {
		for (String s : trueQs) {
			Evaluator e = new Evaluator(s);
			assertTrue(e.eval(b01));
		}

		for (String s : falseQs) {
			Evaluator e = new Evaluator(s);
			assertFalse(e.eval(b11));
		}

//		List<Boolean> bools = new ArrayList<>(2);
//		bools.add(true);
//		bools.add(true);
//		Random r = new Random();
//		for (int i = 0; i < numTests; i++)
//			for (Circuit c : Circuit.loadedCircuits.values())
//				if (c.inputList().size() == 2) {
//					bools.set(0, r.nextBoolean());
//					bools.set(1, r.nextBoolean());
//
//					Boolean b1 = new Evaluator(c.evals.get(0).logic)
//							.eval(bools);
//					Boolean b2 = new Evaluator("0 1 " + c.name+">0").eval(bools);
//					Boolean b3 = c.evals.get(0).eval(bools);
//					assertEquals(c.name + " failed test.", b1, b2);
//					assertEquals(c.name + " failed test.", b2, b3);
//				}
	}

	@Test
	public void xorTest() {
		Circuit c = Circuit.ALL_CIRCUITS.get("Xor");
		assertTrue(c.evals.get(0).eval(b01));
	}
	
	@Test
	public void test1(){
		Evaluator e1 = new Evaluator("0 ~");
		Evaluator e2 = new Evaluator("0 Not>0");
		assertTrue(e1.equiv(e2));
	}

	 @Test
	 public void equivTest(){
		 for(int i = 0; i < equiv.length; i += 2)
			 assertTrue(new Evaluator(equiv[i]).equiv(new Evaluator(equiv[i+1])));
	 }
}
