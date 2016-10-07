package core.circuits;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import core.eval.Evaluator;
import core.game.Gate;

public class Cable extends Gate {
	public static 	int MAX_CAPACITY = 8;
	//input direction is always 3
	public 			int capacity;
	public			boolean[] output_toggle;
	
	public Cable(){
		super("CABLE", "0001", "1110", "0");
		this.capacity = 1;
		this.output_toggle = new boolean[]{false, true, false};
		this.type = 0;
	}
	
	public Cable(int output_num){
		super("CABLE", "0,0,0,1", "1,1,1,0", "0");
		this.name = "CABLE";
		this.capacity = 1;
		this.output_toggle = new boolean[]{false, false, false};
		output_toggle[0] = (output_num & 1) == 1;
		output_toggle[1] = (output_num & 2) == 2;
		output_toggle[2] = (output_num & 4) == 4;
		this.type = 0;
	}
	
//	public Cable(int output_num, int capacity){
//		super("CABLE;0,0,0,1;0,1,0,0;0");
//		this.capacity = capacity;
//	}
	
	public void toggle_output(int dir){
		output_toggle[dir] ^= true;
		this.notifyObservers();
		this.eval();
	}
	
	@Override
	public Queue<Boolean> eval() {
		boolean value = this.count_inputs().get(0);
		
		Queue<Boolean> queue = new LinkedList<>();
		
		for(int i = 0; i <=2; i++){
			boolean b = output_toggle[i] && value;
			((BusOut)this.buses.get(i)).set_outputs(b);
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

	public String getData() {
		return "CABLE" +get_output_num()+ "," + rot + "," + coord.i + "," + coord.j;
	}
	
	@Override
	public Cable clone() {
		return new Cable(get_output_num());
	}
	
}
