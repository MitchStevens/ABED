package testing;

import org.junit.Test;

import core.eval.Input;
import core.eval.OperationWrapper;
import core.eval.Output;

public class TestingOperationWrapper {
	
	@Test
	public void TEST_bus(){
		OperationWrapper bus = new OperationWrapper();
		bus.add(new Input("in1"));
		bus.add(new Output("out1"));
		
		
	}
	
}
