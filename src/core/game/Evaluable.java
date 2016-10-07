package core.game;

import static core.Utilities.mod4;
import static core.Utilities.popn;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import core.circuits.Bus;
import core.circuits.BusIn;
import core.circuits.BusOut;

public interface Evaluable {
	
	public int		get_rot();
	public void		set_rot(int rot);
	public void		add_rot(int rot);
	
	public Coord	get_pos();
	public void		set_pos(Coord c);
	
	public String 	get_name();
	public void		set_name(String name);
	
	public Meta 	get_parent();
	public void		set_parent(Meta m);
	
	public Bus[] get_buses();
	
	/**
	 * Returns the bus in the absolute direction, that is the direction with respect to the Meta that it is enclosed within.
	 * This means that we have to take into account the rotation of the Evaluator
	 * */
	public default Bus get_bus(Direction d){
		return get_buses()[d.rot(get_rot()).value];
	}
	
	public void toggle(Object o);
	
	public default Evaluable adj(Direction d){
		try{
			return get_parent().get(get_pos().at(d));
		}catch(NullPointerException npe){
			return null;
		}
	}
	
	public boolean equiv(Evaluable e);
	
	public default void connect_buses(){
		for(Bus b : get_buses())
			if(b != null)
				b.couple();
	}
	public default void disconnect_buses(){
		for(Bus b : get_buses())
			if(b != null)
				b.uncouple();
	}
	
	public default List<Boolean> get_inputs(){
		List<Boolean> inputs = new ArrayList<>();
		for(Bus b : get_buses())
			if(b instanceof BusIn){
				inputs.addAll(b.to_list());
			}
		return inputs;
	}
	public default List<Boolean> get_outputs(){
		List<Boolean> outputs = new ArrayList<>();
		for(Bus b : get_buses())
			if(b instanceof BusOut)
				outputs.addAll(b.to_list());
		return outputs;
	}
	
	/**
	 * Set the stack of outputs to the correct buses. If there are too many outputs 
	 * */
	public default void set_outputs(Stack<Boolean> outputs){
		for(Bus b : get_buses())
			if(b instanceof BusOut)
				((BusOut) b).set_outputs(popn(outputs, b.size));
	
		assert(outputs.isEmpty());
	}
	
	/**
	 * Using the inputs, evaluate this object and send the outputs to the BusOuts
	 * */
	public void eval();
	
	public default String[] repr(){
		String[] tbr = new String[3];
		tbr[0] = "      ";
		tbr[1] = "  " + get_name().substring(0, 2).toUpperCase() + "  ";
		tbr[2] = "      ";
		
		Bus b;
		Bus[] buses = get_buses();
		int rot = get_rot();
		if ((b = buses[mod4(0-rot)]) != null)
			tbr[0] = (b instanceof BusIn ? "  \\/  " : "  /\\  ");
		
		if ((b = buses[mod4(1-rot)]) != null)
			tbr[1] = tbr[1].substring(0, 4) + (b instanceof BusIn ? "<<" : ">>");
		
		if ((b = buses[mod4(2-rot)]) != null)
			tbr[2] = (b instanceof BusIn ? "  /\\  " : "  \\/  ");

		if ((b = buses[mod4(3-rot)]) != null)
			tbr[1] = (b instanceof BusIn ? ">>" : "<<") + tbr[1].substring(2, 6);

		return tbr;
	}
}
