package testing;

import static org.junit.Assert.*;
import static testing.Tests.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.game.Gate;
import core.tokens.Composite;
import data.Reader;

public class TestingComposite {
	List<Boolean> inputs;
	
	String[] logic_str = new String[]{
		"T", "F", "TTFT",
		"0", "1", "0121",
		"0~", "01+", "01200+++x"
	};
	
	String[] logic_true = new String[]{
		"T", "F~", "T~~",
		"TT~+", "F~F+",
		"00~+", "11x", "10~x"
	};
	String[] logic_false = new String[]{
		"F", "T~", "F~~",
		"FTx", "01x", "0F+"
	};
	List<String> eval_true  = new ArrayList<>(Arrays.asList(logic_true));
	List<String> eval_false = new ArrayList<>(Arrays.asList(logic_false));
	
	@Before
	public void init(){
		inputs = new ArrayList<>(Arrays.asList(new Boolean[]{false, true, false, true}));
	}
	
	@Before
	public void fill_lists(){
		for(int i = 0; i < Tests.NUM_TESTS/2; i++){
			eval_true.add(rand_elem(eval_true)   + rand_elem(eval_true)  + "x");
			eval_false.add(rand_elem(eval_false) + rand_elem(eval_false) + "x");
			eval_true.add(rand_elem(eval_true)   + rand_elem(eval_false) + "+");
			eval_false.add(rand_elem(eval_false) + rand_elem(eval_true)  + "x");
		}
	}
	
	@Test
	public void TEST_reading(){
		assertTrue(Reader.COMPOSITES.size() > 0);
	}
	
	@Test
	public void TEST_predefined_operations(){
		Composite comp = new Composite("11(xor)");
		System.out.println(comp.eval(inputs));
	}
	
	
	@Test
	public void TEST_constructor(){
		Composite comp;
		for(String s : logic_str){
			try{
				comp = new Composite(s);
			}catch(ExceptionInInitializerError e){
				System.err.println("couldn't initalise the string "+ s);
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void TEST_eval_basic(){
		Composite bus = new Composite("0");
		assertFalse(bus.eval(inputs).pop());
		
		Composite not = new Composite("0~");
		assertTrue(not.eval(inputs).pop());
		
		Composite or = new Composite("01+");
		assertTrue(or.eval(inputs).pop());
		
		Composite and = new Composite("01x");
		assertFalse(and.eval(inputs).pop());
	}
	
	@Test
	public void TEST_eval_true(){
		for(String s : eval_true){
			Composite op = new Composite(s);
			assertTrue("The String "+ s +" was not true.", op.eval(inputs).pop());
		}
	}
	
	@Test
	public void TEST_eval_false(){
		for(String s : eval_false){
			Composite op = new Composite(s);
			assertFalse("The String "+ s +" was not true.", op.eval(inputs).pop());
		}
	}
}















