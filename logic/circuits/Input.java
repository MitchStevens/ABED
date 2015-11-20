package circuits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import eval.Evaluator;
import javafx.scene.image.Image;

public class Input extends Circuit {
	public boolean value = false;

	public Input() {
		super();
		this.initData = "";
		this.name = "Input";
		this.evals = new ArrayList<>();
		this.buses.set(1, new BusOut(this, 1, 1));
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