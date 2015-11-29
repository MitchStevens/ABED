package circuits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import eval.Evaluator;

public class Input extends Circuit {
	public boolean value = false;
	public int capacity = 1;

	public Input() {
		super();
		this.initData = "";
		this.name = "Input";
		this.evals = new ArrayList<>();
		initBuses("0,0,0,0", "0,1,0,0");
		this.rot = 0;
	}

	public void setValue(Boolean b) {
		value = b;
	}

	public void toggle() {
		// toggles value true->false, false->true. It is my very favorite hack.
		value ^= true;
		((BusOut)this.buses.get(1)).setOutput(value);
		this.notifyObservers();
		eval();
	}
	
	public Integer input_num(){
		int count = 0;
		for(int i = 0; i < 4; i++)
			for(Circuit c : game.circuitsOnEdge(i, "Input")){
				if(c == this)
					return count;
				else
					count++;
			}
		return null;
	}
	
	@Override
	public Queue<Boolean> eval() {
		Queue<Boolean> queue = new LinkedList<>();
		queue.add(value);
		this.setChanged();
		this.notifyObservers();
		return queue;
	}

	public Circuit clone() {
		return new Input();
	}

}