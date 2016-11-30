package core.operations;

import java.util.List;

import core.eval.Mapping;

public class Output extends Mapping {
	
	public Output() {
		super("OUTPUT", "0001", "0000");
	}
	
	public boolean values(){
		return this.last_inputs.get(0);
	}
	
	public int size(){
		return 1;
	}
	
	@Override
	public void eval(List<Boolean> inputs) {
		this.last_inputs = inputs;
	}
	
	@Override
	public Output clone(){
		return new Output();
	}

}
