package core.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.eval.Mapping;
import core.game.Direction;

public class Input extends Mapping {
	private boolean value = false;
	
	public Input(){
		super("INPUT", "0000", "0100");
		List<Boolean> outs = new ArrayList<>(Arrays.asList(false));
		this.last_outputs.put(Direction.RIGHT, outs);
	}
	
	@Override
	public void toggle(){
		value ^= true;
		eval(null);
		this.setChanged();
		this.notifyObservers();
	}
	
	public List<Boolean> get_values(){
		return this.last_outputs(Direction.RIGHT);
	}
	
	public int size(){
		return 1;
	}

	@Override
	public void eval(List<Boolean> inputs) {
		List<Boolean> outs = new ArrayList<>(Arrays.asList(value));
		this.last_outputs.put(Direction.RIGHT, outs);
	}
	
	@Override
	public Input clone(){
		return new Input();
	}

}
