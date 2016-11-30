package testing;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.eval.Function;
import core.eval.Mapping;
import core.game.Direction;
import core.operations.Input;
import core.operations.Output;

public class TestingFunction {
	public Function basic;
	
	final List<Boolean> FF = Arrays.asList(false, false);
	final List<Boolean> FT = Arrays.asList(false, true);
	final List<Boolean> TF = Arrays.asList(true,  false);
	final List<Boolean> TT = Arrays.asList(true,  true);
	final long wait_time = 100l;
	final double CLOCK_SPEED = 200;
	
	@Test
	public void TEST_init(){
		Function f = new Function("funct");
	}
	
	@Test
	public void TEST_remove(){
		Function f = new Function("funct");
		
	}
	
	@Test
	public void TEST_basic() throws Exception{
		Function f = new Function("basic", CLOCK_SPEED);
		Input in = new Input();
		Output out = new Output();
		
		f.add_input(in, Direction.LEFT, "in");
		f.add_output(out, Direction.RIGHT, "out");
		
		f.port("in", Direction.RIGHT, "out", Direction.LEFT);
		
		assertFalse(out.last_inputs().get(0));
		in.toggle();
		
		Tests.assertTrueEventually(wait_time, () -> {out.last_inputs().get(0);});
	}
	
	@Test
	public void TEST_basic_as_function() throws Exception{
		//create bus
		Function bus = new Function("BUS", CLOCK_SPEED);
			Input in_bus = new Input();
			Output out_bus = new Output();
			bus.add_input(in_bus, Direction.LEFT, "in");
			bus.add_output(out_bus, Direction.RIGHT, "out");
			bus.port("in", Direction.RIGHT, "out", Direction.LEFT);		
		
		Function f = new Function("funct");
		Input in = new Input();
		Output out = new Output();
		
		f.add(in, "in");
		f.add(bus, "bus");
		f.add(out, "out");
		
		f.port("in", Direction.RIGHT, "bus", Direction.LEFT);
		f.port("bus", Direction.RIGHT, "out", Direction.LEFT);
		
		assertFalse(out.last_inputs().get(0));
		
		in.toggle();
		Tests.assertTrueEventually(wait_time, () -> {out.last_inputs().get(0);});
	}
	
	@Test
	public void TEST_not() throws Exception{
		Function f = new Function("funct1", CLOCK_SPEED);
		Input in = new Input();
		Mapping not = new Mapping("not", "0001", "0100", "0~");
		Output out = new Output();
		
		f.add(in, "in");
		f.add(not, "not");
		f.add(out, "out");
		
		f.port("in", Direction.RIGHT, "not", Direction.LEFT);
		f.port("not", Direction.RIGHT, "out", Direction.LEFT);	
		
		assertFalse(not.last_inputs().get(0));
		assertTrue(out.last_inputs().get(0));
		in.toggle();
		
		Tests.assertTrueEventually(wait_time, () -> { not.last_inputs().get(0); });
		Tests.assertTrueEventually(wait_time, () -> { not(out.last_inputs().get(0)); });
	}
	
	@Test
	public void TEST_and() throws Exception{
		Function f = new Function("funct", CLOCK_SPEED);
		Input in1 = new Input();
		Input in2 = new Input();
		Mapping and = new Mapping("and", "1001", "0100", "01x");
		Output out = new Output();
		
		f.add(in1, "in1");
		f.add(in2, "in2");
		f.add(and, "and");
		f.add(out, "out");
		
		f.port("in1", Direction.RIGHT, "and", Direction.UP);
		f.port("in2", Direction.RIGHT, "and", Direction.LEFT);
		f.port("and", Direction.RIGHT, "out", Direction.LEFT);
		
		Runnable and0  = ()->{ and.last_inputs().get(0); };
		Runnable and1  = ()->{ and.last_inputs().get(1); };
		Runnable out0  = ()->{ out.last_inputs().get(0); };
		Runnable Nand0 = ()->{ not(and.last_inputs().get(0)); };
		Runnable Nand1 = ()->{ not(and.last_inputs().get(1)); };
		Runnable Nout0 = ()->{ not(out.last_inputs().get(0)); };
		
		Tests.assertTrueEventually(wait_time, Nand0);
		Tests.assertTrueEventually(wait_time, Nand1);
		Tests.assertTrueEventually(wait_time, Nout0);
		in1.toggle();
		
		Tests.assertTrueEventually(wait_time, and0);
		Tests.assertTrueEventually(wait_time, Nand1);
		Tests.assertTrueEventually(wait_time, Nout0);
		in2.toggle();
		
		Tests.assertTrueEventually(wait_time, and0);
		Tests.assertTrueEventually(wait_time, and1);
		Tests.assertTrueEventually(wait_time, out0);
	}
	
	@Test
	public void TEST_and_as_function() throws Exception{
		Function and = new Function("AND", CLOCK_SPEED);
			Input in1_f = new Input();
			Input in2_f = new Input();
			Mapping and_f = new Mapping("and", "1001", "0100", "01x");
			Output out_f = new Output();
			and.add_input(in1_f, Direction.UP, "in1");
			and.add_input(in2_f, Direction.LEFT, "in2");
			and.add(and_f, "and");
			and.add_output(out_f, Direction.RIGHT, "out");
			and.port("in1", Direction.RIGHT, "and", Direction.UP);
			and.port("in2", Direction.RIGHT, "and", Direction.LEFT);
			and.port("and", Direction.RIGHT, "out", Direction.LEFT);
			
		Function f = new Function("funct", CLOCK_SPEED);
		Input in1 = new Input();
		Input in2 = new Input();
		Output out = new Output();
		f.add(in1, "in1");
		f.add(in2, "in2");
		f.add(and, "and");
		f.add(out, "out");
		f.port("in1", Direction.RIGHT, "and", Direction.UP);
		f.port("in2", Direction.RIGHT, "and", Direction.LEFT);
		f.port("and", Direction.RIGHT, "out", Direction.LEFT);
		
		Runnable out_false = ()->{ not(and.last_inputs().get(0)); };
		Runnable out_true  = ()->{ and.last_inputs().get(1); };
		
		assertFalse(out.last_inputs().get(0));
		
		in1.toggle();
		Tests.assertTrueEventually(wait_time, out_false);
		
		in2.toggle();
		Tests.assertTrueEventually(wait_time, out_true);
		
		in1.toggle();
		Tests.assertTrueEventually(wait_time, out_false);
	}
	
	@Test
	public void TEST_xor() throws Exception{
		Function f = new Function("funct", CLOCK_SPEED);
		Input inA 		= new Input();
		Input inB 		= new Input();
		Mapping supA 	= new Mapping("SUP",  "0001", "0110", "0,0");
		Mapping supB 	= new Mapping("SUP",  "0001", "1010", "0,0");
		Mapping nand 	= new Mapping("NAND", "1001", "0100", "01x~");
		Mapping or 		= new Mapping("OR",   "0011", "0100", "01+");
		Mapping and 	= new Mapping("AND",  "1001", "0100", "01x");
		Output out		= new Output();
		
		f.add(inA, 	"inA");
		f.add(inB, 	"inB");
		f.add(supA, "supA");
		f.add(supB, "supB");
		f.add(nand, "nand");
		f.add(or, 	"or");
		f.add(and, 	"and");
		f.add(out, 	"out");
		
		f.port("inA", Direction.RIGHT, 	"supA", Direction.LEFT);
		f.port("inB", Direction.RIGHT, 	"supB", Direction.LEFT);
		f.port("supA", Direction.RIGHT, "nand", Direction.UP);
		f.port("supB", Direction.UP, 	"nand", Direction.LEFT);
		f.port("supA", Direction.DOWN, 	"or", Direction.DOWN);
		f.port("supB", Direction.DOWN, 	"or", Direction.LEFT);
		f.port("nand", Direction.RIGHT, "and", Direction.UP);
		f.port("or", Direction.RIGHT,	"and", Direction.LEFT);
		f.port("and", Direction.RIGHT, 	"out", Direction.LEFT);
		
		Runnable out0  = ()->{ out.last_inputs().get(0); };
		Runnable Nout0 = ()->{ not(out.last_inputs().get(0)); };
		
		Tests.assertTrueEventually(wait_time, Nout0);
		
		inA.toggle();
		Tests.assertTrueEventually(wait_time, out0);
		
		inB.toggle();
		Tests.assertTrueEventually(wait_time, Nout0);
		
		inA.toggle();
		Tests.assertTrueEventually(wait_time, out0);
	}
	
	@Test
	public void TEST_cyclic_graph() throws Exception{
		Function f = new Function("funct");
		Input in = new Input();
		Mapping or = new Mapping("OR", "1001", "0100", "01+");
		Mapping sup = new Mapping("SUP", "0001", "1100", "0,0");
		Output out = new Output();
		int n = 20;
		
		f.add(in, "in");
		f.add(or, "or");
		f.add(sup, "sup");
		f.add(out, "out");
		
		f.port("in", Direction.RIGHT, 	"or", Direction.LEFT);
		f.port("or", Direction.RIGHT, 	"sup", Direction.LEFT);
		f.port("sup", Direction.RIGHT, 	"out", Direction.LEFT);
		f.port("sup", Direction.UP,	 	"or", Direction.UP);
		
		Runnable out0 = ()->{ out.last_inputs().get(0); };
		
		assertFalse(out.last_inputs().get(0));
		
		in.toggle();
		Tests.assertTrueEventually(wait_time, out0);
		
		in.toggle();
		Tests.assertTrueEventually(wait_time, out0);
	}
	
	@Test
	public void TEST_rs_latch() throws InterruptedException{
		Function f = new Function("funct");
		Input inR 		= new Input();
		Input inS 		= new Input();
		Mapping norR 	= new Mapping("NOR", "1001", "0100", "01+~");
		Mapping norS 	= new Mapping("NOR", "1001", "0100", "01+~");
		Mapping supR 	= new Mapping("SUP", "1001", "0100", "0,0");
		Mapping supS 	= new Mapping("SUP", "0011", "0100", "0,0");
		Output outQ		= new Output();
		Output outNQ	= new Output();
		
		f.add(inR, "inR");
		f.add(inS, "inS");
		f.add(norR, "norR");
		f.add(norS, "norS");
		f.add(supR, "supR");
		f.add(supS, "supS");
		f.add(outQ, "outQ");
		f.add(outNQ, "outNQ");
		
		f.port("inR", Direction.RIGHT, 	"norR", Direction.UP);
		f.port("norR", Direction.RIGHT, "supR", Direction.LEFT);
		f.port("supR", Direction.RIGHT, "outQ", Direction.LEFT);
		f.port("inS", Direction.RIGHT, 	"norS", Direction.LEFT);
		f.port("norS", Direction.RIGHT, "supS", Direction.LEFT);
		f.port("supS", Direction.RIGHT, "outNQ", Direction.LEFT);
		f.port("supS", Direction.UP, 	"norR", Direction.LEFT);
		f.port("supR", Direction.DOWN, 	"norS", Direction.UP);
		
		inS.toggle();
	}
	
	@Test
	public void TEST_equal(){
		Function f1 = new Function("function");
		Function f2 = new Function("function");
		
		assertEquals(f1, f1);
		assertEquals(f1, f2);
		f1.add(new Input(), "in");
		
		assertThat(f1, is(not(f2)));
		assertThat(f2, is(not(f1)));
		f1.remove("in");
		
		assertEquals(f1, f2);
	}
}











