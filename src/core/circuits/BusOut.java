package core.circuits;

import static core.Utilities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import core.game.Direction;
import core.game.Evaluable;
import core.game.Gate;

public class BusOut extends Bus{
	private 	List<Boolean> 	bools;
	private 	BusIn	 		spouse;
	
	/**
	 * The default constructor for a BusOut.
	 * 
	 * @param c The parent circuit.
	 * @param size The number of outputs this bus supports. Usually a power of 2.
	 * @param dir The direction the bus is facing
	 * */
	public BusOut(Evaluable parent, int size, Direction dir){
		this.parent = parent;
		this.size = size;
		this.dir = dir;
		bools = Collections.unmodifiableList(Collections.nCopies(size, false));
	}

	@Override
	public List<Boolean> to_list() {
		return bools;
	}
	
	public void set_outputs(Boolean... bools){
		set_outputs(Arrays.asList(bools));
	}
	
	public void set_outputs(List<Boolean> newBools){
		if(bools.equals(newBools))
			return;
		else if(bools.size() != newBools.size())
			return;
		else{
			bools = newBools;
		}
	}
	
	@Override
	public Bus get_spouse(){
		return spouse;
	}

	@Override
	public boolean set_spouse(Bus b) {
		if(b instanceof BusIn)
			if(b.size == size){
				this.spouse = (BusIn)b;
				return true;
			}
		return false;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		return;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(!(o instanceof BusOut)) return false;
		BusOut b = (BusOut)o;
		
		return
			this.size == b.size &&
			this.dir == b.dir;
	}
}

