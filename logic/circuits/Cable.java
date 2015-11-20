package circuits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eval.Evaluator;

public class Cable extends Circuit {
	public static int MAX_CAPACITY = 8;
	//input direction is always 3
	public int capacity;
	
	public Cable(){
		super("Cable;0,0,0,1;0,1,0,0;0");
		this.capacity = 1;
	}
	
	public Cable(String datum){
		super(datum);
	}
	
	public Cable(int capacity){
		super("Cable;0,0,0,1;0,1,0,0;0");
		this.capacity = capacity;
	}
	
	public void toggle_output(int dir){
		if(dir < 0 || dir > 2)
			throw new Error("direction "+ dir +" invalid.");
		
		if(buses.get(dir) == null){
			BusOut bus = new BusOut(this, capacity, dir);
			this.buses.set(dir, bus);
			this.outputs++;
			this.evals.add(new Evaluator("0"));			
		} else {
			this.buses.set(dir, null);
			this.evals.remove(0);
			this.outputs--;
		}
		
		String init = "Cable;0,0,0,1;";
		for(int i = 0; i <= 2 ; i++)
			init += (this.buses.get(i) instanceof Bus ? "1" : "0")+",";
		
		init += "0;";
			
		for(int i = 0; i < outputs; i++)
			init += "0,";
		this.initData = init;
		
		this.eval();
	}
	
	public int get_output_num(){
		//returns a number the represents the number of outputs
		//that are currently outputing something
		int num = 0;
		if(buses.get(0) != null) num += 1;
		if(buses.get(1) != null) num += 2;
		if(buses.get(2) != null) num += 4;
		return num;
	}

	@Override
	public Cable clone() {
		return new Cable();
	}
	
}
