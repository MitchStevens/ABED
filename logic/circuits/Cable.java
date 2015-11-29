package circuits;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import eval.Evaluator;

public class Cable extends Circuit {
	public static 	int MAX_CAPACITY = 8;
	//input direction is always 3
	public 			int capacity;
	public			boolean[] output_toggle;
	
	public Cable(){
		super("Cable;0,0,0,1;1,1,1,0;0");
		this.capacity = 1;
		this.output_toggle = new boolean[]{false, true, false};
	}
	
	public Cable(String datum){
		super(datum);
	}
	
	public Cable(int capacity){
		super("Cable;0,0,0,1;0,1,0,0;0");
		this.capacity = capacity;
	}
	
	public void toggle_output(int dir){
		output_toggle[dir] ^= true;
		this.notifyObservers();
		this.eval();
	}
	
	@Override
	public Queue<Boolean> eval() {
		boolean value = this.flattenInputs().get(0);
		
		Queue<Boolean> queue = new LinkedList<>();
		
		for(int i = 0; i <=2; i++){
			boolean b = output_toggle[i] && value;
			((BusOut)this.buses.get(i)).setOutput(b);
			queue.add(b);
		}
		
		this.setChanged();
		this.notifyObservers();
		return queue;
	}
	
	public int get_output_num(){
		//returns a number the represents the number of outputs
		//that are currently outputing something
		int num = 0;
		if(output_toggle[0] == true) num += 1;
		if(output_toggle[1] == true) num += 2;
		if(output_toggle[2] == true) num += 4;
		return num;
	}

	@Override
	public Cable clone() {
		return new Cable();
	}
	
}
