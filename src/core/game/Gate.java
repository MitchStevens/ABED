package core.game;

import static core.Utilities.*;

import java.util.List;
import java.util.Observable;
import java.util.Stack;

import core.circuits.Bus;
import core.circuits.BusIn;
import core.circuits.BusOut;
import core.eval.PureOperation;

public class Gate extends Observable implements Evaluable{
	
	public 			int 					inputs = 0, outputs = 0;
	public 			int 					type = 0;
	public 			String 					name = "";
	public 			PureOperation		 		operation;
	public		 	Bus[] 					buses = new Bus[4];
	// rot is the number of CLOCKWISE rotations
	public 			ObservableRotation		rot;
	public 			Coord 					pos;
	// used to sort circuits in the gui
	public 			Meta 					parent;
	
	//Only use this constructor when creating a new gate from a class.
	public Gate(){}
	
	public Gate(String name, String inputs, String outputs, String op_data){
		this.name = name;
		this.operation = new PureOperation("", op_data);
		init_buses(inputs, outputs);
		this.rot = new ObservableRotation(0);
		pos = Coord.NULL;
	}

	public void toggle() {}
	
	protected void init_buses(String ins, String outs) {
		char[] in  = ins.toCharArray();
		char[] out = outs.toCharArray();
		
		int i, o;
		for(int dir = 0; dir < 4; dir++){
			i = toInt(in[dir]);
			o = toInt(out[dir]);
			
			if(i > 0){
				this.inputs += i;
				this.buses[dir] = new BusIn(this, i, Direction.create(dir));
			} else if(o > 0){
				this.outputs += o;
				this.buses[dir] = new BusOut(this, o, Direction.create(dir));
			} else{
				this.buses[dir] = null;
			}
		}
		
		//assertions
		assert (toInt(ins) ^ toInt(outs)) != 0 : "";
		assert hex_sum(ins)  <= 16 : "There are too many inputs for this Gate.";
		assert hex_sum(outs) <= 16 : "There are too many outputs for this Gate.";
		assert hex_sum(ins)  <= operation.count_inputs() : "";
		assert hex_sum(outs) == operation.count_outputs() : "";
	}
	
	public void eval() {
		// evaluates tile and updates outputBus
		List<Boolean> inputs  = get_inputs();
		Stack<Boolean> outputs = this.operation.eval(inputs);

		for(int dir = 0; dir < 4; dir++)
			if(buses[dir] instanceof BusOut)
				((BusOut)buses[dir]).set_outputs(popn(outputs, buses[dir].size));
		
		assert outputs.size() == 0;
		this.setChanged();
		this.notifyObservers();
	}
	
	public Bus bus_at(int dir) {
		// returns output at dir, taking into account rotation
		return buses[mod4(dir - rot.get())];
	}

	public String pos() {
		return pos.toString();
	}

	@Override
	public Gate clone() {
		//TODO:
		Gate c 	= new Gate();
		return c;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Gate))
			return false;
		Gate c = (Gate) o;
		try{
			if(!name.equals(c.get_name()))
				return false;
			
			if(!pos.equals(c.get_pos()))
				return false;
		}catch(NullPointerException npe){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean equiv(Evaluable e) {
		//TODO
		if(!(e instanceof Gate))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		String tbr = name + ";";
		String in = "", out = "";
		
		for(int dir = 0 ; dir < 4; dir++)
			if(buses[dir] instanceof BusIn){
				in  += toHex(buses[dir].size);
				out += "0";
			} else if(buses[dir] instanceof BusOut){
				out += toHex(buses[dir].size);
				in  += "0";
			} else {
				in  += "0";
				out += "0";
			}

		return tbr+in+";"+out+";("+ operation +")";
	}

	@Override
	public String get_name() {
		return name;
	}

	@Override
	public void set_name(String name) {
		this.name = name;
	}

	@Override
	public Bus[] get_buses() {
		return buses;
	}

	@Override
	public Meta get_parent() {
		return parent;
	}
	
	@Override
	public void set_parent(Meta m){
		this.parent = m;
	}

	@Override
	public Coord get_pos() {
		return pos;
	}

	@Override
	public void set_pos(Coord c) {
		this.pos.set(c);
	}

	@Override
	public int get_rot() {
		return rot.get();
	}

	@Override
	public void set_rot(int new_rot) {
		this.rot.set(new_rot);
	}
	
	@Override
	public void add_rot(int turn){
		this.rot.add(turn);
	}

	@Override
	public void toggle(Object o) {
		// TODO Auto-generated method stub
		
	}
	
}